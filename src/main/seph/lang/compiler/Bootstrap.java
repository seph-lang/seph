/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.SephObject;
import seph.lang.SThread;
import seph.lang.LexicalScope;

import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

import java.dyn.CallSite;
import java.dyn.ConstantCallSite;
import java.dyn.MethodHandle;
import java.dyn.MethodHandles;
import java.dyn.MethodType;

import static seph.lang.compiler.CompilationHelpers.*;

public class Bootstrap {
    public final static MethodType BOOTSTRAP_SIGNATURE      = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
    public final static String     BOOTSTRAP_SIGNATURE_DESC = sig(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);

    private static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        site.setTarget(fallback);
        return site;
    }

    public final static MethodType NO_ARGS_SIGNATURE = MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);

    private static MethodHandle dropAllArgumentTypes(MethodHandle mh, MethodType type) {
        if(type.equals(NO_ARGS_SIGNATURE)) {
            return mh;
        }
        Class<?>[] params = type.parameterArray();
        Class<?>[] newParams = new Class<?>[params.length - 3];
        System.arraycopy(params, 2, newParams, 0, params.length - 3);
        return MethodHandles.dropArguments(mh, 2, newParams);
    }

    private static CallSite bootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(findStatic(Bootstrap.class, "intrinsic_true", NO_ARGS_SIGNATURE), type);
        MethodType initialSetupType = NO_ARGS_SIGNATURE.insertParameterTypes(0, SephCallSite.class, MethodHandle.class, MethodHandle.class);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(findStatic(Bootstrap.class, "initialSetup_intrinsic_true", initialSetupType), 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    private static CallSite bootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(findStatic(Bootstrap.class, "intrinsic_false", NO_ARGS_SIGNATURE), type);
        MethodType initialSetupType = NO_ARGS_SIGNATURE.insertParameterTypes(0, SephCallSite.class, MethodHandle.class, MethodHandle.class);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(findStatic(Bootstrap.class, "initialSetup_intrinsic_false", initialSetupType), 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    private static CallSite bootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(findStatic(Bootstrap.class, "intrinsic_nil", NO_ARGS_SIGNATURE), type);
        MethodType initialSetupType = NO_ARGS_SIGNATURE.insertParameterTypes(0, SephCallSite.class, MethodHandle.class, MethodHandle.class);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(findStatic(Bootstrap.class, "initialSetup_intrinsic_nil", initialSetupType), 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    public static CallSite basicSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "fallback");
    }

    public static CallSite noReceiverSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "noReceiverFallback");
    }

    public static CallSite tailCallSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "tailCallFallback");
    }

    public static CallSite noReceiverTailCallSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "noReceiverTailCallFallback");
    }


    public static CallSite basicSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "fallback");
    }

    public static CallSite noReceiverSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "noReceiverFallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "tailCallFallback");
    }

    public static CallSite noReceiverTailCallSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "noReceiverTailCallFallback");
    }


    public static CallSite basicSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "fallback");
    }

    public static CallSite noReceiverSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "noReceiverFallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "tailCallFallback");
    }

    public static CallSite noReceiverTailCallSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "noReceiverTailCallFallback");
    }


    public static CallSite basicSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "fallback");
    }

    public static CallSite noReceiverSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "noReceiverFallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "tailCallFallback");
    }

    public static CallSite noReceiverTailCallSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "noReceiverTailCallFallback");
    }



    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, -1);
            return value.activateWith(receiver, thread, scope, args);
        } else {
            site.installConstantEntry(receiver, null, value, -1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 0);
            return value.activateWith(receiver, thread, scope);
        } else {
            site.installConstantEntry(receiver, null, value, 0);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 1);
            return value.activateWith(receiver, thread, scope, arg0);
        } else {
            site.installConstantEntry(receiver, null, value, 1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 2);
            return value.activateWith(receiver, thread, scope, arg0, arg1);
        } else {
            site.installConstantEntry(receiver, null, value, 2);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 3);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
        } else {
            site.installConstantEntry(receiver, null, value, 3);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 4);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
        } else {
            site.installConstantEntry(receiver, null, value, 4);
        }

        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, value, 5);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        } else {
            site.installConstantEntry(receiver, null, value, 5);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, -1);
            return value.activateWith(receiver, thread, scope, args);
        } else {
            site.installConstantEntry(receiver, scope, value, -1);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 0);
            return value.activateWith(receiver, thread, scope);
        } else {
            site.installConstantEntry(receiver, scope, value, 0);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 1);
            return value.activateWith(receiver, thread, scope, arg0);
        } else {
            site.installConstantEntry(receiver, scope, value, 1);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 2);
            return value.activateWith(receiver, thread, scope, arg0, arg1);
        } else {
            site.installConstantEntry(receiver, scope, value, 2);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 3);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
        } else {
            site.installConstantEntry(receiver, scope, value, 3);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 4);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
        } else {
            site.installConstantEntry(receiver, scope, value, 4);
        }
        return value;
    }

    public static SephObject noReceiverFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, scope, value, 5);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        } else {
            site.installConstantEntry(receiver, scope, value, 5);
        }
        return value;
    }

    public final static MethodHandle ACTIVATE_WITH_ARGS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class));
    public final static MethodHandle ACTIVATE_WITH_ARG0 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class));
    public final static MethodHandle ACTIVATE_WITH_ARG1 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG2 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG3 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG4 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG5 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    
    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS.bindTo(value);
            site.installActivatableEntry(receiver, null, h, -1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, -1);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG0.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 0);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 0);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG1.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 1);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG2.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 2);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 2);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG3.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 3);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 3);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG4.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 4);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 4);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG5.bindTo(value);
            site.installActivatableEntry(receiver, null, h, 5);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, null, value, 5);
        }
        return value;
    }


    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, -1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, -1);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG0.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 0);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 0);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG1.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 1);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG2.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 2);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 2);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG3.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 3);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 3);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG4.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 4);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 4);
        }
        return value;
    }

    public static SephObject noReceiverTailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = scope.get(name);
        if(null == value) {
            value = receiver.get(name);
        }
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG5.bindTo(value);
            site.installActivatableEntry(receiver, scope, h, 5);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, scope, value, 5);
        }
        return value;
    }

    public static SephObject replaceCompletelyImpl(MethodHandle mh, SephCallSite site, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        site.setTarget(mh);
        return (SephObject)mh.invokeExact(receiver, thread, scope);
    }

    public static MethodHandle replaceCompletely(MethodHandle mh, SephCallSite site) {
        return MethodHandles.insertArguments(findStatic(Bootstrap.class, "replaceCompletelyImpl", 
                                                        MethodType.methodType(SephObject.class, MethodHandle.class, SephCallSite.class, SephObject.class, SThread.class, LexicalScope.class)), 
                                             0, mh, site);
    }

    public static SephObject initialSetup_intrinsic_true(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_TRUE_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invokeExact(receiver, thread, scope);
    }

    public static SephObject intrinsic_true(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.TRUE;
    }

    public static SephObject initialSetup_intrinsic_false(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_FALSE_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invokeExact(receiver, thread, scope);
    }

    public static SephObject intrinsic_false(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.FALSE;
    }


    public static SephObject initialSetup_intrinsic_nil(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_NIL_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invokeExact(receiver, thread, scope);
    }

    public static SephObject intrinsic_nil(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.NIL;
    }

    public static MethodHandle findStatic(Class target, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findStatic(target, name, type);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class target, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findVirtual(target, name, type);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
