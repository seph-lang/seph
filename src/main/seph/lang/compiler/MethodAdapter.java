/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;

import static seph.lang.compiler.CompilationHelpers.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MethodAdapter {
    private final static boolean TRACE = false;
    private final MethodVisitor mv;
    private int currentLine = -2;


    private static interface NextLoadOperation {
        void execute();
    }

    private NextLoadOperation next = null;

    public MethodAdapter(MethodVisitor mv) {
        this.mv = mv;
        this.mv.visitCode();

        if(TRACE) System.err.println("\n--------------------------- " + mv);
    }

    public void line(int line) {
        if(line != currentLine) {
            Label l = new Label();
            mv.visitLineNumber(line, l);
            currentLine = line;

            // op();
            // mv.visitLdcInsn(" LINE: " + line);
            // mv.visitInsn(POP);
        }
    }

    public void print(final String message) {
        op();
        printMessage(message, mv);
    }
    
    public void load(final String constant) {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("LDC(str) " + constant);
                    mv.visitLdcInsn(constant);
                }
            };
    }

    public void load(final int constant) {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("LDC(int) " + constant);
                    mv.visitLdcInsn(constant);
                }
            };
    }

    public void loadThis() {
        loadLocal(0);
    }

    public void loadLocal(final int index) {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("ALOAD " + index);
                    mv.visitVarInsn(ALOAD, index);   
                }
            };
    }

    public void newArray(Class c) {
        op();
        if(TRACE) System.err.println("ANEWARRAY " + c);
        this.mv.visitTypeInsn(ANEWARRAY, p(c));
    }

    public void storeArray() {
        op();
        if(TRACE) System.err.println("AASTORE");
        this.mv.visitInsn(AASTORE);
    }

    public void loadArray() {
        op();
        if(TRACE) System.err.println("AALOAD");
        this.mv.visitInsn(AALOAD);
    }

    public void storeLocal(int index) {
        op();
        if(TRACE) System.err.println("ASTORE " + index);
        mv.visitVarInsn(ASTORE, index);
    }

    public void loadLocalInt(final int index) {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("ILOAD " + index);
                    mv.visitVarInsn(ILOAD, index);   
                }
            };
    }

    public void nul() {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("ACONST_NULL");
                    mv.visitInsn(ACONST_NULL);
                }
            };
    }

    public void zero() {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("ICONST_0");
                    mv.visitInsn(ICONST_0);
                }
            };
    }

    public void one() {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("ICONST_1");
                    mv.visitInsn(ICONST_1);
                }
            };
    }
    
    public void dup() {
        op();
        next = new NextLoadOperation() {
                public void execute() {
                    if(TRACE) System.err.println("DUP");
                    mv.visitInsn(DUP);
                }
            };
    }

    public void dup_x1() {
        op();
        if(TRACE) System.err.println("DUP_X1");
        mv.visitInsn(DUP_X1);
    }

    private void op() {
        if(next != null) {
            next.execute();
            next = null;
        }
    }

    public void pop() {
        if(next != null) {
            next = null;
        } else {
            if(TRACE) System.err.println("POP");
            mv.visitInsn(POP);
        }
    }

    public void swap() {
        op();
        if(TRACE) System.err.println("SWAP");
        mv.visitInsn(SWAP);
    }

    public void ret() {
        op();
        if(TRACE) System.err.println("RETURN");
        mv.visitInsn(RETURN);
    }

    public void retValue() {
        op();
        if(TRACE) System.err.println("ARETURN");
        mv.visitInsn(ARETURN);
    }

    public void end() {
        op();
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    public void ifNotEqual(Label l) {
        op();
        if(TRACE) System.err.println("IF_ICMPNE " + l);
        mv.visitJumpInsn(IF_ICMPNE, l);
    }

    public void ifEqual(Label l) {
        op();
        if(TRACE) System.err.println("IF_ICMPEQ " + l);
        mv.visitJumpInsn(IF_ICMPEQ, l);
    }

    public void ifRefNotEqual(Label l) {
        op();
        if(TRACE) System.err.println("IF_ACMPNE " + l);
        mv.visitJumpInsn(IF_ACMPNE, l);
    }

    public void jump(Label l) {
        op();
        if(TRACE) System.err.println("GOTO " + l);
        mv.visitJumpInsn(GOTO, l);
    }

    public void ifNonNull(Label l) {
        op();
        if(TRACE) System.err.println("IFNONNULL " + l);
        mv.visitJumpInsn(IFNONNULL, l);
    }

    public void label(Label l) {
        op();
        if(TRACE) System.err.println("LABEL " + l);
        mv.visitLabel(l);
    }

    public void create(String name) {
        op();
        if(TRACE) System.err.println("NEW " + name);
        mv.visitTypeInsn(NEW, name);
    }

    public void getStatic(Class<?> from, String name, Class<?> type) {
        getStatic(p(from), name, c(type));
    }

    public void getStatic(Class<?> from, String name, String type) {
        getStatic(p(from), name, type);
    }

    public void getStatic(String from, String name, Class<?> type) {
        getStatic(from, name, c(type));
    }

    public void getStatic(String from, String name, String type) {
        op();
        if(TRACE) System.err.println("GETSTATIC " + from + " " + name + " " + type);
        mv.visitFieldInsn(GETSTATIC, from, name, type);
    }



    public void putField(Class<?> fromClass, String name, Class<?> type) {
        putField(p(fromClass), name, c(type));
    }

    public void putField(String fromClass, String name, Class<?> type) {
        putField(fromClass, name, c(type));
    }

    public void putField(String fromClass, String name, String type) {
        op();
        if(TRACE) System.err.println("PUTFIELD " + fromClass + " " + name + " " + type);
        mv.visitFieldInsn(PUTFIELD, fromClass, name, type);
    }



    public void getField(Class<?> fromClass, String name, Class<?> type) {
        getField(p(fromClass), name, c(type));
    }

    public void getField(String fromClass, String name, Class<?> type) {
        getField(fromClass, name, c(type));
    }

    public void getField(String fromClass, String name, String type) {
        op();
        if(TRACE) System.err.println("GETFIELD " + fromClass + " " + name + " " + type);
        mv.visitFieldInsn(GETFIELD, fromClass, name, type);
    }


    public void cast(Class c) {
        op();
        if(TRACE) System.err.println("CHECKCAST " + c);
        mv.visitTypeInsn(CHECKCAST, p(c));        
    }
    
    public void staticCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        staticCall(p(on), name, ret, params);
    }

    public void staticCall(Class<?> on, String name, String sig) {
        staticCall(p(on), name, sig);
    }

    public void staticCall(String on, String name, Class<?> ret, Class<?>... params) {
        staticCall(on, name, sig(ret, params));
    }

    public void staticCall(String on, String name, String sig) {
        op();
        if(TRACE) System.err.println("INVOKESTATIC " + on + " " + name + " " + sig);
        mv.visitMethodInsn(INVOKESTATIC, on, name, sig);
    }


    public void virtualCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        virtualCall(p(on), name, ret, params);
    }

    public void virtualCall(Class<?> on, String name, String sig) {
        virtualCall(p(on), name, sig);
    }

    public void virtualCall(String on, String name, Class<?> ret, Class<?>... params) {
        virtualCall(on, name, sig(ret, params));
    }

    public void virtualCall(String on, String name, String sig) {
        op();
        if(TRACE) System.err.println("INVOKEVIRTUAL " + on + " " + name + " " + sig);
        mv.visitMethodInsn(INVOKEVIRTUAL, on, name, sig);
    }

    
    public void interfaceCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        interfaceCall(p(on), name, ret, params);
    }

    public void interfaceCall(String on, String name, Class<?> ret, Class<?>... params) {
        op();
        if(TRACE) System.err.println("INVOKEINTERFACE " + on + " " + name + " " + sig(ret, params));
        mv.visitMethodInsn(INVOKEINTERFACE, on, name, sig(ret, params));
    }

    public void interfaceCall(Class on, String name, String s) {
        interfaceCall(p(on), name, s);
    }

    public void interfaceCall(String on, String name, String s) {
        op();
        if(TRACE) System.err.println("INVOKEINTERFACE " + on + " " + name + " " + s);
        mv.visitMethodInsn(INVOKEINTERFACE, on, name, s);
    }

    private final static Object[] EMPTY = new Object[0];
    public void dynamicCall(String name, String sig, org.objectweb.asm.MethodHandle bootstrap) {
        op();
        if(TRACE) System.err.println("INVOKEDYNAMIC " + name + " " + sig + " " + bootstrap);
        mv.visitInvokeDynamicInsn(name, sig, bootstrap, EMPTY);
    }

    public void init(Class<?> on, Class<?> ret, Class<?>... params) {
        init(p(on), ret, params);
    }

    public void init(String on, Class<?> ret, Class<?>... params) {
        op();
        if(TRACE) System.err.println("INVOKESPECIAL " + on + " " + sig(ret, params));
        mv.visitMethodInsn(INVOKESPECIAL, on, "<init>", sig(ret, params));
    }
}
