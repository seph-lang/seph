/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.io.PrintStream;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class CompilationHelpers {
    public static String p(String name) {
        return name.replaceAll("\\.", "/");
    }

    public static String p(Class<?> type) {
        return type.getName().replaceAll("\\.", "/");
    }

    public static String c(Class<?> type) {
        if(type == void.class) {
            return "V";
        }
        if(type.isPrimitive()) {
            throw new RuntimeException("Can't handle " + type + " in c yet. Please update me");
        }

        return "L" + p(type) + ";";
    }

    public static String sig(Class<?> ret, Class<?>... args) {
        StringBuilder result = new StringBuilder("(");
        for(Class<?> clz : args) {
            result.append(c(clz));
        }
        result.append(")");
        result.append(c(ret));
        return result.toString();
    }

    public static void printMessage(String outp, MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC, p(System.class), "out", c(PrintStream.class));
        mv.visitLdcInsn(outp);
        mv.visitMethodInsn(INVOKEVIRTUAL, p(PrintStream.class), "println", sig(void.class, String.class));
    }
}// CompilationHelpers
