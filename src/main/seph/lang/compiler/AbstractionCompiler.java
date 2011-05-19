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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static seph.lang.compiler.CompilationHelpers.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    public static boolean DO_COMPILE    = true;
    public static boolean PRINT_COMPILE = false;

    private static org.objectweb.asm.MethodHandle bootstrapNamed(String name) {
        return new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", name, Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    }

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

    private AbstractionCompiler(Message code, List<String> argNames, LexicalScope capture) {
        this.code = code;
        this.argNames = argNames;
        this.capture = capture;
        this.className = "seph$gen$abstraction$" + compiledCount.getAndIncrement();
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    }

    private void generateAbstractionClass() {
        cw.visit(V1_7, ACC_PUBLIC, p(className), null, p(SimpleSephObject.class), new String[0]);

        activateWithMethod();
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

                MethodHandle h = Bootstrap.findStatic(abstractionClass, ae.methodName, ARGUMENT_METHOD_TYPE);
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

    public static SephObject compile(Message code, List<String> argNames, LexicalScope capture) {
        if(!DO_COMPILE) {
            throw new CompilationAborted("Compilation disabled...");
        }

        AbstractionCompiler c = new AbstractionCompiler(code, argNames, capture);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.instantiateAbstraction();
    }

    public static String compile(Message code, List<String> argNames) {
        if(!DO_COMPILE) {
            throw new CompilationAborted("Compilation disabled...");
        }

        AbstractionCompiler c = new AbstractionCompiler(code, argNames, null);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.className;
    }

    public static SephObject compile(ISeq argumentsAndCode, LexicalScope capture) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile((Message)RT.first(argumentsAndCode), argNames, capture);
    }

    public static String compile(ISeq argumentsAndCode) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile((Message)RT.first(argumentsAndCode), argNames);
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

        public ArgumentEntry(String codeName, String handleName, String methodName, Message argumentCode) {
            this.codeName = codeName;
            this.handleName = handleName;
            this.methodName = methodName;
            this.argumentCode = argumentCode;
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
    
    private void compileAssignment(MethodAdapter ma, Assignment current, int plusArity, int scopeIndex) {
        Message left = (Message)current.arguments().seq().first();
        Message right = (Message)current.arguments().seq().next().first();
        String name = left.name();

        switch(current.getAssignment()) {
        case EQ:
            compileCode(ma, plusArity, right, SENTINEL);
            ma.dup();
            ma.loadLocal(scopeIndex);
            ma.swap();
            ma.load(name);
            ma.swap();
            ma.virtualCall(LexicalScope.class, "assign", void.class, String.class, SephObject.class);
            break;
        case PLUS_EQ:
            compileCode(ma, plusArity, left, SENTINEL);
            compileCode(ma, plusArity, NamedMessage.create("+", new PersistentList(right), null, left.filename(), left.line(), left.position()), SENTINEL);
            ma.dup();
            ma.loadLocal(scopeIndex);
            ma.swap();
            ma.load(name);
            ma.swap();
            ma.virtualCall(LexicalScope.class, "assign", void.class, String.class, SephObject.class);
            break;
        }
    }

    private void compileArgument(Message argument, int currentMessageIndex, int argIndex, List<ArgumentEntry> currentArguments) {
        //               printThisClass = true;
        if(argument.name().endsWith(":")) {
            throw new CompilationAborted("No support for compiling keyword arguments");
        }

        final String codeName   = "code_arg_" + currentMessageIndex + "_" + argIndex;
        final String handleName = "handle_arg_" + currentMessageIndex + "_" + argIndex;
        final String methodName = "argument_" + currentMessageIndex + "_" + argIndex;
        ArgumentEntry ae = new ArgumentEntry(codeName, handleName, methodName, argument);
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

        Message current = argument;
        Message last = findLast(current);

        ma.loadLocal(RECEIVER);

        boolean first = true;

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String newName = AbstractionCompiler.compile(RT.seq(current.arguments()));
                ma.create(newName);
                ma.dup();
                ma.loadLocal(0);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, -1, 0);
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

    private int compileArguments(MethodAdapter ma, IPersistentList arguments, boolean activateWith, int plusArity) {
        int num = 0;
        final int currentMessageIndex = messageIndex++;

        final int arity = RT.count(arguments);
        final LinkedList<ArgumentEntry> currentArguments = new LinkedList<>();
        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            compileArgument((Message)seq.first(), currentMessageIndex, num++, currentArguments);
        }
        if(arity > 5) {
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

        return arity;
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

    private String sigFor(int arity) {
        switch(arity) {
        case 0:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
        case 1:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class);
        case 2:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class);
        case 3:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
        case 4:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
        case 5:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
        default:
            return sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class);
        }
    }

    private void compileMessageSend(MethodAdapter ma, Message current, boolean activateWith, int plusArity, boolean first, Message last) {
        ma.loadLocal(THREAD);

        if(activateWith) {
            ma.loadLocal(METHOD_SCOPE + plusArity);
        } else {
            ma.loadLocal(METHOD_SCOPE_ARG);
        }
            
        final int arity = compileArguments(ma, current.arguments(), activateWith, plusArity);

        String possibleIntrinsic = "";

        if(current.name().equals("true")) {
            possibleIntrinsic = "_intrinsic_true";
        } else if(current.name().equals("false")) {
            possibleIntrinsic = "_intrinsic_false";
        } else if(current.name().equals("nil")) {
            possibleIntrinsic = "_intrinsic_nil";
        } else if(current.name().equals("if")) {
            possibleIntrinsic = "_intrinsic_if";
        }

        String bootstrapName = "basicSephBootstrap";
        boolean fullPumping = false;

        if(first) {
            if(current == last) {
                bootstrapName = "noReceiverTailCallSephBootstrap";
                fullPumping = true;
            } else {
                bootstrapName = "noReceiverSephBootstrap";
                fullPumping = false;
            }
        } else if(current == last) {
            bootstrapName = "tailCallSephBootstrap";
            fullPumping = true;
        }

        ma.dynamicCall(encode(current.name()), sigFor(arity), bootstrapNamed(bootstrapName + possibleIntrinsic));
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

    private void compileCode(MethodAdapter ma, int plusArity, Message _code, Message last) {
        boolean first = true;

        Message current = _code;

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(ma, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(ma, current);
                first = true;
            } else if(current instanceof Abstraction) {
                ma.pop();
                String newName = AbstractionCompiler.compile(RT.seq(current.arguments()));
                ma.create(newName);
                ma.dup();
                ma.loadLocal(METHOD_SCOPE + plusArity);
                ma.init(newName, Void.TYPE, LexicalScope.class);
                first = false;
            } else if(current instanceof Assignment) {
                compileAssignment(ma, (Assignment)current, plusArity, METHOD_SCOPE + plusArity);
                first = false;
            } else {
                compileMessageSend(ma, current, true, plusArity, first, last);
                first = false;
            }
            current = current.next();
        }
    }

    private void activateWithBody(MethodAdapter ma, int plusArity) {
        ma.loadLocal(RECEIVER);
        compileCode(ma, plusArity, code, findLast(code));
        ma.retValue();
        ma.end();
    }

    // Should only be called for up to five arguments
    private void activateWithMethodRealArity(final int arity) {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, "activateWith", sigFor(arity), null, null));

        ma.loadThis();
        ma.getField(className, "capture", LexicalScope.class);
        ma.loadLocal(RECEIVER);
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, SephObject.class);
        ma.storeLocal(METHOD_SCOPE - 1 + arity);

        for(int i = 0; i < arity; i++) {
            String name = argNames.get(i);
            ma.loadLocal(METHOD_SCOPE - 1 + arity);
            ma.load(name);
            ma.loadLocal(ARGUMENTS + i);
            ma.loadLocal(SCOPE);
            ma.loadLocal(THREAD);
            ma.one();
            ma.staticCall(ControlDefaultBehavior.class, "evaluateArgument", SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class);
            ma.virtualCall(LexicalScope.class, "directlyAssign", void.class, String.class, SephObject.class);
        }

        activateWithBody(ma, arity - 1);
    }


    private void activateWithMethodCollectedArgs() {
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null));

        ma.loadThis();
        ma.getField(className, "capture", LexicalScope.class);
        ma.loadLocal(RECEIVER);
        ma.virtualCall(LexicalScope.class, "newScopeWith", LexicalScope.class, SephObject.class);
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
        MethodAdapter ma = new MethodAdapter(cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null));

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

        ma.virtualCall(className, "activateWith", sigFor(arity));

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
}// AbstractionCompiler
