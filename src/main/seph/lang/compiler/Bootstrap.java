/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.SephObject;
import seph.lang.SThread;
import seph.lang.LexicalScope;

import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static seph.lang.compiler.CompilationHelpers.*;

public class Bootstrap {
    public final static MethodType BOOTSTRAP_SIGNATURE      = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
    public final static String     BOOTSTRAP_SIGNATURE_DESC = sig(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);

    private static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        if(name.startsWith("seph:var:") || name.startsWith("seph:tailVar:")) {
            String[] pieces = name.split(":");
            int depth = Integer.parseInt(pieces[2]);
            int index = Integer.parseInt(pieces[3]);
            MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, int.class, int.class);
            MethodHandle fallback;
            if(pieces[1].equals("var")) {
                fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, "fallbackVar", fallbackType), 0, site, depth, index);
            } else {
                fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, "fallbackTailVar", fallbackType), 0, site, depth, index);
            }
            site.setTarget(fallback);
            return site;
        } else {
            MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
            MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
            site.setTarget(fallback);
            return site;
        }
    }

    public final static MethodType NO_ARGS_SIGNATURE = MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType INITIAL_SETUP_TYPE = MethodType.methodType(SephObject.class, SephCallSite.class, MethodHandle.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class);
    public final static MethodType ARGS_3_SIGNATURE = MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);
    public final static MethodType INITIAL_SETUP_3_TYPE = MethodType.methodType(SephObject.class, SephCallSite.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class);

    private static MethodHandle dropAllArgumentTypes(MethodHandle mh, MethodType type) {
        if(type.equals(NO_ARGS_SIGNATURE)) {
            return mh;
        }
        Class<?>[] params = type.parameterArray();
        Class<?>[] newParams = new Class<?>[params.length - 3];
        System.arraycopy(params, 2, newParams, 0, params.length - 3);
        return MethodHandles.dropArguments(mh, 2, newParams);
    }

    public final static MethodHandle INTRINSIC_TRUE_MH = findStatic(Bootstrap.class, "intrinsic_true", NO_ARGS_SIGNATURE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_TRUE_MH = findStatic(Bootstrap.class, "initialSetup_intrinsic_true", INITIAL_SETUP_TYPE);

    public final static MethodHandle INTRINSIC_FALSE_MH = findStatic(Bootstrap.class, "intrinsic_false", NO_ARGS_SIGNATURE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_FALSE_MH = findStatic(Bootstrap.class, "initialSetup_intrinsic_false", INITIAL_SETUP_TYPE);

    public final static MethodHandle INTRINSIC_NIL_MH = findStatic(Bootstrap.class, "intrinsic_nil", NO_ARGS_SIGNATURE);
    public final static MethodHandle INITIAL_SETUP_INTRINSIC_NIL_MH = findStatic(Bootstrap.class, "initialSetup_intrinsic_nil", INITIAL_SETUP_TYPE);

    public final static MethodHandle INITIAL_SETUP_INTRINSIC_IF_MH = findStatic(Bootstrap.class, "initialSetup_intrinsic_if", INITIAL_SETUP_3_TYPE);

    private static CallSite bootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(INTRINSIC_TRUE_MH, type);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(INITIAL_SETUP_INTRINSIC_TRUE_MH, 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    private static CallSite bootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(INTRINSIC_FALSE_MH, type);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(INITIAL_SETUP_INTRINSIC_FALSE_MH, 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    private static CallSite bootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle intrinsicMH = dropAllArgumentTypes(INTRINSIC_NIL_MH, type);
        MethodHandle initialSetup = dropAllArgumentTypes(MethodHandles.insertArguments(INITIAL_SETUP_INTRINSIC_NIL_MH, 0, site, intrinsicMH, fallback), type);
        site.setTarget(initialSetup);
        return site;
    }

    private static CallSite bootstrap_intrinsic_if(MethodHandles.Lookup lookup, String name, MethodType type, String bootstrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, bootstrapType, fallbackType), 0, site, decode(name));
        MethodHandle initialSetup = MethodHandles.insertArguments(INITIAL_SETUP_INTRINSIC_IF_MH, 0, site, fallback);
        site.setTarget(initialSetup);
        return site;
    }

    public static CallSite sephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "fallback");
    }

    public static CallSite basicSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "fallback");
    }

    public static CallSite tailCallSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap(lookup, name, type, "tailCallFallback");
    }


    public static CallSite basicSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "fallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_true(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_true(lookup, name, type, "tailCallFallback");
    }

    public static CallSite basicSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "fallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_false(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_false(lookup, name, type, "tailCallFallback");
    }

    public static CallSite basicSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "fallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_nil(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_nil(lookup, name, type, "tailCallFallback");
    }


    public static CallSite basicSephBootstrap_intrinsic_if(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_if(lookup, name, type, "fallback");
    }

    public static CallSite tailCallSephBootstrap_intrinsic_if(MethodHandles.Lookup lookup, String name, MethodType type) {
        return bootstrap_intrinsic_if(lookup, name, type, "tailCallFallback");
    }

    public final static MethodHandle ACTIVATE_WITH_ARGS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class));
    public final static MethodHandle ACTIVATE_WITH_ARG0 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class));
    public final static MethodHandle ACTIVATE_WITH_ARG1 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG2 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG3 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG4 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    public final static MethodHandle ACTIVATE_WITH_ARG5 = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));


    public final static MethodHandle ACTIVATE_WITH_ARGS_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG0_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG1_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG2_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG3_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG4_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    public final static MethodHandle ACTIVATE_WITH_ARG5_KEYWORDS = findVirtual(SephObject.class, "activateWith", MethodType.methodType(SephObject.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));


    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, -1);
            return value.activateWith(receiver, thread, scope, args);
        } else {
            site.installConstantEntry(receiver, value, -1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 0);
            return value.activateWith(receiver, thread, scope);
        } else {
            site.installConstantEntry(receiver, value, 0);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 1);
            return value.activateWith(receiver, thread, scope, arg0);
        } else {
            site.installConstantEntry(receiver, value, 1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 2);
            return value.activateWith(receiver, thread, scope, arg0, arg1);
        } else {
            site.installConstantEntry(receiver, value, 2);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 3);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
        } else {
            site.installConstantEntry(receiver, value, 3);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 4);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
        } else {
            site.installConstantEntry(receiver, value, 4);
        }

        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, value, 5);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        } else {
            site.installConstantEntry(receiver, value, 5);
        }
        return value;
    }

    
    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS.bindTo(value);
            site.installActivatableEntry(receiver, h, -1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, -1);
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
            site.installActivatableEntry(receiver, h, 0);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 0);
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
            site.installActivatableEntry(receiver, h, 1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 1);
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
            site.installActivatableEntry(receiver, h, 2);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 2);
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
            site.installActivatableEntry(receiver, h, 3);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 3);
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
            site.installActivatableEntry(receiver, h, 4);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 4);
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
            site.installActivatableEntry(receiver, h, 5);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntry(receiver, value, 5);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, -1);
            return value.activateWith(receiver, thread, scope, args, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, -1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 0);
            return value.activateWith(receiver, thread, scope, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 0);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 1);
            return value.activateWith(receiver, thread, scope, arg0, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 1);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 2);
            return value.activateWith(receiver, thread, scope, arg0, arg1, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 2);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 3);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 3);
        }
        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 4);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 4);
        }

        return value;
    }

    public static SephObject fallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntryWithKeywords(receiver, value, 5);
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywords);
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 5);
        }
        return value;
    }


    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, -1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, -1);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG0_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 0);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 0);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG1_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 1);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 1);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG2_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 2);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 2);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG3_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 3);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 3);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG4_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 4);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 4);
        }
        return value;
    }

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG5_KEYWORDS.bindTo(value);
            site.installActivatableEntryWithKeywords(receiver, h, 5);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        } else {
            site.installConstantEntryWithKeywords(receiver, value, 5);
        }
        return value;
    }



















    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, args);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
        }

        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        }
        return value;
    }

    
    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG0.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG1.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG2.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG3.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG4.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG5.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, args, keywordNames, keywords);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, keywordNames, keywords);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, keywordNames, keywords);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, keywordNames, keywords);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywords);
        }
        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywords);
        }

        return value;
    }

    public static SephObject fallbackVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywords);
        }
        return value;
    }


    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARGS_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG0_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG1_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG2_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG3_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG4_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }
        return value;
    }

    public static SephObject fallbackTailVar(SephCallSite site, int depth, int index, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywords) {
        SephObject value = scope.get(depth, index);
        if(value.isActivatable()) {
            MethodHandle h = ACTIVATE_WITH_ARG5_KEYWORDS.bindTo(value);
            h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywords);
            thread.tail = h;
            return SThread.TAIL_MARKER;
        }

        return value;
    }
















    public static SephObject replaceCompletelyImpl(MethodHandle mh, SephCallSite site, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        site.setTarget(mh);
        return (SephObject)mh.invoke(receiver, thread, scope);
    }

    public static SephObject replaceCompletely3Impl(MethodHandle mh, SephCallSite site, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) throws Throwable {
        site.setTarget(mh);
        return (SephObject)mh.invoke(receiver, thread, scope, arg0, arg1, arg2);
    }

    public static MethodHandle replaceCompletely(MethodHandle mh, SephCallSite site) {
        return MethodHandles.insertArguments(findStatic(Bootstrap.class, "replaceCompletelyImpl", 
                                                        MethodType.methodType(SephObject.class, MethodHandle.class, SephCallSite.class, SephObject.class, SThread.class, LexicalScope.class)), 
                                             0, mh, site);
    }

    public static MethodHandle replaceCompletely3(MethodHandle mh, SephCallSite site) {
        return MethodHandles.insertArguments(findStatic(Bootstrap.class, "replaceCompletely3Impl", 
                                                        MethodType.methodType(SephObject.class, MethodHandle.class, SephCallSite.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class)), 
                                             0, mh, site);
    }

    public static SephObject initialSetup_intrinsic_true(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_TRUE_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject intrinsic_true(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.TRUE;
    }

    public static SephObject initialSetup_intrinsic_false(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_FALSE_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject intrinsic_false(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.FALSE;
    }


    public static SephObject initialSetup_intrinsic_nil(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_NIL_SP.guardWithTest(fast, replaceCompletely(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject intrinsic_nil(SephObject receiver, SThread thread, LexicalScope scope) {
        return seph.lang.Runtime.NIL;
    }

    public static SephObject intrinsic_if(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle test, MethodHandle then, MethodHandle _else) throws Throwable {
        if(((SephObject)test.invoke(thread, scope, true, true)).isTrue()) {
            return (SephObject)then.invoke(thread, scope, true, false);
        } else {
            return (SephObject)_else.invoke(thread, scope, true, false);
        }
    }

    public final static MethodHandle INTRINSIC_IF_MH = findStatic(Bootstrap.class, "intrinsic_if", ARGS_3_SIGNATURE);

    public static SephObject initialSetup_intrinsic_if(SephCallSite site, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle test, MethodHandle then, MethodHandle _else) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_IF_SP.guardWithTest(INTRINSIC_IF_MH, replaceCompletely3(slow, site));
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope, test, then, _else);
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
