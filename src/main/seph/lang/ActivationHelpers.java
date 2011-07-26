/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.*;

import static seph.lang.compiler.SephCallSite.*;
import static seph.lang.Types.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class ActivationHelpers {
    private ActivationHelpers() {}

    public static SephObject noActivateForMH(SephObject self) {
        throw new RuntimeException(" *** couldn't activate: " + self);
    }

    public static MethodHandle arityErrorMH(int expected, int got, String name) {
        throw new RuntimeException(name + " - expected " + expected + " arguments, got " + got + " arguments");
    }

    public final static MethodHandle NO_ACTIVATE_FOR    = findStatic(ActivationHelpers.class, "noActivateForMH", methodType(SephObject.class, SephObject.class));
    public final static MethodHandle ARITY_ERROR        = findStatic(ActivationHelpers.class, "arityErrorMH", methodType(MethodHandle.class, int.class, int.class, String.class));

    public final static Class[] ARGUMENT_CLASSES_N_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_0_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_1_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_2_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_3_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_4_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_5_K = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};

    public final static Class[] ARGUMENT_CLASSES_N   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class};
    public final static Class[] ARGUMENT_CLASSES_0   = new Class[]{SephObject.class, SThread.class, LexicalScope.class};
    public final static Class[] ARGUMENT_CLASSES_1   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class};
    public final static Class[] ARGUMENT_CLASSES_2   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class};
    public final static Class[] ARGUMENT_CLASSES_3   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
    public final static Class[] ARGUMENT_CLASSES_4   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
    public final static Class[] ARGUMENT_CLASSES_5   = new Class[]{SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};

    public static Class[] argumentClassesFor(int arity, boolean keywords) {
        switch(arity) {
        case 0:
            return keywords ? ARGUMENT_CLASSES_0_K : ARGUMENT_CLASSES_0;
        case 1:
            return keywords ? ARGUMENT_CLASSES_1_K : ARGUMENT_CLASSES_1;
        case 2:
            return keywords ? ARGUMENT_CLASSES_2_K : ARGUMENT_CLASSES_2;
        case 3:
            return keywords ? ARGUMENT_CLASSES_3_K : ARGUMENT_CLASSES_3;
        case 4:
            return keywords ? ARGUMENT_CLASSES_4_K : ARGUMENT_CLASSES_4;
        case 5:
            return keywords ? ARGUMENT_CLASSES_5_K : ARGUMENT_CLASSES_5;
        default:
            return keywords ? ARGUMENT_CLASSES_N_K : ARGUMENT_CLASSES_N;
        }
    }

    public static MethodType methodTypeFor(int arity, boolean keywords) {
        switch(arity) {
        case 0:
            return keywords ? ACTIVATE_METHOD_TYPE_0_K : ACTIVATE_METHOD_TYPE_0;
        case 1:
            return keywords ? ACTIVATE_METHOD_TYPE_1_K : ACTIVATE_METHOD_TYPE_1;
        case 2:
            return keywords ? ACTIVATE_METHOD_TYPE_2_K : ACTIVATE_METHOD_TYPE_2;
        case 3:
            return keywords ? ACTIVATE_METHOD_TYPE_3_K : ACTIVATE_METHOD_TYPE_3;
        case 4:
            return keywords ? ACTIVATE_METHOD_TYPE_4_K : ACTIVATE_METHOD_TYPE_4;
        case 5:
            return keywords ? ACTIVATE_METHOD_TYPE_5_K : ACTIVATE_METHOD_TYPE_5;
        default:
            return keywords ? ACTIVATE_METHOD_TYPE_N_K : ACTIVATE_METHOD_TYPE_N;
        }
    }
    
    public static MethodHandle noActivateFor(SephObject self, int arity, boolean keywords) {
        return MethodHandles.dropArguments(NO_ACTIVATE_FOR.bindTo(self), 0, argumentClassesFor(arity, keywords));
    }

    public static MethodHandle arityError(int expected, int got, String name, boolean keywords) {
        return MethodHandles.dropArguments(MethodHandles.insertArguments(ARITY_ERROR, 0, expected, got, name), 0, argumentClassesFor(got, keywords));
    }

    public static SephObject invokeExact(MethodHandle mh, SThread thread, LexicalScope scope) {
        try {
            return (SephObject)mh.invokeExact(thread, scope, true, true);
        } catch(Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}// ActivationHelpers
