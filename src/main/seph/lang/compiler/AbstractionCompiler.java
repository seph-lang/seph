/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import seph.lang.*;
import seph.lang.ast.*;
import seph.lang.persistent.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static seph.lang.compiler.CompilationHelpers.*;

import java.dyn.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    private static AtomicInteger compiledCount = new AtomicInteger(0);

    private static void abstractionFields(ClassWriter cw, Map<Message, LiteralEntry> literals) {
        cw.visitField(ACC_FINAL, "fullMsg", c(Message.class), null, null);
        cw.visitField(ACC_FINAL, "capture", c(LexicalScope.class), null, null);
        for(Map.Entry<Message, LiteralEntry> me : literals.entrySet()) {
            cw.visitField(ACC_FINAL, me.getValue().name, c(SephObject.class), null, null);
        }
    }

    private static void constructor(ClassWriter cw, String className, Class<?> superClass, Map<Message, LiteralEntry> literals, LiteralEntry[] literalsInOrder) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", sig(void.class, getSignature(literalsInOrder)), null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);   // [recv]
        mv.visitInsn(DUP);           // [recv, recv]
        mv.visitFieldInsn(GETSTATIC, p(PersistentArrayMap.class), "EMPTY", c(PersistentArrayMap.class));   // [recv, recv, EMPTY]
        mv.visitFieldInsn(GETSTATIC, p(SimpleSephObject.class), "activatable", c(Symbol.class));           // [recv, recv, EMPTY, act]
        mv.visitFieldInsn(GETSTATIC, p(seph.lang.Runtime.class), "TRUE", c(SephObject.class));             // [recv, recv, EMPTY, act, true]
        mv.visitMethodInsn(INVOKEVIRTUAL, p(PersistentArrayMap.class), "associate", sig(IPersistentMap.class, Object.class, Object.class)); // [recv, recv, map]
        mv.visitMethodInsn(INVOKESPECIAL, p(superClass), "<init>", sig(void.class, IPersistentMap.class));                                  // [recv]
        mv.visitInsn(DUP);             // [recv, recv]
        mv.visitVarInsn(ALOAD, 1);     // [recv, code]
        mv.visitFieldInsn(PUTFIELD, p(className), "fullMsg", c(Message.class));    // []

        for(LiteralEntry le : literalsInOrder) {
            mv.visitVarInsn(ALOAD, 0);                  // [recv]
            mv.visitVarInsn(ALOAD, 3 + le.position);    // [recv, literal1]
            mv.visitFieldInsn(PUTFIELD, p(className), le.name, c(SephObject.class)); // []
        }

        mv.visitVarInsn(ALOAD, 0);   // [recv]
        mv.visitVarInsn(ALOAD, 2);   // [recv, scope]
        mv.visitFieldInsn(PUTFIELD, p(className), "capture", c(LexicalScope.class));     // []
        mv.visitInsn(RETURN);        // []
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private static void compileLiteral(MethodVisitor mv, String className, Map<Message, LiteralEntry> literals, Message current) {
        mv.visitInsn(POP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, literals.get(current).name, c(SephObject.class));
    }

    private static void compileTerminator(MethodVisitor mv, Message current) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            mv.visitInsn(POP);
            mv.visitVarInsn(ALOAD, 3);
        }
    }

    private static void compileMessageSend(MethodVisitor mv, Message current, String className) {
        if(current.arguments().seq() == null) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(SWAP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "capture", c(LexicalScope.class));
            mv.visitInsn(SWAP);
            compileInvocation(mv, current);
        } else {
            throw new CompilationAborted("No support for method calls with arguments");
        }
    }

    private static void activateWithMethod(ClassWriter cw, String className, Message code, Map<Message, LiteralEntry> literals) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class), null, null);
        mv.visitCode();

        Message current = code;

        mv.visitVarInsn(ALOAD, 3);

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(mv, className, literals, current);
            } else if(current instanceof Terminator) {
                compileTerminator(mv, current);
            } else {
                compileMessageSend(mv, current, className);
            }
            current = current.next();
        }

        mv.visitInsn(ARETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    private final static Object[] EMPTY = new Object[0];

    private final static org.objectweb.asm.MethodHandle basicSephBootstrap = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "basicSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);

    private static void compileInvocation(MethodVisitor mv, Message code) {
        mv.visitInvokeDynamicInsn(code.name(), sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class), basicSephBootstrap, EMPTY);
    }

    private static Class<?> abstractionClass(String className, Message code, Map<Message, LiteralEntry> literals, LiteralEntry[] literalsInOrder) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC, p(className), null, p(SimpleSephObject.class), new String[0]);

        abstractionFields(cw, literals);
        constructor(cw, className, SimpleSephObject.class, literals, literalsInOrder);
        activateWithMethod(cw, className, code, literals);

        cw.visitEnd();

        return seph.lang.Runtime.LOADER.defineClass(className, cw.toByteArray());
    }

    private static Class[] getSignature(LiteralEntry[] literalsInOrder) {
        Class[] params = new Class[literalsInOrder.length + 2];
        Arrays.fill(params, SephObject.class);
        params[0] = Message.class;
        params[1] = LexicalScope.class;
        return params;
    }

    private static Object[] getArguments(Message code, LexicalScope capture, LiteralEntry[] literalsInOrder) {
        Object[] args = new Object[literalsInOrder.length + 2];
        args[0] = code;
        args[1] = capture;
        for(int i = 0; i < literalsInOrder.length; i++) {
            args[i+2] = literalsInOrder[i].code.literal();
        }
        return args;
    }

    private static SephObject instantiateAbstraction(Class<?> c, Message code, LexicalScope capture, LiteralEntry[] literalsInOrder) {
        try {
            return (SephObject)c.getConstructor(getSignature(literalsInOrder)).newInstance(getArguments(code, capture, literalsInOrder));
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
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

    private static Map<Message, LiteralEntry> collectLiterals(Message code) {
        Map<Message, LiteralEntry> literals = new HashMap<>();
        int literalCount = 0;
        Message current = code;
        while(current != null) {
            if(current.isLiteral()) {
                literals.put(current, new LiteralEntry("literal" + literalCount, current, literalCount));
                literalCount++;
            }
            current = current.next();
        }

        return literals;
    }

    public static SephObject compile(Message code, LexicalScope capture) {
        Map<Message, LiteralEntry> literals = collectLiterals(code);
        LiteralEntry[] literalsInOrder = new LiteralEntry[literals.size()];
        for(Map.Entry<Message, LiteralEntry> me : literals.entrySet()) {
            literalsInOrder[me.getValue().position] = me.getValue();
        }


        Class<?> c = abstractionClass("seph$gen$abstraction$" + compiledCount.getAndIncrement(), code, literals, literalsInOrder);
        return instantiateAbstraction(c, code, capture, literalsInOrder);
    }

    public static SephObject compile(ISeq argumentsAndCode, LexicalScope capture) {
        if(RT.next(argumentsAndCode) == null) { // Only compile methods that take no arguments for now
            return compile((Message)RT.first(argumentsAndCode), capture);
        } else {
            throw new CompilationAborted("No support for compiling abstractions with arguments");
        }
    }
}// AbstractionCompiler
