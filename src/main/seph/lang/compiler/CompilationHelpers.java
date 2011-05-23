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
        if(type.isArray()) {
            type = type.getComponentType();
            if(type.isPrimitive()) {
                if(type == Byte.TYPE) {
                    return "[B";
                } else if (type == Boolean.TYPE) {
                    return "[Z";
                } else if (type == Short.TYPE) {
                    return "[S";
                } else if (type == Character.TYPE) {
                    return "[C";
                } else if (type == Integer.TYPE) {
                    return "[I";
                } else if (type == Float.TYPE) {
                    return "[F";
                } else if (type == Double.TYPE) {
                    return "[D";
                } else if (type == Long.TYPE) {
                    return "[J";
                } else {
                    throw new RuntimeException("Unrecognized type in compiler: " + type.getName());
                }
            } else {
                return "[" + c(type);
            }
        }
        if(type == void.class) {
            return "V";
        } else if(type == boolean.class) {
            return "Z";
        } else if(type == int.class) {
            return "I";
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

    public static String sigCombine(Class<?> ret, Class<?>[] args, Class<?>[] args2) {
        StringBuilder result = new StringBuilder("(");
        for(Class<?> clz : args) {
            result.append(c(clz));
        }
        for(Class<?> clz : args2) {
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



    public static String encode(String name) {
        return StringNames.toBytecodeName(name);
    }

    public static String decode(String name) {
        return StringNames.toSourceName(name);
    }
}// CompilationHelpers
