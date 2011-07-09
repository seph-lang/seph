/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.anno;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.util.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.util.Collections.*;
import static com.sun.mirror.util.DeclarationVisitors.*;

import seph.lang.SephMethod;
import seph.lang.SephCell;
import seph.lang.SephKind;
import seph.lang.SephSingleton;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AnnotationBimCreator implements AnnotationProcessorFactory {
    private static final Collection<String> supportedAnnotations = unmodifiableCollection(Arrays.asList("seph.lang.SephMethod", "seph.lang.SephCell", "seph.lang.SephSingleton", "seph.lang.SephKind"));
    private static final Collection<String> supportedOptions = emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new AnnotationBimCreatorProcessor(env);
    }

    private static class AnnotationBimCreatorProcessor implements AnnotationProcessor {
        private final AnnotationProcessorEnvironment env;

        AnnotationBimCreatorProcessor(AnnotationProcessorEnvironment env) {
            this.env = env;
        }

        public void process() {
            for(TypeDeclaration typeDecl : env.getSpecifiedTypeDeclarations()) {
                typeDecl.accept(getDeclarationScanner(new SephClassVisitor(), NO_OP));
            }
        }

        private class SephClassVisitor extends SimpleDeclarationVisitor {
            private PrintStream out;
            private StringBuilder tempOut;
            private static final boolean DEBUG = false;

            @Override
            public void visitClassDeclaration(ClassDeclaration cd) {
                try {
                    SephSingleton ss = cd.getAnnotation(SephSingleton.class);
                    SephKind sk = cd.getAnnotation(SephKind.class);
                    if(ss == null && sk == null) {
                        return;
                    }

                    final boolean kind = sk != null;
                
                    String[] parents = ss != null ? ss.parents() : sk.parents();

                    Map<String, MethodDeclaration> methods = new LinkedHashMap<String, MethodDeclaration>();

                    for(MethodDeclaration md : cd.getMethods()) {
                        SephMethod anno = md.getAnnotation(SephMethod.class);
                        if (anno == null) {
                            continue;
                        }

                        String name = md.getSimpleName();
                        methods.put(name, md);
                    }

                    Map<String, FieldDeclaration> fields = new LinkedHashMap<String, FieldDeclaration>();

                    for(FieldDeclaration fd : cd.getFields()) {
                        SephCell anno = fd.getAnnotation(SephCell.class);
                        if (anno == null) {
                            continue;
                        }

                        String name = fd.getSimpleName();
                        fields.put(name, fd);
                    }

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
                    out = new PrintStream(bytes);

                    out.println("/* THIS FILE IS GENERATED. DO NOT EDIT */");
                    out.println("package seph.lang.bim;");
                    out.println();
                    out.println("import java.lang.invoke.*;");
                    out.println("import seph.lang.*;");
                    out.println("import seph.lang.persistent.*;");
                    out.println("import static seph.lang.Types.*;");
                    out.println("import static seph.lang.ActivationHelpers.*;");
                    out.println();
                    out.println("public class " + cd.getSimpleName() + "Base {");
                    out.println();
                    for(String parent : parents) {
                        out.println("    public final static SephObject parent_" + parent + " = " + parent + ".instance;");
                    }
                    out.println();
                    for(String cell : methods.keySet()) {
                        out.println("    public final static SephObject cell_" + cell + " = getSephMethodHandleObject_" + cell + "();");
                    }
                    out.println();
                    for(String cell : fields.keySet()) {
                        out.println("    public final static SephObject cell_" + cell + " = " + cell + ".instance;");
                    }
                    out.println();
                    out.println("    public final static Object IDENTITY = new Object();");
                    out.println();
                    out.println("    public static SephObject get(String name) {");
                    out.println("        name = name.intern();");
                    for(Map.Entry<String,MethodDeclaration> entry : methods.entrySet()) {
                        String name = entry.getKey();
                        SephMethod anno = entry.getValue().getAnnotation(SephMethod.class);
                        if(anno.name().length > 0) {
                            name = anno.name()[0];
                        }
                        out.println("        if(name == \"" + name + "\") return cell_" + entry.getKey() + ";");
                    }
                    for(Map.Entry<String,FieldDeclaration> entry : fields.entrySet()) {
                        SephCell c = entry.getValue().getAnnotation(SephCell.class);
                        String realName = entry.getKey();
                        String cell = c.name().length > 0 ? c.name()[0] : realName;
                        out.println("        if(name == \"" + cell + "\") return cell_" + realName + ";");
                    }
                    out.println();
                    out.println("        SephObject result;");
                    for(String parent : parents) {
                        out.println("        if((result = parent_" + parent + ".get(name)) != null) return result;");
                    }
                    out.println("        return null;");
                    out.println("    }");
                    out.println();
                    for(Map.Entry<String, MethodDeclaration> entry : methods.entrySet()) {
                        String name = entry.getKey();
                        SephMethod anno = entry.getValue().getAnnotation(SephMethod.class);
                        if(anno.name().length > 0) {
                            name = anno.name()[0];
                        }

                        generateMethod(cd, entry.getKey(), entry.getValue(), name);
                    }
                    out.println();
                    out.println("}");
                    out.close();
                    out = null;

                    FileOutputStream fos = new FileOutputStream("src/gen/seph/lang/bim/" + cd.getSimpleName() + "Base.java");
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (IOException ioe) {
                    System.err.println("Failed to generate:");
                    ioe.printStackTrace();
                    System.exit(1);
                }
            }

            private int generateRegularArgumentEvaluation(MethodDeclaration md, StringBuilder sb) throws IOException {
                boolean hasRestKeywords = false;
                boolean hasRestPositional = false;
                int positionalArity = 0;
                boolean hasReceiver = false;

                String sep = "";
                for(ParameterDeclaration pd : md.getParameters()) {
                    sb.append(sep);
                    String tname = pd.getType().toString();
                    if(tname.equals("seph.lang.LexicalScope")) {
                        sb.append("scope");
                    } else if(tname.equals("seph.lang.SephObject")) {
                        if(!hasReceiver) {
                            sb.append("receiver");
                            hasReceiver = true;
                        } else {
                            sb.append("args.arg" + positionalArity);
                            positionalArity++;
                        }
                    } else if(tname.equals("seph.lang.SThread")) {
                        sb.append("thread");
                    } else if(tname.equals("java.lang.String[]")) {
                        sb.append("args.restKeywordNames");
                        hasRestKeywords = true;
                    } else if(hasRestKeywords && tname.equals("java.lang.invoke.MethodHandle[]")) {
                        sb.append("args.restKeywordMHs");
                        hasRestKeywords = true;
                    } else if(tname.equals("seph.lang.SephObject[]")) {
                        sb.append("args.restPositional");
                        hasRestPositional = true;
                    }
                    sep = ", ";
                }

                if(positionalArity > 5) {
                    throw new RuntimeException("Built in functions can't take more than five positional arguments. Use a rest argument instead (for " + md + ")");
                }

                out.println("        SephMethodObject.ArgumentResult args = SephMethodObject.parseAndEvaluateArguments(thread, scope, arguments, "+positionalArity+", "+hasRestPositional+", " +hasRestKeywords+ ");");
                return positionalArity;
            }

            private int generateUnevaluatedArguments(MethodDeclaration md, StringBuilder sb) throws IOException {
                boolean hasReceiver = false;
                int positionalArity = 0;
                boolean restArgs = false;
                String sep = "";
                for(ParameterDeclaration pd : md.getParameters()) {
                    sb.append(sep);
                    String tname = pd.getType().toString();
                    if(tname.equals("seph.lang.LexicalScope")) {
                        sb.append("scope");
                    } else if(tname.equals("seph.lang.SephObject")) {
                        sb.append("receiver");
                    } else if(tname.equals("seph.lang.SThread")) {
                        sb.append("thread");
                    } else if(tname.equals("java.lang.invoke.MethodHandle[]")) {
                        sb.append("arguments");
                        restArgs = true;
                    } else if(tname.equals("java.lang.invoke.MethodHandle")) {
                        sb.append("args.argMH" + positionalArity);
                        positionalArity++;
                    }
                    sep = ", ";
                }

                if(positionalArity > 5) {
                    throw new RuntimeException("Built in functions can't take more than five positional arguments. Use a rest argument instead (for " + md + ")");
                }

                out.println("        SephMethodObject.ArgumentResult args = SephMethodObject.parseAndEvaluateArgumentsUneval(thread, scope, arguments, "+positionalArity+");");
                return restArgs ? -1 : positionalArity;
            }

            private int generateForArityMH(int arity, int currentArity, MethodDeclaration md, ClassDeclaration cd, boolean eval, boolean key) throws IOException {
                if(arity == currentArity) {
                    int args = 0;
                    StringBuilder sb = new StringBuilder();
                    String sep = "";
                    boolean haveReceiver = false;
                    List<String> parameterTypes = new LinkedList<String>();
                    List<String> outputTypes = new LinkedList<String>();

                    for(ParameterDeclaration pd : md.getParameters()) {
                        sb.append(sep);
                        String tname = pd.getType().toString();
                        parameterTypes.add(tname);
                        if(tname.equals("seph.lang.LexicalScope")) {
                            sb.append("scope");
                            outputTypes.add("seph.lang.LexicalScope");
                        } else if(tname.equals("seph.lang.SephObject")) {
                            if(haveReceiver) {
                                sb.append("invokeExact(arg" + (args++)).append(", thread, scope)");;
                                outputTypes.add("invokeExact(SephObject)");
                            } else {
                                haveReceiver = true;
                                sb.append("receiver");
                                outputTypes.add("seph.lang.SephObject");
                            }
                        } else if(tname.equals("java.lang.invoke.MethodHandle")) {
                            sb.append("arg" + (args++));
                            outputTypes.add("java.lang.invoke.MethodHandle");
                        } else if(key && tname.equals("java.lang.invoke.MethodHandle[]")) {
                            sb.append("keywordArguments");
                            outputTypes.add("java.lang.invoke.MethodHandle[]");
                        } else if(key && tname.equals("java.lang.String[]")) {
                            sb.append("keywordNames");
                            outputTypes.add("java.lang.String[]");
                        } else if(tname.equals("seph.lang.SThread")) {
                            sb.append("thread");
                            outputTypes.add("seph.lang.SThread");
                        } else {
                            tempOut.append("        throw new RuntimeException(\"Unexpected argument type: " + tname + ". This is most likely a compiler bug\");").append("\n");
                            return 1;
                        }

                        sep = ", ";
                    }
                    tempOut.append("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");").append("\n");
                
                    if(!eval && !key && parameterTypes.equals(outputTypes)) {
                        return -2;
                    }
                } else if(arity < 0) {
                    int args = 0;
                    StringBuilder sb = new StringBuilder();
                    String sep = "";
                    boolean haveReceiver = false;
                    boolean createArgs = false;
                    for(ParameterDeclaration pd : md.getParameters()) {
                        sb.append(sep);
                        String tname = pd.getType().toString();
                        if(tname.equals("seph.lang.LexicalScope")) {
                            sb.append("scope");
                        } else if(!createArgs && tname.equals("java.lang.invoke.MethodHandle[]")) {
                            sb.append("arguments");
                            createArgs = true;
                        } else if(tname.equals("seph.lang.SephObject")) {
                            if(haveReceiver) {
                                sb.append("invokeExact(arg" + (args++)).append(", thread, scope)");;
                            } else {
                                haveReceiver = true;
                                sb.append("receiver");
                            }
                        } else if(tname.equals("java.lang.invoke.MethodHandle")) {
                            sb.append("arg" + (args++));
                        } else if(key && tname.equals("java.lang.invoke.MethodHandle[]")) {
                            sb.append("keywordArguments");
                        } else if(key && tname.equals("java.lang.String[]")) {
                            sb.append("keywordNames");
                        } else if(tname.equals("seph.lang.SThread")) {
                            sb.append("thread");
                        } else {
                            tempOut.append("        throw new RuntimeException(\"Unexpected argument type: " + tname + ". This is most likely a compiler bug\");").append("\n");
                            return 1;
                        }

                        sep = ", ";
                    }
                    if(createArgs) {
                        tempOut.append("        MethodHandle[] arguments = new MethodHandle[" + currentArity + "];").append("\n");
                        for(int i = 0; i < currentArity; i++) {
                            tempOut.append("        arguments[" + i + "] = arg" + i + ";").append("\n");
                        }
                    }
                    tempOut.append("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");").append("\n");
                } else {
                    return 0;
                }
                return 1;
            }

            private void generateMethod(ClassDeclaration cd, String name, MethodDeclaration md, String realName) throws IOException {
                String[] getters = new String[14];
                int ix = 0;
                int result = 0;

                out.println("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle[] arguments) {");

                SephMethod sm = md.getAnnotation(SephMethod.class);

                StringBuilder sb = new StringBuilder();

                boolean eval = true;
                int arity = -1;
                if(sm.evaluateArguments()) {
                    arity = generateRegularArgumentEvaluation(md, sb);
                } else {
                    arity = generateUnevaluatedArguments(md, sb);
                    eval = false;
                }

                out.println("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");");
                out.println("    }");
                out.println();
                getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_N)";

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope) {").append("\n");
                result = generateForArityMH(arity, 0, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_0)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_0)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 0, \"" + realName + "\", false)";
                }


                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {").append("\n");
                result = generateForArityMH(arity, 1, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_1)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_1)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 1, \"" + realName + "\", false)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {").append("\n");
                result = generateForArityMH(arity, 2, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_2)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_2)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 2, \"" + realName + "\", false)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {").append("\n");
                result = generateForArityMH(arity, 3, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_3)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_3)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 3, \"" + realName + "\", false)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {").append("\n");
                result = generateForArityMH(arity, 4, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_4)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_4)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 4, \"" + realName + "\", false)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {").append("\n");
                result = generateForArityMH(arity, 5, md, cd, eval, false);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_5)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_5)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 5, \"" + realName + "\", false)";
                }


                out.println("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle[] arguments, String[] keywordNames, MethodHandle[] keywordArguments) {");

                sb = new StringBuilder();

                eval = true;
                arity = -1;
                if(sm.evaluateArguments()) {
                    arity = generateRegularArgumentEvaluation(md, sb);
                } else {
                    arity = generateUnevaluatedArguments(md, sb);
                    eval = false;
                }

                out.println("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");");
                out.println("    }");
                out.println();
                getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_N_K)";
                
                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 0, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_0_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_0_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 0, \"" + realName + "\", true)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 1, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_1_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_1_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 1, \"" + realName + "\", true)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 2, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_2_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_2_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 2, \"" + realName + "\", true)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 3, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_3_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_3_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 3, \"" + realName + "\", true)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 4, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_4_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_4_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 4, \"" + realName + "\", true)";
                }

                tempOut = new StringBuilder();
                tempOut.append("    public static SephObject " + name + "(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {").append("\n");
                result = generateForArityMH(arity, 5, md, cd, eval, true);
                tempOut.append("    }").append("\n");
                if(result == 1) {
                    out.print(tempOut.toString());
                    getters[ix++] = "                             l.findStatic(" + cd.getSimpleName() + "Base.class, \"" + name + "\", ACTIVATE_METHOD_TYPE_5_K)";
                } else if(result == -2) {
                    getters[ix++] = "                             l.findStatic(" + cd.getQualifiedName() + ".class, \"" + md.getSimpleName() + "\", ACTIVATE_METHOD_TYPE_5_K)";
                } else {
                    getters[ix++] = "                             arityError(" + arity + ", 5, \"" + realName + "\", true)";
                }



                out.println("    private static SephMethodHandleObject getSephMethodHandleObject_" + name + "() {");
                out.println("        try {");
                out.println("            MethodHandles.Lookup l = MethodHandles.lookup();");
                out.println("            return new SephMethodHandleObject(");
                for(int i = 0; i < getters.length; i++) {
                    out.print(getters[i]);
                    if(i == getters.length - 1) {
                        out.println(");");
                    } else {
                        out.println(",");
                    }
                }
                out.println("        } catch(Throwable e) {");
                out.println("            e.printStackTrace();");
                out.println("            throw new RuntimeException(e);");
                out.println("        }");
                out.println("    }");
            }
        }
    }
}// AnnotationBimCreator
