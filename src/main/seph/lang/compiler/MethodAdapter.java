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
    private final MethodVisitor mv;

    public MethodAdapter(MethodVisitor mv) {
        this.mv = mv;
        this.mv.visitCode();
    }
    
    public void load(String constant) {
        mv.visitLdcInsn(constant);
    }

    public void loadThis() {
        loadLocal(0);
    }

    public void loadLocal(int index) {
        mv.visitVarInsn(ALOAD, index);   
    }

    public void storeLocal(int index) {
        mv.visitVarInsn(ASTORE, index);
    }

    public void loadLocalInt(int index) {
        mv.visitVarInsn(ILOAD, index);   
    }

    public void nul() {
        mv.visitInsn(ACONST_NULL);
    }

    public void zero() {
        mv.visitInsn(ICONST_0);
    }

    public void one() {
        mv.visitInsn(ICONST_1);
    }
    
    public void dup() {
        mv.visitInsn(DUP);
    }

    public void pop() {
        mv.visitInsn(POP);
    }

    public void swap() {
        mv.visitInsn(SWAP);
    }

    public void ret() {
        mv.visitInsn(RETURN);
    }

    public void retValue() {
        mv.visitInsn(ARETURN);
    }

    public void end() {
        mv.visitMaxs(0,0);
        mv.visitEnd();
    }

    public void ifNotEqual(Label l) {
        mv.visitJumpInsn(IF_ICMPNE, l);
    }

    public void ifEqual(Label l) {
        mv.visitJumpInsn(IF_ICMPEQ, l);
    }

    public void ifRefNotEqual(Label l) {
        mv.visitJumpInsn(IF_ACMPNE, l);
    }

    public void jump(Label l) {
        mv.visitJumpInsn(GOTO, l);
    }

    public void ifNonNull(Label l) {
        mv.visitJumpInsn(IFNONNULL, l);
    }

    public void label(Label l) {
        mv.visitLabel(l);
    }

    public void create(String name) {
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
        mv.visitFieldInsn(GETSTATIC, from, name, type);
    }



    public void putField(Class<?> fromClass, String name, Class<?> type) {
        putField(p(fromClass), name, c(type));
    }

    public void putField(String fromClass, String name, Class<?> type) {
        putField(fromClass, name, c(type));
    }

    public void putField(String fromClass, String name, String type) {
        mv.visitFieldInsn(PUTFIELD, fromClass, name, type);
    }



    public void getField(Class<?> fromClass, String name, Class<?> type) {
        getField(p(fromClass), name, c(type));
    }

    public void getField(String fromClass, String name, Class<?> type) {
        getField(fromClass, name, c(type));
    }

    public void getField(String fromClass, String name, String type) {
        mv.visitFieldInsn(GETFIELD, fromClass, name, type);
    }


    
    public void staticCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        staticCall(p(on), name, ret, params);
    }

    public void staticCall(String on, String name, Class<?> ret, Class<?>... params) {
        mv.visitMethodInsn(INVOKESTATIC, on, name, sig(ret, params));
    }


    public void virtualCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        virtualCall(p(on), name, ret, params);
    }

    public void virtualCall(String on, String name, Class<?> ret, Class<?>... params) {
        virtualCall(on, name, sig(ret, params));
    }

    public void virtualCall(String on, String name, String sig) {
        mv.visitMethodInsn(INVOKEVIRTUAL, on, name, sig);
    }

    
    public void interfaceCall(Class<?> on, String name, Class<?> ret, Class<?>... params) {
        interfaceCall(p(on), name, ret, params);
    }

    public void interfaceCall(String on, String name, Class<?> ret, Class<?>... params) {
        mv.visitMethodInsn(INVOKEINTERFACE, on, name, sig(ret, params));
    }

    private final static Object[] EMPTY = new Object[0];
    public void dynamicCall(String name, String sig, org.objectweb.asm.MethodHandle bootstrap) {
        mv.visitInvokeDynamicInsn(name, sig, bootstrap, EMPTY);
    }

    public void init(Class<?> on, Class<?> ret, Class<?>... params) {
        init(p(on), ret, params);
    }

    public void init(String on, Class<?> ret, Class<?>... params) {
        mv.visitMethodInsn(INVOKESPECIAL, on, "<init>", sig(ret, params));
    }
}
