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
                    out.println("import seph.lang.*;");
                    out.println("import seph.lang.persistent.*;");
                    out.println();
                    out.println("public class " + cd.getSimpleName() + "Base {");
                    out.println();
                    for(String parent : parents) {
                        out.println("    public final static SephObject parent_" + parent + " = " + parent + ".instance;");
                    }
                    out.println();
                    for(String cell : methods.keySet()) {
                        out.println("    public final static SephObject cell_" + cell + " = " + cell + "Impl.instance;");
                    }
                    out.println();
                    for(String cell : fields.keySet()) {
                        out.println("    public final static SephObject cell_" + cell + " = " + cell + ".instance;");
                    }
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

            private void generateRegularArgumentEvaluation(MethodDeclaration md, StringBuilder sb) throws IOException {
                boolean hasRestKeywords = false;
                boolean hasRestPositional = false;
                int positionalArity = 0;
                int givenPositional = 0;
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
                    } else if(tname.equals("seph.lang.persistent.IPersistentMap")) {
                        sb.append("args.restKeywords");
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

                out.println("            ArgumentResult args = parseAndEvaluateArguments(thread, scope, arguments, "+positionalArity+", "+hasRestPositional+", " +hasRestKeywords+ ");");
            }

            private void generateUnevaluatedArguments(MethodDeclaration md, StringBuilder sb) throws IOException {
                boolean hasReceiver = false;

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
                    }
                    sep = ", ";
                }

            }

            private void generateMethod(ClassDeclaration cd, String name, MethodDeclaration md) throws IOException {
                out.println("    public final static class " + name + "Impl extends SephMethodObject {");
                out.println();
                out.println("        public final static " + name + "Impl instance = new " + name + "Impl();");
                out.println("        public SephObject activateWith(SThread thread, LexicalScope scope, SephObject receiver, IPersistentList arguments) {");

                SephMethod sm = md.getAnnotation(SephMethod.class);

                StringBuilder sb = new StringBuilder();

                if(sm.evaluateArguments()) {
                    generateRegularArgumentEvaluation(md, sb);
                } else {
                    generateUnevaluatedArguments(md, sb);
                }

                out.println("            return " + cd.getQualifiedName() + "." + md.getSimpleName() + "(" + sb + ");");
                out.println("        }");
                out.println("    }");
                out.println();
            }
        }
    }
}// AnnotationBimCreator
