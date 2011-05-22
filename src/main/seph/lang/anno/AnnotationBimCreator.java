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
                    out.println();
                    out.println("public class " + cd.getSimpleName() + "Base {");

                    out.println("    private static SephMethodHandleObject getSephMethodHandleObject(String name) {");
                    out.println("        try {");
                    out.println("            MethodHandles.Lookup l = MethodHandles.lookup();");
                    out.println("            return new SephMethodHandleObject(");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_0),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_1),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_2),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_3),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_4),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_5),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_0_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_1_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_2_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_3_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_4_K),");
                    out.println("                             l.findStatic(" + cd.getSimpleName() + "Base.class, name, ACTIVATE_METHOD_TYPE_5_K));");
                    out.println("        } catch(Throwable e) {");
                    out.println("            e.printStackTrace();");
                    out.println("            throw new RuntimeException(e);");
                    out.println("        }");
                    out.println("    }");
                    out.println("    private static SephObject invoke(MethodHandle mh, SThread thread, LexicalScope scope) {");
                    out.println("        try {");
                    out.println("            return (SephObject)mh.invoke(thread, scope, true, true);");
                    out.println("        } catch(Throwable e) {");
                    out.println("            e.printStackTrace();");
                    out.println("            throw new RuntimeException(e);");
                    out.println("        }");
                    out.println("    }");
                    out.println();
                    for(String parent : parents) {
                        out.println("    public final static SephObject parent_" + parent + " = " + parent + ".instance;");
                    }
                    out.println();
                    for(String cell : methods.keySet()) {
                        out.println("    public final static SephObject cell_" + cell + " = getSephMethodHandleObject(\""+cell+"\");");
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
                        generateMethod(cd, entry.getKey(), entry.getValue());
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
                    } else if(tname.equals("java.lang.invoke.MethodHandle[]")) {
                        sb.append("args.restKeywordMHs");
                        hasRestKeywords = true;
                    } else if(tname.equals("seph.lang.persistent.IPersistentList")) {
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
                    } else if(tname.equals("seph.lang.persistent.IPersistentList")) {
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

            private void generateForArityMH(int arity, int currentArity, MethodDeclaration md, ClassDeclaration cd, boolean eval, boolean key) throws IOException {
                if(arity == currentArity) {
                    int args = 0;
                    StringBuilder sb = new StringBuilder();
                    String sep = "";
                    boolean haveReceiver = false;
                    for(ParameterDeclaration pd : md.getParameters()) {
                        sb.append(sep);
                        String tname = pd.getType().toString();
                        if(tname.equals("seph.lang.LexicalScope")) {
                            sb.append("scope");
                        } else if(tname.equals("seph.lang.SephObject")) {
                            if(haveReceiver) {
                                sb.append("invoke(arg" + (args++)).append(", thread, scope)");;
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
                            out.println("        throw new RuntimeException(\"Unexpected argument type: " + tname + ". This is most likely a compiler bug\");");
                            return;
                        }

                        sep = ", ";
                    }
                
                    out.println("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");");
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
                        } else if(tname.equals("seph.lang.persistent.IPersistentList")) {
                            sb.append("arguments");
                            createArgs = true;
                        } else if(tname.equals("seph.lang.SephObject")) {
                            if(haveReceiver) {
                                sb.append("invoke(arg" + (args++)).append(", thread, scope)");;
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
                            out.println("        throw new RuntimeException(\"Unexpected argument type: " + tname + ". This is most likely a compiler bug\");");
                            return;
                        }

                        sep = ", ";
                    }
                    if(createArgs) {
                        out.println("        IPersistentList arguments = PersistentList.EMPTY;");
                        for(int i = currentArity-1; i >= 0; i--) {
                            out.println("        arguments = (IPersistentList)arguments.cons(arg" + i + ");");
                        }
                    }
                    out.println("        return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");");
                } else {
                    out.println("        throw new RuntimeException(\"Expected " + arity + " arguments, got " + currentArity + "\");");
                }
            }

            private void generateMethod(ClassDeclaration cd, String name, MethodDeclaration md) throws IOException {
                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, IPersistentList arguments) {");

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

                
                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver) {");
                generateForArityMH(arity, 0, md, cd, eval, false);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0) {");
                generateForArityMH(arity, 1, md, cd, eval, false);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1) {");
                generateForArityMH(arity, 2, md, cd, eval, false);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {");
                generateForArityMH(arity, 3, md, cd, eval, false);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {");
                generateForArityMH(arity, 4, md, cd, eval, false);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {");
                generateForArityMH(arity, 5, md, cd, eval, false);
                out.println("    }");



                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, IPersistentList arguments, String[] keywordNames, MethodHandle[] keywordArguments) {");

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

                
                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 0, md, cd, eval, true);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 1, md, cd, eval, true);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 2, md, cd, eval, true);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 3, md, cd, eval, true);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 4, md, cd, eval, true);
                out.println("    }");

                out.println("    public static SephObject " + name + "(SThread thread, LexicalScope scope, SephObject receiver, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {");
                generateForArityMH(arity, 5, md, cd, eval, true);
                out.println("    }");
            }
        }
    }
}// AnnotationBimCreator
