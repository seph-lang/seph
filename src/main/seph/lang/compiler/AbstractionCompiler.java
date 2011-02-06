/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

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

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    private static class CompilationAborted extends RuntimeException {
    }

    private static AtomicInteger compiledCount = new AtomicInteger(0);

    private static void abstractionFields(ClassWriter cw) {
        cw.visitField(ACC_FINAL, "fullMsg", c(Message.class), null, null);
        cw.visitField(ACC_FINAL, "capture", c(LexicalScope.class), null, null);
        cw.visitField(ACC_FINAL, "literal1", c(SephObject.class), null, null);
    }

    private static void constructor(ClassWriter cw, String className, Class<?> superClass) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", sig(void.class, Message.class, LexicalScope.class), null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(DUP);
        mv.visitFieldInsn(GETSTATIC, p(PersistentArrayMap.class), "EMPTY", c(PersistentArrayMap.class));
        mv.visitFieldInsn(GETSTATIC, p(SimpleSephObject.class), "activatable", c(Symbol.class));
        mv.visitFieldInsn(GETSTATIC, p(seph.lang.Runtime.class), "TRUE", c(SephObject.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, p(PersistentArrayMap.class), "associate", sig(IPersistentMap.class, Object.class, Object.class));
        mv.visitMethodInsn(INVOKESPECIAL, p(superClass), "<init>", sig(void.class, IPersistentMap.class));
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, p(className), "fullMsg", c(Message.class));
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, p(Message.class), "literal", sig(SephObject.class));
        mv.visitFieldInsn(PUTFIELD, p(className), "literal1", c(SephObject.class));
        mv.visitVarInsn(ALOAD, 2);
        mv.visitFieldInsn(PUTFIELD, p(className), "capture", c(LexicalScope.class));
        mv.visitInsn(RETURN);
        mv.visitMaxs(3,2);
        mv.visitEnd();
    }

    private static void activateWithMethod(ClassWriter cw, String className, Message code) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "activateWith", sig(SephObject.class, SThread.class, LexicalScope.class, SephObject.class, IPersistentList.class), null, null);
        mv.visitCode();
        if(code.isLiteral() && code.next() == null) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "literal1", c(SephObject.class));
            mv.visitInsn(ARETURN);
        } else {
            throw new CompilationAborted();
        }

        mv.visitMaxs(3,2);
        mv.visitEnd();
    }


    private static Class<?> abstractionClass(String className, Message code) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC, p(className), null, p(SimpleSephObject.class), new String[0]);

        abstractionFields(cw);
        constructor(cw, className, SimpleSephObject.class);
        activateWithMethod(cw, className, code);

        cw.visitEnd();

        return seph.lang.Runtime.LOADER.defineClass(className, cw.toByteArray());
    }

    private static SephObject instantiateAbstraction(Class<?> c, Message code, LexicalScope capture) {
        try {
            return (SephObject)c.getConstructor(Message.class, LexicalScope.class).newInstance(code, capture);
        } catch(Exception e) {
            e.printStackTrace();
            throw new CompilationAborted();
        }
    }

    public static SephObject compile(Message code, LexicalScope capture) {
        try {
            Class<?> c = abstractionClass("seph$gen$abstraction$" + compiledCount.getAndIncrement(), 
                                       code);
            return instantiateAbstraction(c, code, capture);
        } catch(CompilationAborted e) {
            System.err.println("BAILING OUT ON COMPILE");
            return null;
        }
    }
}// AbstractionCompiler
