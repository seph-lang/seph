/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.*;

import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.compiler.SephCallSite.*;
import seph.lang.compiler.SephCallSite;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class Types {
    private Types() {}
    public final static MethodType ACTIVATE_METHOD_TYPE_N    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);

    public final static MethodType ACTIVATE_METHOD_TYPE_N_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle[].class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    

    public final static MethodType BOOTSTRAP_SIGNATURE      = methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Object[].class);
    public final static String     BOOTSTRAP_SIGNATURE_DESC =        sig(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Object[].class);

    public final static MethodType NO_ARGS_SIGNATURE        = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType INITIAL_SETUP_TYPE       = methodType(SephObject.class, SephCallSite.class, MethodHandle.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType ARGS_3_SIGNATURE         = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType INITIAL_SETUP_3_TYPE     = methodType(SephObject.class, SephCallSite.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, 
                                                                                                                                                                    MethodHandle.class, MethodHandle.class, MethodHandle.class);

    public final static MethodHandle INITIAL_SETUP_INTRINSIC_TRUE_MH  = findStatic(SephCallSite.class, "initialSetup_intrinsic_true", INITIAL_SETUP_TYPE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_FALSE_MH = findStatic(SephCallSite.class, "initialSetup_intrinsic_false", INITIAL_SETUP_TYPE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_NIL_MH   = findStatic(SephCallSite.class, "initialSetup_intrinsic_nil", INITIAL_SETUP_TYPE);

    public final static MethodType   SCOPE_GETTER_TYPE = methodType(SephObject.class, LexicalScope.class);
    public final static MethodType   SCOPE_GETTER_M_TYPE = methodType(SephObject.class, LexicalScope.class);
    public final static MethodType   SCOPE_ARRAY_GETTER_TYPE = methodType(SephObject[].class, LexicalScope.class);

    public final static MethodHandle SCOPE_0_GETTER_M = findVirtual(LexicalScope.One.class, "getValueOne", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_1_GETTER_M = findVirtual(LexicalScope.Two.class, "getValueTwo", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_2_GETTER_M = findVirtual(LexicalScope.Three.class, "getValueThree", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_3_GETTER_M = findVirtual(LexicalScope.Four.class, "getValueFour", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_4_GETTER_M = findVirtual(LexicalScope.Five.class, "getValueFive", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_5_GETTER_M = findVirtual(LexicalScope.Six.class, "getValueSix", methodType(SephObject.class)).asType(SCOPE_GETTER_M_TYPE);
    public final static MethodHandle SCOPE_N_GETTER_M = findVirtual(LexicalScope.Many.class, "getValueMany", methodType(SephObject.class, int.class)).asType(methodType(SephObject.class, LexicalScope.class, int.class));

    public final static MethodHandle PARENT_SCOPE_METHOD = findVirtual(LexicalScope.class, "getParent", methodType(LexicalScope.class));

    public final static MethodHandle BIND_TO             = findVirtual(MethodHandle.class, "bindTo", methodType(MethodHandle.class, Object.class));

    public final static MethodHandle THREAD_TAIL_GETTER  = findVirtual(SThread.class, "getTail", methodType(MethodHandle.class));
}// Types
