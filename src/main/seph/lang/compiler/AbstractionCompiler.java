/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
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
    private final static Object[] EMPTY = new Object[0];
    private final static org.objectweb.asm.MethodHandle basicSephBootstrap = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "basicSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static AtomicInteger compiledCount = new AtomicInteger(0);

    private final Message code;
    private final LexicalScope capture;

    private List<LiteralEntry>  literals = new LinkedList<>();
    private List<ArgumentEntry> arguments = new LinkedList<>();

    private Class<?> abstractionClass;

    private final ClassWriter cw;
    private final String className;

    private int messageIndex = 0;

    private boolean printThisClass = false;

    private AbstractionCompiler(Message code, LexicalScope capture) {
        this.code = code;
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

        if(printThisClass) {
            new ClassReader(classBytes).accept(new org.objectweb.asm.util.TraceClassVisitor(new java.io.PrintWriter(System.err)), 0);
            //            throw new CompilationAborted("No support for method calls with arguments");
        }

        abstractionClass = seph.lang.Runtime.LOADER.defineClass(className, classBytes);
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

    private final static MethodType ARGUMENT_METHOD_TYPE = MethodType.methodType(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, boolean.class);
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

    public static SephObject compile(Message code, LexicalScope capture) {
        AbstractionCompiler c = new AbstractionCompiler(code, capture);
        c.generateAbstractionClass();
        c.setStaticValues();
        return c.instantiateAbstraction();
    }

    public static SephObject compile(ISeq argumentsAndCode, LexicalScope capture) {
        if(RT.next(argumentsAndCode) == null) { // Only compile methods that take no arguments for now
            return compile((Message)RT.first(argumentsAndCode), capture);
        } else {
            throw new CompilationAborted("No support for compiling abstractions with arguments");
        }
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

    private void compileTerminator(MethodVisitor mv, Message current, int index) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            mv.visitInsn(POP);
            mv.visitVarInsn(ALOAD, RECEIVER + index);
        }
    }

    private void compileArgument(MethodVisitor act_mv, Message argument, int currentMessageIndex, int argIndex, int stackIndex) {
        //               printThisClass = true;
        final String codeName   = "code_arg_" + currentMessageIndex + "_" + argIndex;
        final String handleName = "handle_arg_" + currentMessageIndex + "_" + argIndex;
        final String methodName = "argument_" + currentMessageIndex + "_" + argIndex;
        arguments.add(new ArgumentEntry(codeName, handleName, methodName, argument));
        

        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, codeName,   c(SephObject.class), null, null);
        cw.visitField(ACC_PRIVATE + ACC_STATIC, handleName, c(MethodHandle.class), null, null);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, methodName, sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, boolean.class), null, null);
        mv.visitCode();

        mv.visitVarInsn(ILOAD, SHOULD_EVALUATE - 1);
        mv.visitInsn(ICONST_0);
        Label els = new Label();
        mv.visitJumpInsn(IF_ICMPNE, els);

        mv.visitFieldInsn(GETSTATIC, className, codeName, c(SephObject.class));
        mv.visitInsn(ARETURN);

        mv.visitLabel(els);


        Message current = argument;

        mv.visitVarInsn(ALOAD, RECEIVER - 1);

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(mv, current);
            } else if(current instanceof Terminator) {
                compileTerminator(mv, current, -1);
            } else if(current instanceof Abstraction) {
                throw new CompilationAborted("No support for compiling abstractions in abstractions");
            } else if(current instanceof Assignment) {
                throw new CompilationAborted("No support for compiling assignment");
            } else {
                compileMessageSend(mv, current, -1);
            }
            current = current.next();
        }

        mv.visitInsn(ARETURN);

        mv.visitMaxs(0,0);
        mv.visitEnd();

        
        act_mv.visitFieldInsn(GETSTATIC, className, handleName, c(MethodHandle.class));  // [mh]
        act_mv.visitInsn(ICONST_2);   // [mh, 2]
        act_mv.visitInsn(ICONST_1);   // [mh, 2, 1]
        act_mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");   // [mh, 2, [null]]
        act_mv.visitInsn(DUP);                                 // [mh, 2, [null], [null]]
        act_mv.visitInsn(ICONST_0);                            // [mh, 2, [null], [null], 0]
        act_mv.visitVarInsn(ALOAD, RECEIVER + stackIndex);     // [mh, 2, [null], [null], 0, recv]
        act_mv.visitInsn(AASTORE);                             // [mh, 2, [recv]]
        act_mv.visitMethodInsn(INVOKESTATIC, p(MethodHandles.class), "insertArguments", sig(MethodHandle.class, MethodHandle.class, int.class, Object[].class));
    }

    private void compileArguments(MethodVisitor mv, IPersistentList arguments, int index) {
        int num = 0;
        mv.visitFieldInsn(GETSTATIC, p(PersistentList.class), "EMPTY", "Lseph/lang/persistent/PersistentList$EmptyList;");
        final int currentMessageIndex = messageIndex;
        for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
            compileArgument(mv, (Message)seq.first(), currentMessageIndex, num++, index);
            mv.visitMethodInsn(INVOKEINTERFACE, p(IPersistentCollection.class), "cons", sig(IPersistentCollection.class, Object.class));
        }
            
        messageIndex++;
    }

    private final static int THREAD          = 1;
    private final static int SCOPE           = 2;
    private final static int RECEIVER        = 3;
    private final static int ARGUMENTS       = 4;
    private final static int SHOULD_EVALUATE = 4;

    private void compileMessageSend(MethodVisitor mv, Message current, int index) {
        mv.visitVarInsn(ALOAD, THREAD + index);
        mv.visitInsn(SWAP);
        mv.visitFieldInsn(GETSTATIC, className, "capture", c(LexicalScope.class));
        mv.visitInsn(SWAP);
            
        compileArguments(mv, current.arguments(), index);
        mv.visitInvokeDynamicInsn(current.name(), sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class), basicSephBootstrap, EMPTY);
    }

    private void activateWithMethod() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class), null, null);
        mv.visitCode();

        Message current = code;

        mv.visitVarInsn(ALOAD, RECEIVER);

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(mv, current);
            } else if(current instanceof Terminator) {
                compileTerminator(mv, current, 0);
            } else if(current instanceof Abstraction) {
                throw new CompilationAborted("No support for compiling abstractions in abstractions");
            } else if(current instanceof Assignment) {
                throw new CompilationAborted("No support for compiling assignment");
            } else {
                compileMessageSend(mv, current, 0);
            }
            current = current.next();
        }

        mv.visitInsn(ARETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }
}// AbstractionCompiler
