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
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static seph.lang.compiler.CompilationHelpers.*;

import java.dyn.MethodHandle;
import java.dyn.MethodHandles;
import java.dyn.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    public static boolean DO_COMPILE    = true;
    public static boolean PRINT_COMPILE = false;

    private final static Object[] EMPTY = new Object[0];
    private final static org.objectweb.asm.MethodHandle basicSephBootstrap      = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "basicSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static org.objectweb.asm.MethodHandle noReceiverSephBootstrap = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "noReceiverSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static org.objectweb.asm.MethodHandle tailCallSephBootstrap   = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "tailCallSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static org.objectweb.asm.MethodHandle noReceiverTailCallSephBootstrap   = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "noReceiverTailCallSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static AtomicInteger compiledCount = new AtomicInteger(0);

    private final Message code;
    private final LexicalScope capture;

    private List<LiteralEntry>  literals = new LinkedList<>();
    private List<ArgumentEntry> arguments = new LinkedList<>();

    private Class<?> abstractionClass;

    private final ClassWriter cw;
    private final String className;
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
            //            throw new CompilationAborted("No support for method calls with arguments");
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
            return (SephObject)abstractionClass.newInstance();
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

            f = abstractionClass.getDeclaredField("capture");
            f.setAccessible(true);
            f.set(null, capture);

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
        cw.visitField(ACC_PRIVATE + ACC_STATIC, "capture", c(LexicalScope.class), null, null);
    }

    private void constructor(Class<?> superClass) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", sig(void.class), null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);   // [recv]
        mv.visitInsn(DUP);           // [recv, recv]
        mv.visitFieldInsn(GETSTATIC, p(PersistentArrayMap.class), "EMPTY", c(PersistentArrayMap.class));   // [recv, recv, EMPTY]
        mv.visitFieldInsn(GETSTATIC, p(SimpleSephObject.class), "activatable", c(Symbol.class));           // [recv, recv, EMPTY, act]
        mv.visitFieldInsn(GETSTATIC, p(seph.lang.Runtime.class), "TRUE", c(SephObject.class));             // [recv, recv, EMPTY, act, true]
        mv.visitMethodInsn(INVOKEVIRTUAL, p(PersistentArrayMap.class), "associate", sig(IPersistentMap.class, Object.class, Object.class)); // [recv, recv, map]
        mv.visitMethodInsn(INVOKESPECIAL, p(superClass), "<init>", sig(void.class, IPersistentMap.class));                                  // [recv]
        mv.visitInsn(RETURN);        // []
        mv.visitMaxs(0,0);
        mv.visitEnd();
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

    public static SephObject compile(ISeq argumentsAndCode, LexicalScope capture) {
        final List<String> argNames = new ArrayList<>();
        if(argumentsAndCode != null) {
            for(;RT.next(argumentsAndCode) != null; argumentsAndCode = RT.next(argumentsAndCode)) {
                argNames.add(((Message)RT.first(argumentsAndCode)).name());
            }
        }
        return compile((Message)RT.first(argumentsAndCode), argNames, capture);
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



















    private void compileLiteral(MethodVisitor mv, Message current) {
        int position = literals.size();
        LiteralEntry le = new LiteralEntry("literal" + position, current, position);
        literals.add(le);
        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, le.name, c(SephObject.class), null, null);

        mv.visitInsn(POP);
        mv.visitFieldInsn(GETSTATIC, className, le.name, c(SephObject.class));
    }

    private void compileTerminator(MethodVisitor mv, Message current) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            mv.visitInsn(POP);
            mv.visitVarInsn(ALOAD, RECEIVER);
        }
    }

    private void compileArgument(MethodVisitor act_mv, Message argument, int currentMessageIndex, int argIndex, List<ArgumentEntry> currentArguments) {
        //               printThisClass = true;
        final String codeName   = "code_arg_" + currentMessageIndex + "_" + argIndex;
        final String handleName = "handle_arg_" + currentMessageIndex + "_" + argIndex;
        final String methodName = "argument_" + currentMessageIndex + "_" + argIndex;
        ArgumentEntry ae = new ArgumentEntry(codeName, handleName, methodName, argument);
        arguments.add(ae);
        currentArguments.add(ae);
        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, codeName,   c(SephObject.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, handleName, c(MethodHandle.class), null, null);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, sig(SephObject.class, LexicalScope.class, SephObject.class, SThread.class, LexicalScope.class, boolean.class, boolean.class), null, null);
        mv.visitCode();

        mv.visitVarInsn(ILOAD, SHOULD_EVALUATE);
        mv.visitInsn(ICONST_0);
        Label els = new Label();
        mv.visitJumpInsn(IF_ICMPNE, els);

        mv.visitFieldInsn(GETSTATIC, className, codeName, c(SephObject.class));
        mv.visitInsn(ARETURN);

        mv.visitLabel(els);


        Message current = argument;
        Message last = findLast(current);

        mv.visitVarInsn(ALOAD, RECEIVER);

        boolean first = true;

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(mv, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(mv, current);
                first = true;
            } else if(current instanceof Abstraction) {
                first = false;
                throw new CompilationAborted("No support for compiling abstractions in abstractions");
            } else if(current instanceof Assignment) {
                first = false;
                throw new CompilationAborted("No support for compiling assignment");
            } else {
                compileMessageSend(mv, current, false, -1, first, last);
                first = false;
            }
            current = current.next();
        }

        mv.visitInsn(ARETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private int compileArguments(MethodVisitor mv, IPersistentList arguments, boolean activateWith, int plusArity) {
        int num = 0;
        final int currentMessageIndex = messageIndex++;

        final int arity = RT.count(arguments);
        final LinkedList<ArgumentEntry> currentArguments = new LinkedList<>();
        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            compileArgument(mv, (Message)seq.first(), currentMessageIndex, num++, currentArguments);
        }
        if(arity > 5) {
            mv.visitFieldInsn(GETSTATIC, p(PersistentList.class), "EMPTY", "Lseph/lang/persistent/PersistentList$EmptyList;");
            for(final Iterator<ArgumentEntry> iter = currentArguments.descendingIterator(); iter.hasNext();) {
                ArgumentEntry ae = iter.next();

                mv.visitFieldInsn(GETSTATIC, className, ae.handleName, c(MethodHandle.class));  // [mh]

                if(activateWith) {
                    mv.visitVarInsn(ALOAD, METHOD_SCOPE + plusArity); 
                } else {
                    mv.visitVarInsn(ALOAD, METHOD_SCOPE_ARG); 
                }

                mv.visitMethodInsn(INVOKEVIRTUAL, p(MethodHandle.class), "bindTo", sig(MethodHandle.class, Object.class));
                mv.visitVarInsn(ALOAD, RECEIVER);     // [mh, 2, [null], [null], 0, recv]
                mv.visitMethodInsn(INVOKEVIRTUAL, p(MethodHandle.class), "bindTo", sig(MethodHandle.class, Object.class));
                mv.visitMethodInsn(INVOKEINTERFACE, p(IPersistentCollection.class), "cons", sig(IPersistentCollection.class, Object.class));
            }
        } else {
            for(ArgumentEntry ae : currentArguments) {
                // printThisClass = true;
                mv.visitFieldInsn(GETSTATIC, className, ae.handleName, c(MethodHandle.class));  // [mh]

                if(activateWith) {
                    mv.visitVarInsn(ALOAD, METHOD_SCOPE + plusArity); 
                } else {
                    mv.visitVarInsn(ALOAD, METHOD_SCOPE_ARG); 
                }

                mv.visitMethodInsn(INVOKEVIRTUAL, p(MethodHandle.class), "bindTo", sig(MethodHandle.class, Object.class));
                mv.visitVarInsn(ALOAD, RECEIVER);     // [mh, 2, [null], [null], 0, recv]
                mv.visitMethodInsn(INVOKEVIRTUAL, p(MethodHandle.class), "bindTo", sig(MethodHandle.class, Object.class));
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

    private void pumpTailCall(MethodVisitor mv) {
        Label done = new Label();
        Label loop = new Label();
        mv.visitLabel(loop);
        mv.visitInsn(DUP);
        if (SThread.TAIL_MARKER == null) {
            mv.visitJumpInsn(IFNONNULL, done);
        } else {
            mv.visitFieldInsn(GETSTATIC, p(SThread.class), "TAIL_MARKER", c(SephObject.class));
            mv.visitJumpInsn(IF_ACMPNE, done);
        }
        mv.visitInsn(POP);
        mv.visitVarInsn(ALOAD, THREAD);
        mv.visitInsn(DUP);
        mv.visitFieldInsn(GETFIELD, p(SThread.class), "tail", c(MethodHandle.class));
        mv.visitInsn(SWAP);
        mv.visitInsn(ACONST_NULL);
        mv.visitFieldInsn(PUTFIELD, p(SThread.class), "tail", c(MethodHandle.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(MethodHandle.class), "invokeExact", sig(SephObject.class));
        mv.visitJumpInsn(GOTO, loop);
        mv.visitLabel(done);
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

    private void compileMessageSend(MethodVisitor mv, Message current, boolean activateWith, int plusArity, boolean first, Message last) {
        mv.visitVarInsn(ALOAD, THREAD);

        if(activateWith) {
            mv.visitVarInsn(ALOAD, METHOD_SCOPE + plusArity); 
        } else {
            mv.visitVarInsn(ALOAD, METHOD_SCOPE_ARG); 
        }
            
        final int arity = compileArguments(mv, current.arguments(), activateWith, plusArity);
        if(first) {
            if(current == last) {
                mv.visitInvokeDynamicInsn(encode(current.name()), sigFor(arity), noReceiverTailCallSephBootstrap, EMPTY);
                if(!activateWith) {
                    Label noPump = new Label();
                    mv.visitVarInsn(ILOAD, SHOULD_EVALUATE_FULLY);
                    mv.visitInsn(ICONST_0);
                    mv.visitJumpInsn(IF_ICMPEQ, noPump);
                    pumpTailCall(mv);
                    mv.visitLabel(noPump);
                }
            } else {
                mv.visitInvokeDynamicInsn(encode(current.name()), sigFor(arity), noReceiverSephBootstrap, EMPTY);
                pumpTailCall(mv);
            }
        } else if(current == last) {
            mv.visitInvokeDynamicInsn(encode(current.name()), sigFor(arity), tailCallSephBootstrap, EMPTY);
            if(!activateWith) {
                Label noPump = new Label();
                mv.visitVarInsn(ILOAD, SHOULD_EVALUATE_FULLY);
                mv.visitInsn(ICONST_0);
                mv.visitJumpInsn(IF_ICMPEQ, noPump);
                pumpTailCall(mv);
                mv.visitLabel(noPump);
            }
        } else {
            mv.visitInvokeDynamicInsn(encode(current.name()), sigFor(arity), basicSephBootstrap, EMPTY);
            pumpTailCall(mv);
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

    private void activateWithBody(MethodVisitor mv, int plusArity) {
        boolean first = true;

        Message current = code;
        Message last = findLast(current);
        mv.visitVarInsn(ALOAD, RECEIVER);

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(mv, current);
                first = false;
            } else if(current instanceof Terminator) {
                compileTerminator(mv, current);
                first = true;
            } else if(current instanceof Abstraction) {
                first = false;
                throw new CompilationAborted("No support for compiling abstractions in abstractions");
            } else if(current instanceof Assignment) {
                first = false;
                throw new CompilationAborted("No support for compiling assignment");
            } else {
                compileMessageSend(mv, current, true, plusArity, first, last);
                first = false;
            }
            current = current.next();
        }

        mv.visitInsn(ARETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    // Should only be called for up to five arguments
    private void activateWithMethodRealArity(final int arity) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sigFor(arity), null, null);
        mv.visitCode();
        
        mv.visitFieldInsn(GETSTATIC, className, "capture", c(LexicalScope.class));
        mv.visitVarInsn(ALOAD, RECEIVER);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(LexicalScope.class), "newScopeWith", sig(LexicalScope.class, SephObject.class));
        mv.visitVarInsn(ASTORE, METHOD_SCOPE - 1 + arity);

        for(int i = 0; i < arity; i++) {
            String name = argNames.get(i);
            mv.visitVarInsn(ALOAD, METHOD_SCOPE - 1 + arity);
            mv.visitLdcInsn(name);
            mv.visitVarInsn(ALOAD, ARGUMENTS + i);
            mv.visitVarInsn(ALOAD, SCOPE);
            mv.visitVarInsn(ALOAD, THREAD);
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESTATIC, p(ControlDefaultBehavior.class), "evaluateArgument", sig(SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class));
            mv.visitMethodInsn(INVOKEVIRTUAL, p(LexicalScope.class), "directlyAssign", sig(void.class, String.class, SephObject.class));
        }

        activateWithBody(mv, arity - 1);
    }


    private void activateWithMethodCollectedArgs() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null);
        mv.visitCode();

        mv.visitFieldInsn(GETSTATIC, className, "capture", c(LexicalScope.class));
        mv.visitVarInsn(ALOAD, RECEIVER);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(LexicalScope.class), "newScopeWith", sig(LexicalScope.class, SephObject.class));
        mv.visitVarInsn(ASTORE, METHOD_SCOPE);

        mv.visitVarInsn(ALOAD, ARGUMENTS);                                             // [args]
        mv.visitMethodInsn(INVOKEINTERFACE, p(Seqable.class), "seq", sig(ISeq.class));   // [argsSeq]

        for(String arg : argNames) {
            mv.visitInsn(DUP);           // [argsSeq, argsSeq]
            mv.visitVarInsn(ALOAD, METHOD_SCOPE); // [argsSeq, argsSeq, mscope]
            mv.visitInsn(SWAP);           // [argsSeq, mscope, argsSeq]
            mv.visitMethodInsn(INVOKEINTERFACE, p(ISeq.class), "first", sig(Object.class));   // [argsSeq, mscope, arg1]
            mv.visitVarInsn(ALOAD, SCOPE);                                                  // [argsSeq, mscope, arg1, scope]
            mv.visitVarInsn(ALOAD, THREAD);                                                 // [argsSeq, mscope, arg1, scope, thread]
            mv.visitInsn(ICONST_1);                                                         // [argsSeq, mscope, arg1, scope, thread, true]
            mv.visitMethodInsn(INVOKESTATIC, p(ControlDefaultBehavior.class), "evaluateArgument", sig(SephObject.class, Object.class, LexicalScope.class, SThread.class, boolean.class)); // [argsSeq, mscope, arg]
            mv.visitLdcInsn(arg); // [argsSeq, mscope, arg, name]
            mv.visitInsn(SWAP);   // [argsSeq, mscope, name, arg]
            mv.visitMethodInsn(INVOKEVIRTUAL, p(LexicalScope.class), "directlyAssign", sig(void.class, String.class, SephObject.class));   // [argsSeq]
            mv.visitMethodInsn(INVOKEINTERFACE, p(ISeq.class), "next", sig(ISeq.class));   // [argsSeqNext]
        }

        mv.visitInsn(POP);

        activateWithBody(mv, 0);
    }

    private void activateWithMethodPassArgs(final int arity) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class), null, null);
        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, RECEIVER);
        mv.visitVarInsn(ALOAD, THREAD);
        mv.visitVarInsn(ALOAD, SCOPE);

        if(arity > 0) {
            mv.visitVarInsn(ALOAD, ARGUMENTS);
            mv.visitMethodInsn(INVOKEINTERFACE, p(Seqable.class), "seq", sig(ISeq.class));

            for(int i = 0; i < arity; i++) {
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKEINTERFACE, p(ISeq.class), "first", sig(Object.class));
                mv.visitMethodInsn(INVOKESTATIC, p(SephMethodObject.class), "ensureMH", sig(MethodHandle.class, Object.class));
                mv.visitInsn(SWAP);
                mv.visitMethodInsn(INVOKEINTERFACE, p(ISeq.class), "next", sig(ISeq.class));
            }

            mv.visitInsn(POP);
        }

        mv.visitMethodInsn(INVOKEVIRTUAL, className, "activateWith", sigFor(arity));

        mv.visitInsn(ARETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
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
