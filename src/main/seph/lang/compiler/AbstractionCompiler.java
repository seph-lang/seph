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
    private final static Object[] EMPTY = new Object[0];
    private final static org.objectweb.asm.MethodHandle basicSephBootstrap = new org.objectweb.asm.MethodHandle(MH_INVOKESTATIC, "seph/lang/compiler/Bootstrap", "basicSephBootstrap", Bootstrap.BOOTSTRAP_SIGNATURE_DESC);
    private final static AtomicInteger compiledCount = new AtomicInteger(0);

    private final Message code;
    private final LexicalScope capture;

    private LiteralEntry[] literals = new LiteralEntry[4];
    private int literalsFill = 0;

    private Class<?> abstractionClass;

    private final ClassWriter cw;
    private MethodVisitor mv_act;
    private final String className;

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

        abstractionClass = seph.lang.Runtime.LOADER.defineClass(className, cw.toByteArray());
    }

    private SephObject instantiateAbstraction() {
        try {
            return (SephObject)abstractionClass.getConstructor(getSignature()).newInstance(getArguments());
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
        }
    }

    private void abstractionFields() {
        cw.visitField(ACC_FINAL, "fullMsg", c(Message.class), null, null);
        cw.visitField(ACC_FINAL, "capture", c(LexicalScope.class), null, null);
    }

    private void constructor(Class<?> superClass) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", sig(void.class, getSignature()), null, null);
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

        for(int i = 0; i<literalsFill; i++) {
            LiteralEntry le = literals[i];
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

    private void compileLiteral(Message current) {
        int position = literalsFill;
        LiteralEntry le = new LiteralEntry("literal" + position, current, position);
        if(position >= literals.length) {
            LiteralEntry[] newLiterals = new LiteralEntry[literals.length * 2];
            System.arraycopy(literals, 0, newLiterals, 0, literals.length);
            literals = newLiterals;
        }
        literals[position] = le;
        literalsFill++;

        cw.visitField(ACC_FINAL, le.name, c(SephObject.class), null, null);

        mv_act.visitInsn(POP);
        mv_act.visitVarInsn(ALOAD, 0);
        mv_act.visitFieldInsn(GETFIELD, className, le.name, c(SephObject.class));
    }

    private void compileTerminator(Message current) {
        if(current.next() != null && !(current.next() instanceof Terminator)) {
            mv_act.visitInsn(POP);
            mv_act.visitVarInsn(ALOAD, 3);
        }
    }

    private void compileMessageSend(Message current) {
        if(current.arguments().seq() == null) {
            mv_act.visitVarInsn(ALOAD, 1);
            mv_act.visitInsn(SWAP);
            mv_act.visitVarInsn(ALOAD, 0);
            mv_act.visitFieldInsn(GETFIELD, className, "capture", c(LexicalScope.class));
            mv_act.visitInsn(SWAP);
            compileInvocation(current);
        } else {
            throw new CompilationAborted("No support for method calls with arguments");
        }
    }

    private void activateWithMethod() {
        mv_act = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class), null, null);
        mv_act.visitCode();

        Message current = code;

        mv_act.visitVarInsn(ALOAD, 3);

        while(current != null) {
            if(current.isLiteral()) {
                compileLiteral(current);
            } else if(current instanceof Terminator) {
                compileTerminator(current);
            } else {
                compileMessageSend(current);
            }
            current = current.next();
        }

        mv_act.visitInsn(ARETURN);
        mv_act.visitMaxs(0,0);
        mv_act.visitEnd();
    }

    private void compileInvocation(Message code) {
        mv_act.visitInvokeDynamicInsn(code.name(), sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class), basicSephBootstrap, EMPTY);
    }

    private Class[] getSignature() {
        Class[] params = new Class[literals.length + 2];
        Arrays.fill(params, SephObject.class);
        params[0] = Message.class;
        params[1] = LexicalScope.class;
        return params;
    }

    private Object[] getArguments() {
        Object[] args = new Object[literals.length + 2];
        args[0] = code;
        args[1] = capture;
        for(int i = 0; i < literalsFill; i++) {
            args[i+2] = literals[i].code.literal();
        }
        return args;
    }

    public static SephObject compile(Message code, LexicalScope capture) {
        AbstractionCompiler c = new AbstractionCompiler(code, capture);
        c.generateAbstractionClass();
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
}// AbstractionCompiler
