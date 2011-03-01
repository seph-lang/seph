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

    private List<LiteralEntry> literals = new LinkedList<>();

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
            return (SephObject)abstractionClass.newInstance();
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
            throw new CompilationAborted("An error was encountered during compilation");
        }
    }

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

    private void compileLiteral(Message current) {
        int position = literals.size();
        LiteralEntry le = new LiteralEntry("literal" + position, current, position);
        literals.add(le);
        
        cw.visitField(ACC_PRIVATE + ACC_STATIC, le.name, c(SephObject.class), null, null);

        mv_act.visitInsn(POP);
        mv_act.visitFieldInsn(GETSTATIC, className, le.name, c(SephObject.class));
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
            mv_act.visitFieldInsn(GETSTATIC, className, "capture", c(LexicalScope.class));
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
}// AbstractionCompiler
