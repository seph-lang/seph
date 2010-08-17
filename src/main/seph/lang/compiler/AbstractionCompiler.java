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

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompiler {
    private static AtomicInteger compiledCount = new AtomicInteger(0);

    public static SephObject compile(Message code, LexicalScope capture) {
        int currentCompilation = compiledCount.getAndIncrement();
        String className = "seph$gen$abstraction$" + currentCompilation;
        Class<?> superClass = SimpleSephObject.class;
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC, p(className), null, p(superClass), new String[0]);
        cw.visitField(ACC_FINAL, "fullMsg", "Lseph/lang/ast/Message;", null, null);
        cw.visitField(ACC_FINAL, "capture", "Lseph/lang/LexicalScope;", null, null);
        cw.visitField(ACC_FINAL, "literal1", "Lseph/lang/SephObject;", null, null);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lseph/lang/ast/Message;Lseph/lang/LexicalScope;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(DUP);
        mv.visitFieldInsn(GETSTATIC, p(PersistentArrayMap.class), "EMPTY", "Lseph/lang/persistent/PersistentArrayMap;");
        mv.visitFieldInsn(GETSTATIC, p(SimpleSephObject.class), "activatable", "Lseph/lang/Symbol;");
        mv.visitFieldInsn(GETSTATIC, p(seph.lang.Runtime.class), "TRUE", "Lseph/lang/SephObject;");
        mv.visitMethodInsn(INVOKEVIRTUAL, p(PersistentArrayMap.class), "associate", "(Ljava/lang/Object;Ljava/lang/Object;)Lseph/lang/persistent/IPersistentMap;");
        mv.visitMethodInsn(INVOKESPECIAL, p(superClass), "<init>", "(Lseph/lang/persistent/IPersistentMap;)V");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, p(className), "fullMsg", "Lseph/lang/ast/Message;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, p(Message.class), "literal", "()Lseph/lang/SephObject;");
        mv.visitFieldInsn(PUTFIELD, p(className), "literal1", "Lseph/lang/SephObject;");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitFieldInsn(PUTFIELD, p(className), "capture", "Lseph/lang/LexicalScope;");
        mv.visitInsn(RETURN);
        mv.visitMaxs(3,2);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PUBLIC, "activateWith", "(Lseph/lang/SThread;Lseph/lang/LexicalScope;Lseph/lang/SephObject;Lseph/lang/persistent/IPersistentList;)Lseph/lang/SephObject;", null, null);
        mv.visitCode();
        if(code.isLiteral() && code.next() == null) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "literal1", "Lseph/lang/SephObject;");
            mv.visitInsn(ARETURN);
        } else {
            System.err.println("BAILING OUT ON COMPILE");
            return null;
        }

        mv.visitMaxs(3,2);
        mv.visitEnd();

        cw.visitEnd();

        byte[] b = cw.toByteArray();
        
        Class<?> c = seph.lang.Runtime.LOADER.defineClass(className, b);

        try {
            return (SephObject)c.getConstructor(Message.class, LexicalScope.class).newInstance(code, capture);
        } catch(Exception e) {
            System.err.println("BAILING OUT ON COMPILE: " + e);
            e.printStackTrace();
            return null;
        }
    }

    private static void printMessage(String outp, MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC, p(System.class), "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(outp);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(java.io.PrintStream.class), "println", "(Ljava/lang/String;)V");
    }

    private static String p(String name) {
        return name.replaceAll("\\.", "/");
    }

    private static String p(Class<?> type) {
        return type.getName().replaceAll("\\.", "/");
    }
}// AbstractionCompiler
