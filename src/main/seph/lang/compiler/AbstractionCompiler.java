/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import seph.lang.*;
import seph.lang.ast.*;
import seph.lang.persistent.*;
import seph.lang.parser.StaticScope;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.ActivationHelpers.*;
import static seph.lang.Types.*;
import static seph.lang.compiler.SephCallSite.*;
import static seph.lang.compiler.MethodAdapter.EMPTY;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    public static boolean PRINT_COMPILE = false;

    private final static org.objectweb.asm.MethodHandle BOOTSTRAP_METHOD = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/SephCallSite", "bootstrap", BOOTSTRAP_SIGNATURE_DESC);

    private final static AtomicInteger compiledCount = new AtomicInteger(0);

    private final Message code;
    private final LexicalScope capture;

    private List<LiteralEntry>  literals = new LinkedList<>();
    private List<ArgumentEntry> arguments = new LinkedList<>();

    private Class<?> abstractionClass;

    private final ClassWriter cw;
    public final String className;
    private final List<String> argNames;

    private int messageIndex = 0;

    private boolean printThisClass = false;

    private final SemiStaticScope scope;

    private final String abstractionName;

    private final seph.lang.Runtime runtime;

    private static class ScopeEntry {
        public final int depth;
        public final int index;
        public ScopeEntry(int depth, int index) {
            this.depth = depth;
            this.index = index;
        }
    }

    public static class SemiStaticScope {
        public final List<String> names;
        public final SemiStaticScope parent;
        public SemiStaticScope(List<String> names, SemiStaticScope parent) {
            this.names = names;
            this.parent = parent;
        }

        public boolean hasName(String name) {
            if(names.contains(name)) {
                return true;
            }
            if(parent != null) {
                return parent.hasName(name);
            }
            return false;
        }

        public ScopeEntry find(String name) {
            return find(name, 0);
        }
        
        private ScopeEntry find(String name, int depth) {
            int ix = names.indexOf(name);
            if(ix != -1) {
                return new ScopeEntry(depth, ix);
            } else if(parent != null) {
                return parent.find(name, depth + 1);
            } else {
                return null;
            }
        }
    }

    private AbstractionCompiler(seph.lang.Runtime runtime, Message code, List<String> argNames, LexicalScope capture, StaticScope scope, SemiStaticScope parentScope, String name) {
        this.code = code;
        this.argNames = argNames;
        this.capture = capture;
        this.abstractionName = name;
        this.runtime = runtime;
        this.className = "seph$gen$abstraction$" + compiledCount.getAndIncrement() + "$" + encode(abstractionName);
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        final java.io.File sourceFile = new java.io.File(code.filename());
        this.cw.visitSource(code.filename(), sourceFile.getAbsolutePath());

        List<String> newNames = new LinkedList<>();
        for(String s : scope.getNames()) {
            if(!parentScope.hasName(s)) {
                newNames.add(s);
            }
        }
        for(String s : scope.getShadowing()) {
            if(!newNames.contains(s)) {
                newNames.add(s);
            }
        }
        this.scope = new SemiStaticScope(newNames, parentScope);
    }

    private void generateAbstractionClass() {
        cw.visit(V1_7, ACC_PUBLIC, p(className), null, p(SimpleSephObject.class), new String[0]);

        activateWithMethod();
        activationForMethod();
        abstractionFields();
        constructor(SimpleSephObject.class);

        cw.visitEnd();

        final byte[] classBytes = cw.toByteArray();

        if(printThisClass || PRINT_COMPILE || runtime.configuration().doPrintBytecode()) {
            new ClassReader(classBytes).accept(new org.objectweb.asm.util.TraceClassVisitor(new java.io.PrintWriter(System.err)), 0);
            // try {
            //     java.io.FileOutputStream ff = new java.io.FileOutputStream(className + ".class");
            //     ff.write(classBytes);
            //     ff.close();
            // } catch(Exception e) {
            // }
        }

        try {
            abstractionClass = seph.lang.Runtime.LOADER.defineClass(className, classBytes);
        } catch(Throwable e) {
            new ClassReader(classBytes).accept(new org.objectweb.asm.util.TraceClassVisitor(new java.io.PrintWriter(System.err)), 0);
            throw new RuntimeException(e);
        }
    }

    private SephObject instantiateAbstraction() {
        try {

            return (SephObject)abstractionClass.getConstructor(LexicalScope.class).newInstance(capture);
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
        }
    }

    private final static MethodType ARGUMENT_METHOD_TYPE = MethodType.methodType(SephObject.class, LexicalScope.class, SephObject.class, SThread.class, LexicalScope.class, boolean.class, boolean.class);
    private void setStaticValues() {
        try {
            Field f = abstractionClass.getDeclaredField("fullMsg");
            f.setAccessible(true);
            f.set(null, code);

            for(LiteralEntry le : literals) {
                f = abstractionClass.getDeclaredField(le.name);
                f.setAccessible(true);
                f.set(null, le.code.literal());
            }

            for(ArgumentEntry ae : arguments) {
                f = abstractionClass.getDeclaredField(ae.codeName);
                f.setAccessible(true);
                f.set(null, ae.argumentCode);

                MethodHandle h = findStatic(abstractionClass, ae.methodName, ARGUMENT_METHOD_TYPE);
                f = abstractionClass.getDeclaredField(ae.handleName);
                f.setAccessible(true);
                f.set(null, h);
            }
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
        }
    }

    private void abstractionFields() {
        cw.visitField(ACC_PRIVATE + ACC_STATIC, "fullMsg", c(Message.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_FINAL, "capture", c(LexicalScope.class), null, null);
    }

    private void constructor(Class<?> superClass) {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, "<init>", sig(void.class, LexicalScope.class), null, null));
        ma.loadThis();
        ma.dup();
        ma.dup();

        ma.getStatic(PersistentArrayMap.class, "EMPTY", PersistentArrayMap.class);
        ma.getStatic(SimpleSephObject.class, "activatable", Symbol.class);
        ma.getStatic(seph.lang.Runtime.class, "TRUE", SephObject.class);

        ma.virtualCall(PersistentArrayMap.class, "associate", IPersistentMap.class, Object.class, Object.class);
        ma.init(superClass, void.class, IPersistentMap.class);

        ma.loadLocal(1);

        ma.putField(className, "capture", LexicalScope.class);

        ma.ret();
        ma.end();
    }

    public static SephObject compile(seph.lang.Runtime runtime, Message code, List<String> argNames, LexicalScope capture, StaticScope scope, SemiStaticScope parent, String name) {
        AbstractionCompiler c = new AbstractionCompiler(runtime, code, argNames, capture, scope, parent, name);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.instantiateAbstraction();
    }

    public static String compile(seph.lang.Runtime runtime, Message code, List<String> argNames, StaticScope scope, SemiStaticScope parent, String name) {
        AbstractionCompiler c = new AbstractionCompiler(runtime, code, argNames, null, scope, parent, name);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.className;
    }

    public static SephObject compile(seph.lang.Runtime runtime, ISeq argumentsAndCode, LexicalScope capture, StaticScope scope, SemiStaticScope parent, String name) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile(runtime, (Message)RT.first(argumentsAndCode), argNames, capture, scope, parent, name);
    }

    public static String compile(seph.lang.Runtime runtime, ISeq argumentsAndCode, StaticScope scope, SemiStaticScope parent, String name) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile(runtime, (Message)RT.first(argumentsAndCode), argNames, scope, parent, name);
    }

    private static class LiteralEntry {
        public final String name;
        public final Message code;
        public final int position;
        public LiteralEntry(String name, Message code, int position) {
            this.name = name;
            this.code = code;
            this.position = position;
        }
    }

    private static class ArgumentEntry {
        public final String codeName;
        public final String handleName;
        public final String methodName;
        public final Message argumentCode;
        public final String keyword;

        public ArgumentEntry(String codeName, String handleName, String methodName, Message argumentCode, String keyword) {
            this.codeName = codeName;
            this.handleName = handleName;
            this.methodName = methodName;
            this.argumentCode = argumentCode;
            this.keyword = keyword;
        }
    }



















    private void compileLiteral(MethodAdapter ma, Message current) {
        int position = literals.size();
        LiteralEntry le = new LiteralEntry("literal" + position, current, position);
        literals.add(le);
        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, le.name, c(SephObject.class), null, null);

        ma.pop();
        ma.getStatic(className, le.name, SephObject.class);
    }

    private void compileTerminator(MethodAdapter ma, Message current, int[] layout) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            ma.pop();
            ma.loadLocal(layout[RECEIVER_IDX]);
        }
    }

    private static final Message SENTINEL = new LiteralMessage(null, null, null, -1, -1);

    private String currentAssignment = null;
    
    private void compileAssignment(MethodAdapter ma, Assignment current, int[] layout) {
        Message left = (Message)current.arguments().seq().first();
        Message right = (Message)current.arguments().seq().next().first();
        String name = left.name();
        ScopeEntry se = scope.find(name);

        String oldAssignment = currentAssignment;
        currentAssignment = name;

        //        printThisClass = true;
        switch(current.getAssignment()) {
        case EQ:
            compileCode(ma, right, SENTINEL, layout);   // [val]
            ma.dup(); // [val, val]
            ma.loadLocal(layout[METHOD_SCOPE_IDX]);  // [val, val, scope]
            ma.swap(); // [val, scope, val]
            ma.load(se.depth); // [val, scope, val, depth]
            ma.swap(); // [val, scope, depth, val]
            ma.load(se.index); // [val, scope, depth, val, index]
            ma.swap(); // [val, scope, depth, index, val]
            ma.virtualCall(LexicalScope.class, "assign", void.class, int.class, int.class, SephObject.class);
            break;
        case PLUS_EQ:
            compileCode(ma, left, SENTINEL, layout);
            compileCode(ma, NamedMessage.create("+", new PersistentList(right), null, left.filename(), left.line(), left.position(), null), SENTINEL, layout);
            ma.dup();
            ma.loadLocal(layout[METHOD_SCOPE_IDX]);
            ma.swap();
            ma.load(se.depth); // [val, scope, val, depth]
            ma.swap(); // [val, scope, depth, val]
            ma.load(se.index); // [val, scope, depth, val, index]
            ma.swap(); // [val, scope, depth, index, val]
            ma.virtualCall(LexicalScope.class, "assign", void.class, int.class, int.class, SephObject.class);
            break;
        default:
            assert false : "Should never reach here - the compiler has to cover all cases of assignment, but is missing: " + current.getAssignment();
        }

        currentAssignment = oldAssignment;
    }

    private void compileArgument(Message argument, int currentMessageIndex, int argIndex, List<ArgumentEntry> currentArguments, Message last, List<org.objectweb.asm.MethodHandle> mhsAndAsts) {
        //               printThisClass = true;
        String keyword = null;
        Message argumentToCompile = argument;
        String name = argument.name();
        if(name.endsWith(":")) {
            keyword = name.substring(0, name.length() - 1);
            argumentToCompile = argument.next();
        }

        final String codeName   = "code_arg_" + currentMessageIndex + "_" + argIndex;
        final String handleName = "handle_arg_" + currentMessageIndex + "_" + argIndex;
        final String methodName = "argument_" + currentMessageIndex + "_" + argIndex;
        ArgumentEntry ae = new ArgumentEntry(codeName, handleName, methodName, argument, keyword);
        mhsAndAsts.add(new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC,  className, methodName, sig(SephObject.class, LexicalScope.class, SephObject.class, SThread.class, LexicalScope.class, boolean.class, boolean.class)));
        mhsAndAsts.add(new org.objectweb.asm.MethodHandle(MH_GETSTATIC,     className, codeName, c(SephObject.class)));

        arguments.add(ae);
        currentArguments.add(ae);

        cw.visitField(ACC_PRIVATE + ACC_STATIC, codeName,   c(SephObject.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, handleName, c(MethodHandle.class), null, null);

        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, sig(SephObject.class, LexicalScope.class, SephObject.class, SThread.class, LexicalScope.class, boolean.class, boolean.class), null, null));
        
        int[] layout = VARIABLE_LAYOUT_ARGUMENT_METHOD;

        ma.loadLocalInt(layout[SHOULD_EVALUATE_IDX]);
        ma.zero();

        Label els = new Label();
        ma.ifNotEqual(els);

        ma.getStatic(className, codeName, SephObject.class);
        ma.retValue();

        ma.label(els);

        Message current = argumentToCompile;

        ma.loadLocal(layout[METHOD_SCOPE_IDX]);
        ma.getField(LexicalScope.class, "ground", SephObject.class);
        ma.dup();
        ma.storeLocal(layout[RECEIVER_IDX]);

        boolean first = true;

        while(current != null) {
            ma.line(current.line());
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current, layout);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String aname = currentAssignment;
                if(aname == null) {
                    aname = "__inline__";
                }
                String newName = AbstractionCompiler.compile(this.runtime, RT.seq(current.arguments()), ((Abstraction)current).scope, scope, aname);
                ma.create(newName);
                ma.dup();
                ma.loadLocal(0);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, layout);
                first = false;
            } else {
                compileMessageSend(ma, current, layout, first, last);
                first = false;
            }
            current = current.next();
        }

        ma.retValue();
        ma.end();
    }

    private static class Arity {
        public final int positional;
        public final int keyword;
        public Arity(int positional, int keyword) {
            this.positional = positional;
            this.keyword = keyword;
        }
    }

    private Arity countArguments(IPersistentList arguments) {
        final int arity = RT.count(arguments);
        int keywordArgs = 0;
        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            Message m = (Message)seq.first();
            if(m.name().endsWith(":")) {
                keywordArgs++;
            }
        }
        return new Arity(arity - keywordArgs, keywordArgs);
    }

    private org.objectweb.asm.MethodHandle[] compileArguments(MethodAdapter ma, IPersistentList arguments, int[] layout, Message last) {
        int num = 0;
        final int currentMessageIndex = messageIndex++;

        final int arity = RT.count(arguments);
        final List<ArgumentEntry> currentArguments = new ArrayList<>();
        final List<Message> keywordArguments = new LinkedList<>();
        final List<String> keywordArgumentNames = new LinkedList<>();
        final List<org.objectweb.asm.MethodHandle> mhsAndAsts = new LinkedList<>();


        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            Message m = (Message)seq.first();
            if(m.name().endsWith(":")) {
                keywordArgumentNames.add(m.name().substring(0, m.name().length() - 1));
                keywordArguments.add(m);
            } else {
                compileArgument((Message)seq.first(), currentMessageIndex, num++, currentArguments, last, mhsAndAsts);
            }
        }

        final LinkedList<ArgumentEntry> keywordCurrentArguments = new LinkedList<>();
        for(Message m : keywordArguments) {
            compileArgument(m, currentMessageIndex, num++, keywordCurrentArguments, last, mhsAndAsts);
        }

        if((arity - keywordArguments.size()) > 5) {
            ma.load(arity - keywordArguments.size());
            ma.newArray(MethodHandle.class);
            int i = 0;
            for(ArgumentEntry ae : keywordCurrentArguments) {
                ma.dup();
                ma.load(i++);
                
                ma.getStatic(className, ae.handleName, MethodHandle.class);
                ma.loadLocal(layout[METHOD_SCOPE_IDX]);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(layout[RECEIVER_IDX]);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);

                ma.storeArray();
            }
        } else {
            for(ArgumentEntry ae : currentArguments) {
                // printThisClass = true;
                ma.getStatic(className, ae.handleName, MethodHandle.class);

                ma.loadLocal(layout[METHOD_SCOPE_IDX]);

                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(layout[RECEIVER_IDX]);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
            }
        }

        if(keywordArguments.size() > 0) {
            ma.load(keywordArgumentNames.size());
            ma.newArray(String.class);
            int i = 0;
            for(String s : keywordArgumentNames) {
                ma.dup();
                ma.load(i++);
                ma.load(s);
                ma.storeArray();
            }

            ma.load(keywordArgumentNames.size());
            ma.newArray(MethodHandle.class);
            i = 0;
            for(ArgumentEntry ae : keywordCurrentArguments) {
                ma.dup();
                ma.load(i++);
                ma.getStatic(className, ae.handleName, MethodHandle.class);

                ma.loadLocal(layout[METHOD_SCOPE_IDX]);

                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(layout[RECEIVER_IDX]);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.storeArray();
            }
        }

        return mhsAndAsts.toArray(new org.objectweb.asm.MethodHandle[0]);
    }


    private final static int[] VARIABLE_LAYOUT_ACTIVATE_WITH   = new int[7];
    private final static int[] VARIABLE_LAYOUT_ARGUMENT_METHOD = new int[7];

    private final static int RECEIVER_IDX               = 0;
    private final static int THREAD_IDX                 = 1;
    private final static int SCOPE_IDX                  = 2;
    private final static int ARGUMENTS_IDX              = 3;
    private final static int METHOD_SCOPE_IDX           = 4;
    private final static int SHOULD_EVALUATE_IDX        = 5;
    private final static int SHOULD_EVALUATE_FULLY_IDX  = 6;

    static {
        VARIABLE_LAYOUT_ACTIVATE_WITH[RECEIVER_IDX]     = 1;
        VARIABLE_LAYOUT_ACTIVATE_WITH[THREAD_IDX]       = 2;
        VARIABLE_LAYOUT_ACTIVATE_WITH[SCOPE_IDX]        = 3;
        VARIABLE_LAYOUT_ACTIVATE_WITH[ARGUMENTS_IDX]    = 4;
        VARIABLE_LAYOUT_ACTIVATE_WITH[METHOD_SCOPE_IDX] = 5;

        VARIABLE_LAYOUT_ARGUMENT_METHOD[ARGUMENTS_IDX]             = -1;  // argument evaluation doesn't have any arguments
        VARIABLE_LAYOUT_ARGUMENT_METHOD[METHOD_SCOPE_IDX]          = 0;
        VARIABLE_LAYOUT_ARGUMENT_METHOD[THREAD_IDX]                = 2;
        VARIABLE_LAYOUT_ARGUMENT_METHOD[SCOPE_IDX]                 = 3;
        VARIABLE_LAYOUT_ARGUMENT_METHOD[SHOULD_EVALUATE_IDX]       = 4;
        VARIABLE_LAYOUT_ARGUMENT_METHOD[SHOULD_EVALUATE_FULLY_IDX] = 5;
        VARIABLE_LAYOUT_ARGUMENT_METHOD[RECEIVER_IDX]              = 6;
    }


    private void pumpTailCall(MethodAdapter ma, int[] layout) {
        if(runtime.configuration().doTailCallOptimization()) {
            ma.loadLocal(layout[THREAD_IDX]);
            ma.dynamicCall("seph:pumpTailCall", sig(SephObject.class, SephObject.class, SThread.class), BOOTSTRAP_METHOD);
        }
    }

    private Class[] argumentArrayFor(Arity arity) {
        if(arity.keyword > 0) {
            switch(arity.positional) {
            case 0:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class};
            case 1:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 2:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 3:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 4:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 5:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            default:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class, String[].class, MethodHandle[].class};
            }
        } else {
            switch(arity.positional) {
            case 0:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class};
            case 1:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class};
            case 2:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class};
            case 3:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            case 4:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            case 5:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            default:
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class};
            }
        }
    }

    private Class[] argumentArrayFor2(Arity arity) {
        if(arity.keyword > 0) {
            switch(arity.positional) {
            case 0:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class};
            case 1:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 2:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 3:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 4:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            case 5:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
            default:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class, String[].class, MethodHandle[].class};
            }
        } else {
            switch(arity.positional) {
            case 0:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class};
            case 1:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class};
            case 2:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class};
            case 3:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            case 4:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            case 5:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
            default:
                return new Class[]{SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class};
            }
        }
    }


    private String sigFor(Arity arity) {
        return sig(SephObject.class, argumentArrayFor(arity));
    }

    private String sigFor2(Arity arity) {
        return sig(SephObject.class, argumentArrayFor2(arity));
    }

    private void compileIfStatement(MethodAdapter ma, Message current, int[] layout, boolean first, Message last, Arity arity) {
        ISeq seq = current.arguments().seq();
        Message conditional = (Message)seq.first();
        Message _then = null;
        Message _else = null;
        if((seq = seq.next()) != null) {
            _then = (Message)seq.first();
            if((seq = seq.next()) != null) {
                _else = (Message)seq.first();
            }
        }

        compileCode(ma, conditional, SENTINEL, layout);
        ma.interfaceCall(SephObject.class, "isTrue", boolean.class);
        Label elseBranch = new Label();
        Label endIf = new Label();
        ma.zero();

        ma.ifEqual(elseBranch);

        if(_then != null) {
            ma.loadLocal(layout[RECEIVER_IDX]);
            Message newLast = current == last ? findLast(_then) : SENTINEL;
            compileCode(ma, _then, newLast, layout);
            if(current != last) {
                pumpTailCall(ma, layout);
            }
        } else {
            ma.getStatic(seph.lang.Runtime.class, "NIL", SephObject.class);
        }
        ma.jump(endIf);
        ma.label(elseBranch);
        if(_else != null) {
            ma.loadLocal(layout[RECEIVER_IDX]);
            Message newLast = current == last ? findLast(_else) : SENTINEL;
            compileCode(ma, _else, newLast, layout);
            if(current != last) {
                pumpTailCall(ma, layout);
            }
        } else {
            ma.getStatic(seph.lang.Runtime.class, "NIL", SephObject.class);
        }
        ma.label(endIf);
    }


    private void loadFromDepth(int depth, int index, MethodAdapter ma) {
        int currentDepth = depth;
        while(currentDepth-- > 0) {
            ma.getField(LexicalScope.class, "parent", LexicalScope.class);
        }
     
        switch(index) {
        case 0:
            ma.cast(LexicalScope.One.class);
            ma.getField(LexicalScope.One.class, "value0", SephObject.class);
            break;
        case 1:
            ma.cast(LexicalScope.Two.class);
            ma.getField(LexicalScope.Two.class, "value1", SephObject.class);
            break;
        case 2:
            ma.cast(LexicalScope.Three.class);
            ma.getField(LexicalScope.Three.class, "value2", SephObject.class);
            break;
        case 3:
            ma.cast(LexicalScope.Four.class);
            ma.getField(LexicalScope.Four.class, "value3", SephObject.class);
            break;
        case 4:
            ma.cast(LexicalScope.Five.class);
            ma.getField(LexicalScope.Five.class, "value4", SephObject.class);
            break;
        case 5:
            ma.cast(LexicalScope.Six.class);
            ma.getField(LexicalScope.Six.class, "value5", SephObject.class);
            break;
        default:
            ma.cast(LexicalScope.Many.class);
            ma.getField(LexicalScope.Many.class, "values", SephObject[].class);
            ma.load(index-6);
            ma.loadArray();
            break;
        }
    }

    private void compileRegularMessageSend(MethodAdapter ma, Message current, int[] layout, boolean first, Message last, Arity arity, String name) {
        ScopeEntry se = null;
        Label noActivate = null;
        String messageType = "message";
        String possibleAdditional = "";

        if(first && (se = scope.find(name)) != null) {
            ma.loadLocal(layout[METHOD_SCOPE_IDX]);
            
            if(runtime.configuration().doLexicalMethodHandleLookup()) {
                ma.dynamicCall("seph:lookup:" + encode(name) + ":lexical:" + se.depth + ":" + se.index, sig(SephObject.class, LexicalScope.class), BOOTSTRAP_METHOD);
            } else {
                loadFromDepth(se.depth, se.index, ma);
            }

            if(!runtime.configuration().doLexicalMethodHandleInvoke()) {
                noActivate = new Label();
                ma.dup();
                ma.interfaceCall(SephObject.class, "isActivatable", boolean.class);
                ma.zero();
                ma.ifEqual(noActivate);
     
                ma.load(arity.positional);
                ma.load(arity.keyword == 0 ? 0 : 1);
                ma.interfaceCall(SephObject.class, "activationFor", MethodHandle.class, int.class, boolean.class);
            
                if(runtime.configuration().doTailCallOptimization() && current == last) {
                    ma.swap();
                    ma.load(0);
                }
            }

            ma.swap();
        }        

        ma.loadLocal(layout[THREAD_IDX]);
        ma.loadLocal(layout[METHOD_SCOPE_IDX]);
            
        org.objectweb.asm.MethodHandle[] argMHrefs = compileArguments(ma, current.arguments(), layout, last);

        if(first && se != null) {
            // [recv, value, thread, scope, arg0, arg1]

            if(runtime.configuration().doLexicalMethodHandleInvoke()) {
                boolean fullPumping = false;

                messageType = "invoke";
                if(runtime.configuration().doTailCallOptimization() && current == last) {
                    messageType = "tailInvoke";
                    fullPumping = true;
                }

                ma.dynamicCall("seph:" + messageType + ":" + encode(name), sigFor2(arity), BOOTSTRAP_METHOD, argMHrefs);
                if(runtime.configuration().doTailCallOptimization()) {
                    if(fullPumping) {
                        if(layout[SHOULD_EVALUATE_FULLY_IDX] != 0) {
                            Label noPump = new Label();
                            ma.loadLocalInt(layout[SHOULD_EVALUATE_FULLY_IDX]);
                            ma.zero();
                            ma.ifEqual(noPump);
                            pumpTailCall(ma, layout);
                            ma.label(noPump);
                        }
                    } else {
                        pumpTailCall(ma, layout);
                    }
                }
            } else {
                if(runtime.configuration().doTailCallOptimization() && current == last) {
                    Label activate = new Label();
                    int len = argumentArrayFor(arity).length;
                    ma.load(len);
                    ma.newArray(Object.class);
                    for(int i = len - 1; i >= 0; i--) {
                        ma.dup_x1();
                        ma.swap();
                        ma.load(i);
                        ma.swap();
                        ma.storeArray();
                    }
                    ma.staticCall(MethodHandles.class, "insertArguments", MethodHandle.class, MethodHandle.class, int.class, Object[].class);
                    ma.loadLocal(layout[THREAD_IDX]);
                    ma.swap();
                    ma.putField(SThread.class, "tail", MethodHandle.class);
                    ma.getStatic(SThread.class, "TAIL_MARKER", SephObject.class);

                    if(layout[SHOULD_EVALUATE_FULLY_IDX] != 0) {
                        Label noPump = new Label();
                        ma.loadLocalInt(layout[SHOULD_EVALUATE_FULLY_IDX]);
                        ma.zero();
                        ma.ifEqual(noPump);
                        pumpTailCall(ma, layout);
                        ma.label(noPump);
                    }

                    ma.jump(activate);
                    ma.label(noActivate);

                    ma.swap();
                    ma.pop();

                    ma.label(activate);
                } else {
                    Label activate = new Label();
                    ma.virtualCall(MethodHandle.class, "invokeExact", sigFor(arity));

                    pumpTailCall(ma, layout);
                    ma.jump(activate);
                    ma.label(noActivate);

                    ma.swap();
                    ma.pop();
                    ma.label(activate);
                }
            }
        } else {
            boolean fullPumping = false;

            if(runtime.configuration().doTailCallOptimization() && current == last) {
                messageType = "tailMessage";
                fullPumping = true;
            }

            ma.dynamicCall("seph:" + messageType + ":" + encode(name) + possibleAdditional, sigFor(arity), BOOTSTRAP_METHOD, argMHrefs);
            if(runtime.configuration().doTailCallOptimization()) {
                if(fullPumping) {
                    if(layout[SHOULD_EVALUATE_FULLY_IDX] != 0) {
                        Label noPump = new Label();
                        ma.loadLocalInt(layout[SHOULD_EVALUATE_FULLY_IDX]);
                        ma.zero();
                        ma.ifEqual(noPump);
                        pumpTailCall(ma, layout);
                        ma.label(noPump);
                    }
                } else {
                    pumpTailCall(ma, layout);
                }
            }
        }
    }

    private void compileMessageSend(MethodAdapter ma, Message current, int[] layout, boolean first, Message last) {
        final Arity arity = countArguments(current.arguments());
        final String name = current.name().intern();
        if(name == "if" && arity.positional > 0 && arity.positional < 4) {
            compileIfStatement(ma, current, layout, first, last, arity);
        } else {
            compileRegularMessageSend(ma, current, layout, first, last, arity, name);
        }
    }

    private Message findLast(Message code) {
        Message current = code;
        Message lastReal = null;
        while(current != null) {
            if(!(current instanceof Terminator)) {
                lastReal = current;
            }
            current = current.next();
        }
        return lastReal;
    }

    private void compileCode(MethodAdapter ma, Message _code, Message last, int[] layout) {
        boolean first = true;

        Message current = _code;

        while(current != null) {
            ma.line(current.line());
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current, layout);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String aname = currentAssignment;
                if(aname == null) {
                    aname = "__inline__";
                }
                String newName = AbstractionCompiler.compile(this.runtime, RT.seq(current.arguments()), ((Abstraction)current).scope, scope, aname);
                ma.create(newName);
                ma.dup();
                ma.loadLocal(layout[METHOD_SCOPE_IDX]);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, layout);
                first = false;
            } else {
                compileMessageSend(ma, current, layout, first, last);
                first = false;
            }
            current = current.next();
        }
    }

    private void activateWithBody(MethodAdapter ma, int[] layout) {
        ma.loadLocal(layout[RECEIVER_IDX]);
        compileCode(ma, code, findLast(code), layout);
        ma.retValue();
        ma.end();
    }

    // Should only be called for up to five arguments
    private void activateWithMethodRealArity(final int arity) {
        final int[] layout = (int[])VARIABLE_LAYOUT_ACTIVATE_WITH.clone();
        layout[METHOD_SCOPE_IDX] += (arity-1);
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, encode(abstractionName), sigFor(new Arity(arity, 0)), null, null));
        ma.loadThis();
        ma.getField(className, "capture", LexicalScope.class);

        List<String> names = scope.names;
        ma.load(names.size());
        ma.newArray(String.class);
        int ix = 0;
        for(String s : names) {
            ma.dup();
            ma.load(ix++);
            ma.load(s);
            ma.storeArray();
        }
        ma.loadLocal(layout[RECEIVER_IDX]);
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, String[].class, SephObject.class);
        ma.storeLocal(layout[METHOD_SCOPE_IDX]);

        for(int i = 0; i < arity; i++) {
            String name = argNames.get(i);
            ma.loadLocal(layout[METHOD_SCOPE_IDX]);
            ScopeEntry se = scope.find(name);
            ma.load(se.depth);
            ma.load(se.index);
            ma.loadLocal(layout[ARGUMENTS_IDX] + i);
            ma.loadLocal(layout[SCOPE_IDX]);
            ma.loadLocal(layout[THREAD_IDX]);
            ma.one();
            ma.staticCall(ControlDefaultBehavior.class, "evaluateArgument", SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class);
            
            ma.virtualCall(LexicalScope.class, "assign", void.class, int.class, int.class, SephObject.class);
        }

        activateWithBody(ma, layout);
    }


    private void activateWithMethodCollectedArgs() {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, encode(abstractionName), sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class), null, null));

        ma.loadThis();
        ma.getField(className, "capture", LexicalScope.class);
        List<String> names = scope.names;
        ma.load(names.size());
        ma.newArray(String.class);
        int ix = 0;
        for(String s : names) {
            ma.dup();
            ma.load(ix++);
            ma.load(s);
            ma.storeArray();
        }
        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[RECEIVER_IDX]);
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, String[].class, SephObject.class);
        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[METHOD_SCOPE_IDX]);

        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[ARGUMENTS_IDX]);
        ix = 0;
        for(String arg : argNames) {
            ma.dup();
            ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[METHOD_SCOPE_IDX]);
            ma.swap();
            ma.load(ix++);
            ma.loadArray();
            ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[SCOPE_IDX]);
            ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[THREAD_IDX]);
            ma.one();
            ma.staticCall(ControlDefaultBehavior.class, "evaluateArgument", SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class);
            ma.load(arg);
            ma.swap();
            ma.virtualCall(LexicalScope.class, "directlyAssign", void.class, String.class, SephObject.class);
        }

        ma.pop();

        activateWithBody(ma, VARIABLE_LAYOUT_ACTIVATE_WITH);
    }

    private void activateWithMethodPassArgs(final int arity) {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, encode(abstractionName), sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class), null, null));

        ma.loadThis();
        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[RECEIVER_IDX]);
        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[THREAD_IDX]);
        ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[SCOPE_IDX]);

        if(arity > 0) {
            ma.loadLocal(VARIABLE_LAYOUT_ACTIVATE_WITH[ARGUMENTS_IDX]);

            for(int i = 0; i < arity; i++) {
                ma.dup();
                ma.load(i);
                ma.loadArray();
                ma.swap();
            }
            
            ma.pop();
        }

        ma.virtualCall(className, encode(abstractionName), sigFor(new Arity(arity, 0)));

        ma.retValue();
        ma.end();
    }

    private void activateWithMethod() {
        int arity = argNames.size();
        if(arity <= 5) {
            activateWithMethodRealArity(arity);
            activateWithMethodPassArgs(arity);
        } else {
            activateWithMethodCollectedArgs();
        }
    }
    

    private void activationForMethod() {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, "activationFor", sig(MethodHandle.class, int.class, boolean.class), null, null));

        org.objectweb.asm.MethodHandle specific = new org.objectweb.asm.MethodHandle(MH_INVOKEVIRTUAL,  className, encode(abstractionName), sig(SephObject.class, argumentClassesFor(argNames.size(), false)));
        org.objectweb.asm.MethodHandle generic = new org.objectweb.asm.MethodHandle(MH_INVOKEVIRTUAL,  className, encode(abstractionName), sig(SephObject.class, argumentClassesFor(-1, false)));

        ma.loadThis();
        ma.loadLocalInt(1);
        ma.loadLocalInt(2);
        ma.dynamicCall("seph:activationFor:" + encode(abstractionName) + ":" + argNames.size() + ":false", sig(MethodHandle.class, Object.class, int.class, boolean.class), BOOTSTRAP_METHOD, specific, generic);
        ma.retValue();
        ma.end();
    }
}// AbstractionCompiler
