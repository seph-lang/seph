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
import static seph.lang.compiler.Bootstrap.*;

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

    private AbstractionCompiler(Message code, List<String> argNames, LexicalScope capture, StaticScope scope, SemiStaticScope parentScope, String name) {
        this.code = code;
        this.argNames = argNames;
        this.capture = capture;
        this.abstractionName = name;
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

        if(printThisClass || PRINT_COMPILE) {
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

            MethodHandle h = findVirtual(abstractionClass, encode(abstractionName), methodTypeFor(argNames.size(), false));
            f = abstractionClass.getDeclaredField("ACTIVATION_SPECIFIC");
            f.setAccessible(true);
            f.set(null, h);

            h = findVirtual(abstractionClass, encode(abstractionName), methodTypeFor(-1, false));
            f = abstractionClass.getDeclaredField("ACTIVATION_GENERIC");
            f.setAccessible(true);
            f.set(null, h);
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
        }
    }

    private void abstractionFields() {
        cw.visitField(ACC_PRIVATE + ACC_STATIC, "fullMsg", c(Message.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_FINAL, "capture", c(LexicalScope.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, "ACTIVATION_SPECIFIC", c(MethodHandle.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, "ACTIVATION_GENERIC", c(MethodHandle.class), null, null);
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

    public static SephObject compile(Message code, List<String> argNames, LexicalScope capture, StaticScope scope, SemiStaticScope parent, String name) {
        AbstractionCompiler c = new AbstractionCompiler(code, argNames, capture, scope, parent, name);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.instantiateAbstraction();
    }

    public static String compile(Message code, List<String> argNames, StaticScope scope, SemiStaticScope parent, String name) {
        AbstractionCompiler c = new AbstractionCompiler(code, argNames, null, scope, parent, name);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.className;
    }

    public static SephObject compile(ISeq argumentsAndCode, LexicalScope capture, StaticScope scope, SemiStaticScope parent, String name) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile((Message)RT.first(argumentsAndCode), argNames, capture, scope, parent, name);
    }

    public static String compile(ISeq argumentsAndCode, StaticScope scope, SemiStaticScope parent, String name) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile((Message)RT.first(argumentsAndCode), argNames, scope, parent, name);
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

    private void compileTerminator(MethodAdapter ma, Message current) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            ma.pop();
            ma.loadLocal(RECEIVER);
        }
    }

    private static final Message SENTINEL = new LiteralMessage(null, null, null, -1, -1);

    private String currentAssignment = null;
    
    private void compileAssignment(MethodAdapter ma, Assignment current, int plusArity, int scopeIndex, boolean activateWith) {
        Message left = (Message)current.arguments().seq().first();
        Message right = (Message)current.arguments().seq().next().first();
        String name = left.name();
        ScopeEntry se = scope.find(name);

        String oldAssignment = currentAssignment;
        currentAssignment = name;

        //        printThisClass = true;
        switch(current.getAssignment()) {
        case EQ:
            compileCode(ma, plusArity, right, SENTINEL, activateWith);   // [val]
            ma.dup(); // [val, val]
            ma.loadLocal(scopeIndex);  // [val, val, scope]
            ma.swap(); // [val, scope, val]
            ma.load(se.depth); // [val, scope, val, depth]
            ma.swap(); // [val, scope, depth, val]
            ma.load(se.index); // [val, scope, depth, val, index]
            ma.swap(); // [val, scope, depth, index, val]
            ma.virtualCall(LexicalScope.class, "assign", void.class, int.class, int.class, SephObject.class);
            break;
        case PLUS_EQ:
            compileCode(ma, plusArity, left, SENTINEL, activateWith);
            compileCode(ma, plusArity, NamedMessage.create("+", new PersistentList(right), null, left.filename(), left.line(), left.position(), null), SENTINEL, activateWith);
            ma.dup();
            ma.loadLocal(scopeIndex);
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

    private void compileArgument(Message argument, int currentMessageIndex, int argIndex, List<ArgumentEntry> currentArguments) {
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
        arguments.add(ae);
        currentArguments.add(ae);
        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, codeName,   c(SephObject.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, handleName, c(MethodHandle.class), null, null);

        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, sig(SephObject.class, LexicalScope.class, SephObject.class, SThread.class, LexicalScope.class, boolean.class, boolean.class), null, null));
        ma.loadLocalInt(SHOULD_EVALUATE);
        ma.zero();

        Label els = new Label();
        ma.ifNotEqual(els);

        ma.getStatic(className, codeName, SephObject.class);
        ma.retValue();

        ma.label(els);

        Message current = argumentToCompile;
        Message last = findLast(current);

        ma.loadLocal(RECEIVER);

        boolean first = true;

        while(current != null) {
            ma.line(current.line());
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String aname = currentAssignment;
                if(aname == null) {
                    aname = "__inline__";
                }
                String newName = AbstractionCompiler.compile(RT.seq(current.arguments()), ((Abstraction)current).scope, scope, aname);
                ma.create(newName);
                ma.dup();
                ma.loadLocal(0);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, -1, 0, false);
                first = false;
            } else {
                compileMessageSend(ma, current, false, -1, first, last);
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

    private void compileArguments(MethodAdapter ma, IPersistentList arguments, boolean activateWith, int plusArity) {
        int num = 0;
        final int currentMessageIndex = messageIndex++;

        final int arity = RT.count(arguments);
        final LinkedList<ArgumentEntry> currentArguments = new LinkedList<>();
        final List<Message> keywordArguments = new LinkedList<>();
        final List<String> keywordArgumentNames = new LinkedList<>();

        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            Message m = (Message)seq.first();
            if(m.name().endsWith(":")) {
                keywordArgumentNames.add(m.name().substring(0, m.name().length() - 1));
                keywordArguments.add(m);
            } else {
                compileArgument((Message)seq.first(), currentMessageIndex, num++, currentArguments);
            }
        }

        final LinkedList<ArgumentEntry> keywordCurrentArguments = new LinkedList<>();
        for(Message m : keywordArguments) {
            compileArgument(m, currentMessageIndex, num++, keywordCurrentArguments);
        }

        if((arity - keywordArguments.size()) > 5) {
            ma.getStatic(PersistentList.class, "EMPTY", "Lseph/lang/persistent/PersistentList$EmptyList;");
            for(final Iterator<ArgumentEntry> iter = currentArguments.descendingIterator(); iter.hasNext();) {
                ArgumentEntry ae = iter.next();
                
                ma.getStatic(className, ae.handleName, MethodHandle.class);

                if(activateWith) {
                    ma.loadLocal(METHOD_SCOPE + plusArity); 
                } else {
                    ma.loadLocal(METHOD_SCOPE_ARG);
                }

                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(RECEIVER);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.interfaceCall(IPersistentCollection.class, "cons", IPersistentCollection.class, Object.class);
            }
        } else {
            for(ArgumentEntry ae : currentArguments) {
                // printThisClass = true;
                ma.getStatic(className, ae.handleName, MethodHandle.class);

                if(activateWith) {
                    ma.loadLocal(METHOD_SCOPE + plusArity); 
                } else {
                    ma.loadLocal(METHOD_SCOPE_ARG);
                }

                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(RECEIVER);
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

                if(activateWith) {
                    ma.loadLocal(METHOD_SCOPE + plusArity); 
                } else {
                    ma.loadLocal(METHOD_SCOPE_ARG);
                }

                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.loadLocal(RECEIVER);
                ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
                ma.storeArray();
            }
        }
    }

    private final static int METHOD_SCOPE_ARG       = 0;
    private final static int RECEIVER               = 1;
    private final static int THREAD                 = 2;
    private final static int SCOPE                  = 3;
    private final static int ARGUMENTS              = 4;
    private final static int SHOULD_EVALUATE        = 4;
    private final static int METHOD_SCOPE           = 5;
    private final static int SHOULD_EVALUATE_FULLY  = 5;

    private void pumpTailCall(MethodAdapter ma) {
        Label done = new Label();
        Label loop = new Label();
        ma.label(loop);
        ma.dup();
        if (SThread.TAIL_MARKER == null) {
            ma.ifNonNull(done);
        } else {
            ma.getStatic(SThread.class, "TAIL_MARKER", SephObject.class);
            ma.ifRefNotEqual(done);
        }
        ma.pop();
        ma.loadLocal(THREAD);
        ma.dup();
        ma.getField(SThread.class, "tail", MethodHandle.class);
        ma.swap();
        ma.nul();
        ma.putField(SThread.class, "tail", MethodHandle.class);
        ma.virtualCall(MethodHandle.class, "invoke", SephObject.class);
        ma.jump(loop);
        ma.label(done);
    }

    private int dropFor(Arity arity) {
        return (arity.keyword > 0 ? 2 : 0) + (arity.positional > 5 ? 1 : arity.positional) + 3;
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
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class, String[].class, MethodHandle[].class};
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
                return new Class[]{SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class};
            }
        }
    }

    private String sigFor(Arity arity) {
        return sig(SephObject.class, argumentArrayFor(arity));
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
            ma.load(index);
            ma.loadArray();
            break;
        }
    }

    private void compileMessageSend(MethodAdapter ma, Message current, boolean activateWith, int plusArity, boolean first, Message last) {
        ScopeEntry se = null;
        Label noActivate = null;

        final Arity arity = countArguments(current.arguments());

        if(first && (se = scope.find(current.name())) != null) {
            noActivate = new Label();
            if(activateWith) {
                ma.loadLocal(METHOD_SCOPE + plusArity);
            } else {
                ma.loadLocal(METHOD_SCOPE_ARG);
            }
            loadFromDepth(se.depth, se.index, ma);
            ma.dup();
            ma.interfaceCall(SephObject.class, "isActivatable", boolean.class);
            ma.zero();
            ma.ifEqual(noActivate);

            ma.load(arity.positional);
            ma.load(arity.keyword == 0 ? 0 : 1);
            ma.interfaceCall(SephObject.class, "activationFor", MethodHandle.class, int.class, boolean.class);

            if(current == last) {
                ma.swap();
                ma.load(0);  // [boundActivateWith, recv, 0]
            }

            ma.swap();
        } 

        ma.loadLocal(THREAD);

        if(activateWith) {
            ma.loadLocal(METHOD_SCOPE + plusArity);
        } else {
            ma.loadLocal(METHOD_SCOPE_ARG);
        }
            
        compileArguments(ma, current.arguments(), activateWith, plusArity);

        if(first && se != null) {
            if(current == last) {
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
                ma.loadLocal(THREAD);
                ma.swap();
                ma.putField(SThread.class, "tail", MethodHandle.class);
                ma.getStatic(SThread.class, "TAIL_MARKER", SephObject.class);

                if(!activateWith) {
                    Label noPump = new Label();
                    ma.loadLocalInt(SHOULD_EVALUATE_FULLY);
                    ma.zero();
                    ma.ifEqual(noPump);
                    pumpTailCall(ma);
                    ma.label(noPump);
                }

                ma.jump(activate);
                ma.label(noActivate);

                ma.swap();
                ma.pop();

                ma.label(activate);
            } else {
                Label activate = new Label();
                ma.virtualCall(MethodHandle.class, "invoke", sigFor(arity));

                pumpTailCall(ma);
                ma.jump(activate);
                ma.label(noActivate);

                ma.swap();
                ma.pop();
                ma.label(activate);
            }
        } else {
            String possibleIntrinsic = "";
            String name = current.name().intern();
            if(name == "true" ||
               name == "false" ||
               name == "nil" ||
               name == "if") {
                possibleIntrinsic = ":intrinsic";
            }

            String messageType = "message";
            boolean fullPumping = false;

            if(current == last) {
                messageType = "tailMessage";
                fullPumping = true;
            }

            ma.dynamicCall("seph:" + messageType + ":" + encode(current.name()) + possibleIntrinsic, sigFor(arity), BOOTSTRAP_METHOD);
            if(fullPumping) {
                if(!activateWith) {
                    Label noPump = new Label();
                    ma.loadLocalInt(SHOULD_EVALUATE_FULLY);
                    ma.zero();
                    ma.ifEqual(noPump);
                    pumpTailCall(ma);
                    ma.label(noPump);
                }
            } else {
                pumpTailCall(ma);
            }
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

    private void compileCode(MethodAdapter ma, int plusArity, Message _code, Message last, boolean isActivateWith) {
        boolean first = true;

        Message current = _code;

        while(current != null) {
            ma.line(current.line());
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String aname = currentAssignment;
                if(aname == null) {
                    aname = "__inline__";
                }
                String newName = AbstractionCompiler.compile(RT.seq(current.arguments()), ((Abstraction)current).scope, scope, aname);
                ma.create(newName);
                ma.dup();
                ma.loadLocal(METHOD_SCOPE + plusArity);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, plusArity, METHOD_SCOPE + plusArity, isActivateWith);
                first = false;
            } else {
                compileMessageSend(ma, current, isActivateWith, isActivateWith ? plusArity : -1, first, last);
                first = false;
            }
            current = current.next();
        }
    }

    private void activateWithBody(MethodAdapter ma, int plusArity) {
        ma.loadLocal(RECEIVER);
        compileCode(ma, plusArity, code, findLast(code), true);
        ma.retValue();
        ma.end();
    }

    // Should only be called for up to five arguments
    private void activateWithMethodRealArity(final int arity) {
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
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, String[].class);
        ma.storeLocal(METHOD_SCOPE - 1 + arity);

        for(int i = 0; i < arity; i++) {
            String name = argNames.get(i);
            ma.loadLocal(METHOD_SCOPE - 1 + arity);
            ScopeEntry se = scope.find(name);
            ma.load(se.depth);
            ma.load(se.index);
            ma.loadLocal(ARGUMENTS + i);
            ma.loadLocal(SCOPE);
            ma.loadLocal(THREAD);
            ma.one();
            ma.staticCall(ControlDefaultBehavior.class, "evaluateArgument", SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class);
            
            ma.virtualCall(LexicalScope.class, "assign", void.class, int.class, int.class, SephObject.class);
        }

        activateWithBody(ma, arity - 1);
    }


    private void activateWithMethodCollectedArgs() {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, encode(abstractionName), sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null));

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
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, String[].class);
        ma.loadLocal(METHOD_SCOPE);

        ma.loadLocal(ARGUMENTS);
        ma.interfaceCall(Seqable.class, "seq", ISeq.class);

        for(String arg : argNames) {
            ma.dup();
            ma.loadLocal(METHOD_SCOPE);
            ma.swap();
            ma.interfaceCall(ISeq.class, "first", Object.class);
            ma.loadLocal(SCOPE);
            ma.loadLocal(THREAD);
            ma.one();
            ma.staticCall(ControlDefaultBehavior.class, "evaluateArgument", SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class);
            ma.load(arg);
            ma.swap();
            ma.virtualCall(LexicalScope.class, "directlyAssign", void.class, String.class, SephObject.class);
            ma.interfaceCall(ISeq.class, "next", ISeq.class);
        }

        ma.pop();

        activateWithBody(ma, 0);
    }

    private void activateWithMethodPassArgs(final int arity) {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, encode(abstractionName), sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null));

        ma.loadThis();
        ma.loadLocal(RECEIVER);
        ma.loadLocal(THREAD);
        ma.loadLocal(SCOPE);

        if(arity > 0) {
            ma.loadLocal(ARGUMENTS);
            ma.interfaceCall(Seqable.class, "seq", ISeq.class);

            for(int i = 0; i < arity; i++) {
                ma.dup();
                ma.interfaceCall(ISeq.class, "first", Object.class);
                ma.staticCall(SephMethodObject.class, "ensureMH", MethodHandle.class, Object.class);
                ma.swap();
                ma.interfaceCall(ISeq.class, "next", ISeq.class);
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
        Label wrongArity = new Label();
        Label genericArity = new Label();
        Label ret = new Label();

        ma.load(0);
        ma.loadLocalInt(2);
        ma.ifNotEqual(wrongArity);

        ma.load(argNames.size());
        ma.loadLocalInt(1);
        ma.ifNotEqual(genericArity);

        ma.getStatic(className, "ACTIVATION_SPECIFIC", MethodHandle.class);
        ma.loadThis();
        ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
        ma.jump(ret);

        ma.label(genericArity);
        ma.load(-1);
        ma.loadLocalInt(1);
        ma.ifNotEqual(wrongArity);

        ma.getStatic(className, "ACTIVATION_GENERIC", MethodHandle.class);
        ma.loadThis();
        ma.virtualCall(MethodHandle.class, "bindTo", MethodHandle.class, Object.class);
        ma.jump(ret);

        ma.label(wrongArity);
        ma.load(argNames.size());
        ma.loadLocalInt(1);
        ma.loadLocalInt(2);
        ma.staticCall(ActivationHelpers.class, "wrongArity", MethodHandle.class, int.class, int.class, boolean.class);

        ma.label(ret);
        ma.retValue();
        ma.end();
    }
}// AbstractionCompiler
