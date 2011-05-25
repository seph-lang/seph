/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.*;

import seph.lang.persistent.IPersistentList;

import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.compiler.SephCallSite.*;
import seph.lang.compiler.SephCallSite;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class Types {
    private Types() {}
    public final static MethodType ACTIVATE_METHOD_TYPE_N    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5    = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);

    public final static MethodType ACTIVATE_METHOD_TYPE_N_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_0_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_1_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_2_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_3_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_4_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    public final static MethodType ACTIVATE_METHOD_TYPE_5_K  = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class);
    

    public final static MethodType BOOTSTRAP_SIGNATURE      = methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
    public final static String     BOOTSTRAP_SIGNATURE_DESC =        sig(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);

    public final static MethodType NO_ARGS_SIGNATURE        = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType INITIAL_SETUP_TYPE       = methodType(SephObject.class, SephCallSite.class, MethodHandle.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType ARGS_3_SIGNATURE         = methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType INITIAL_SETUP_3_TYPE     = methodType(SephObject.class, SephCallSite.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, 
                                                                                                                                                                    MethodHandle.class, MethodHandle.class, MethodHandle.class);

    public final static MethodHandle INITIAL_SETUP_INTRINSIC_TRUE_MH  = findStatic(SephCallSite.class, "initialSetup_intrinsic_true", INITIAL_SETUP_TYPE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_FALSE_MH = findStatic(SephCallSite.class, "initialSetup_intrinsic_false", INITIAL_SETUP_TYPE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_NIL_MH   = findStatic(SephCallSite.class, "initialSetup_intrinsic_nil", INITIAL_SETUP_TYPE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_IF_MH    = findStatic(SephCallSite.class, "initialSetup_intrinsic_if", INITIAL_SETUP_3_TYPE);

    public final static MethodType   SCOPE_GETTER_TYPE = methodType(SephObject.class, LexicalScope.class);
    public final static MethodType   SCOPE_ARRAY_GETTER_TYPE = methodType(SephObject[].class, LexicalScope.class);

    public final static MethodHandle SCOPE_0_GETTER = findField(LexicalScope.One.class, "value0", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_1_GETTER = findField(LexicalScope.Two.class, "value1", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_2_GETTER = findField(LexicalScope.Three.class, "value2", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_3_GETTER = findField(LexicalScope.Four.class, "value3", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_4_GETTER = findField(LexicalScope.Five.class, "value4", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_5_GETTER = findField(LexicalScope.Six.class, "value5", SephObject.class).asType(SCOPE_GETTER_TYPE);
    public final static MethodHandle SCOPE_VALUES_GETTER = findField(LexicalScope.Many.class, "values", SephObject[].class).asType(SCOPE_ARRAY_GETTER_TYPE);
    public final static MethodHandle SCOPE_N_GETTER = findArrayGetter(SephObject[].class);

    public final static MethodHandle PARENT_SCOPE_GETTER = findField(LexicalScope.class, "parent", LexicalScope.class);
}// Types
