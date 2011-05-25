/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.util.*;

import seph.lang.*;
import seph.lang.persistent.IPersistentList;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodHandles.invoker;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodHandles.filterArguments;
import static java.lang.invoke.MethodHandles.foldArguments;
import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.identity;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.Types.*;

public class SephCallSite extends MutableCallSite {
    public static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        return new SephCallSite(lookup, name, type);
    }

    private final boolean intrinsic;
    private final String messageKind;
    private final String messageName;
    private final MethodHandle slowPath;

    public SephCallSite(MethodHandles.Lookup lookup, String name, MethodType type) {
        super(type);

        Object[] pieces = kindNameIntrinsic(name.split(":"));
        this.messageKind = (String)pieces[0];
        this.messageName = (String)pieces[1];
        this.intrinsic   = (Boolean)pieces[2];
        this.slowPath    = computeSlowPath();

        if(intrinsic) {
            setNeutral();
            setTarget(computeIntrinsicPath());
        } else {
            setNeutral();
        }
    }
    
    private final static MethodHandle INTRINSIC_TRUE_MH =  constant(SephObject.class, seph.lang.Runtime.TRUE);
    private final static MethodHandle INTRINSIC_FALSE_MH = constant(SephObject.class, seph.lang.Runtime.FALSE);
    private final static MethodHandle INTRINSIC_NIL_MH =   constant(SephObject.class, seph.lang.Runtime.NIL);

    private MethodHandle computeIntrinsicPath() {
        Class<?>[] argParts = type().dropParameterTypes(0, 3).parameterArray();
        MethodHandle fallback = getTarget();
        if(messageName == "true") {
            MethodHandle intrinsicMH =  dropArguments(INTRINSIC_TRUE_MH, 0, type().parameterArray());
            MethodHandle initialSetup = dropArguments(insertArguments(INITIAL_SETUP_INTRINSIC_TRUE_MH, 0, this, intrinsicMH, fallback), 2, argParts);
            return initialSetup;
        } else if(messageName == "false") {
            MethodHandle intrinsicMH =  dropArguments(INTRINSIC_FALSE_MH, 0, type().parameterArray());
            MethodHandle initialSetup = dropArguments(insertArguments(INITIAL_SETUP_INTRINSIC_FALSE_MH, 0, this, intrinsicMH, fallback), 2, argParts);
            return initialSetup;
        } else if(messageName == "nil") {
            MethodHandle intrinsicMH =  dropArguments(INTRINSIC_NIL_MH, 0, type().parameterArray());
            MethodHandle initialSetup = dropArguments(insertArguments(INITIAL_SETUP_INTRINSIC_NIL_MH, 0, this, intrinsicMH, fallback), 2, argParts);
            return initialSetup;
        } else if(messageName == "if") {
            MethodHandle initialSetup = insertArguments(INITIAL_SETUP_INTRINSIC_IF_MH, 0, this, fallback);
            return initialSetup;
        } else {
            return null;
        }
    }


    private int arity = -2;
    private boolean keywords = false;;
    
    private void computeArity() {
        int num = type().parameterCount() - 3;
        if(num == 0) {
            keywords = false;
            arity = 0;
        } else {
            Class<?>[] tp = type().parameterArray();
            int minus = 0;
            if(tp[tp.length-1] == MethodHandle[].class) {
                keywords = true;
                minus = 2;
            }
            if(num == 1 && tp[3] == IPersistentList.class) {
                arity = -1;
            } else {
                arity = num - minus;
            }
        }
    }

    private int arity() {
        if(arity == -2) {
            computeArity();
        }
        return arity;
    }

    private boolean keywords() {
        if(arity == -2) {
            computeArity();
        }
        return keywords;
    }

    private static Object[] kindNameIntrinsic(Object[] nc) {
        String kind = ((String)nc[1]).intern();
        String name = decode((String)nc[2]).intern();
        boolean intrinsic = nc.length == 4;
        return new Object[] { kind, name, intrinsic };
    }

    public static SephObject setTail(MethodHandle h, SThread thread) {
        thread.tail = h;
        return SThread.TAIL_MARKER;
    }
    
    public final static MethodHandle SET_TAIL_MH = findStatic(SephCallSite.class, "setTail", methodType(SephObject.class, MethodHandle.class, SThread.class));
    public final static MethodType SLOW_PATH_TYPE    = methodType(SephObject.class, SephCallSite.class, String.class, SephObject.class, SThread.class, LexicalScope.class, Object[].class);

    public static void checkForNull(String name, SephObject value, SephObject receiver) {
         if(null == value) {
             throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
         }
    }

    public final static MethodHandle CHECK_FOR_NULL_MH = findStatic(SephCallSite.class, "checkForNull", methodType(void.class, String.class, SephObject.class, SephObject.class));

    MethodHandle computeSlowPath() {
            if(messageKind == "message") {
                MethodHandle _test = dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, type().parameterArray());
                MethodHandle _then = filterArguments(invoker(type()), 0, insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords()));
                MethodHandle _else = dropArguments(identity(SephObject.class), 1, type().parameterArray());
                MethodHandle all = guardWithTest(_test, _then, _else);

                MethodHandle _nullCheck = dropArguments(insertArguments(CHECK_FOR_NULL_MH, 0, messageName), 
                                                        2, 
                                                        type().dropParameterTypes(0, 1).parameterArray());
                MethodHandle complete = foldArguments(
                                                      foldArguments(all, _nullCheck),
                                                      dropArguments(insertArguments(findVirtual(SephObject.class, "get", methodType(SephObject.class, String.class)), 1, messageName), 1, type().dropParameterTypes(0, 1).parameterArray()));

                return complete;
            } else if(messageKind == "tailMessage") {
                MethodHandle _test = dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, type().parameterArray());
                MethodHandle _insertArguments = insertArguments(findStatic(MethodHandles.class, "insertArguments", methodType(MethodHandle.class, MethodHandle.class, int.class, Object[].class)), 1, 0).asCollector(Object[].class, type().parameterCount()).asType(type().insertParameterTypes(0, MethodHandle.class).changeReturnType(MethodHandle.class));
                MethodHandle _activationFor = insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords());
                MethodHandle _filtered = filterArguments( _insertArguments, 0 ,_activationFor);
                MethodHandle _then = foldArguments(dropArguments(dropArguments(SET_TAIL_MH, 1, SephObject.class, SephObject.class), 4, type().dropParameterTypes(0, 2).parameterArray()), 
                                                   _filtered);
                MethodHandle _else = dropArguments(identity(SephObject.class), 1, type().parameterArray());
                MethodHandle all = guardWithTest(_test, _then, _else);

                MethodHandle _nullCheck = dropArguments(
                                                        insertArguments(CHECK_FOR_NULL_MH, 0, messageName), 
                                                        2, 
                                                        type().dropParameterTypes(0, 1).parameterArray());

                MethodHandle complete = foldArguments(
                                                      foldArguments(all, _nullCheck), 
                                                      dropArguments(insertArguments(findVirtual(SephObject.class, "get", methodType(SephObject.class, String.class)), 1, messageName), 1, type().dropParameterTypes(0, 1).parameterArray()));

                return complete;
            } else {
                return null;
            }
    }

    public void setNeutral() {
        specializedMisses = 0;
        setTarget(filterArguments(slowPath, 0, computeProfiler(false)));
    }

    public void setMegamorphic() {
        setTarget(slowPath);
        resetProfile();
    }

    TypeProfile typeProfile;
    int profileCount;
    int specializedMisses;

    public void resetProfile() {
        typeProfile = null;
        specializedMisses = 0;
    }

    public int incrementProfileCount() {
        int cc1 = profileCount;
        int cc2 = cc1 + 1;
        if(cc2 <= 0)
            return cc1;
        profileCount = cc2;
        return cc2;
    }

    protected TypeProfile makeTypeProfile(SephObject receiver) {
        return TypeProfile.forIdentity(receiver);
    }

    private static final MethodHandle MH_profileReceiver = findVirtual(SephCallSite.class, "profileReceiver", methodType(SephObject.class, SephObject.class));
    private static final MethodHandle MH_profileReceiverForMiss = findVirtual(SephCallSite.class, "profileReceiverForMiss", methodType(SephObject.class, SephObject.class));

    private static int MONO_TARGET_COUNT = 3;
    private static int POLY_TARGET_COUNT = 5;
    private static int MAX_SPECIALIZED_MISSES = MONO_TARGET_COUNT+2;
    private static int MAX_TOTAL_PROFILES = POLY_TARGET_COUNT*2;

    protected void doPolicy(boolean sawMiss) {
        TypeProfile prof = typeProfile;
        if(prof == null) return;
        int pc = incrementProfileCount();
        if(pc >= MAX_TOTAL_PROFILES) {
            setMegamorphic();
            return;
        }
        if(sawMiss) {
            if(++specializedMisses < MAX_SPECIALIZED_MISSES)
                return;
            specializedMisses >>= 1;
        } else {
            int targetCount = MONO_TARGET_COUNT;
            if(prof.isMonomorphic())
                targetCount = POLY_TARGET_COUNT;
            if(prof.count() <= targetCount)
                return;
        }
        setSpecialized(prof);
    }

    public void setSpecialized(TypeProfile allProfiles) {
        MethodType type = this.type();
        MethodHandle elsePath = slowPath;
        elsePath = filterArguments(elsePath, 0, computeProfiler(true));
        TypeProfile[] cases = allProfiles.cases();
        int casesToCode = cases.length;
        for(int i = casesToCode - 1; i >= 0; i--) {
            MethodHandle[] guardAndFastPath = computeGuardAndFastPath(cases[i]);
            if(guardAndFastPath == null) {
                continue;
            }
            MethodHandle guard = guardAndFastPath[0]; 
            MethodHandle fastPath = guardAndFastPath[1];

            elsePath = guardWithTest(guard, fastPath, elsePath);
        }
        setTarget(elsePath);
    }

    private Class[] dropClasses() {
        return type().dropParameterTypes(2, type().parameterCount() - 3).parameterArray();
    }

    private final static MethodHandle INSERT_ARGUMENTS_MH = findStatic(MethodHandles.class, "insertArguments", methodType(MethodHandle.class, MethodHandle.class, int.class, Object[].class));

    protected MethodHandle[] computeGuardAndFastPath(TypeProfile prof) {
        if(prof instanceof TypeProfile.ForIdentity) {
            MethodHandle fastPath = null;

            Class[] argumentsToDrop = type().dropParameterTypes(0, 1).parameterArray();
            MethodHandle guard = dropArguments(EQ.bindTo(((TypeProfile.ForIdentity)prof).matchIdentity()), 1, argumentsToDrop);
            
            SephObject receiver = ((TypeProfile.ForIdentity)prof).matchValue();
            SephObject value = receiver.get(messageName);

            if(value.isActivatable()) {
                MethodHandle activation = value.activationFor(arity(), keywords());
                if(messageKind == "message") {
                    fastPath = activation;
                } else {
                    MethodHandle one = insertArguments(INSERT_ARGUMENTS_MH, 0, activation, 0);
                    MethodHandle two = one.asCollector(Object[].class, type().parameterCount());
                    MethodHandle three = two.asType(type().changeReturnType(MethodHandle.class));
                    MethodHandle four = dropArguments(dropArguments(SET_TAIL_MH, 1, SephObject.class), 3, type().dropParameterTypes(0, 2).parameterArray());
                    MethodHandle folded = foldArguments(four, three);
                    fastPath = folded;
                }
            } else {
                fastPath = dropArguments(constant(SephObject.class, value), 0, type().parameterArray());
            }

            return new MethodHandle[] {guard, fastPath};
        }
        
        return null;
    }

    protected SephObject profileReceiver(SephObject receiver) {
        return profileReceiver(receiver, false);
    }
    
    protected SephObject profileReceiverForMiss(SephObject receiver) {
        return profileReceiver(receiver, true);
    }

    protected SephObject profileReceiver(SephObject receiver, boolean sawMiss) {
        TypeProfile prof = typeProfile;
        if(prof == null) {
            typeProfile = prof = makeTypeProfile(receiver);
        } else if(!prof.matchAndIncrement(receiver)) {
            typeProfile = prof = prof.append(makeTypeProfile(receiver));
        }
        doPolicy(sawMiss);
        return receiver;
    }

    MethodHandle computeProfiler(boolean forMissPath) {
        MethodHandle profiler = (forMissPath ? MH_profileReceiverForMiss : MH_profileReceiver);
        profiler = insertArguments(profiler, 0, this);
        return profiler;
    }


    public static SephObject initialSetup_intrinsic_true(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_TRUE_SP.guardWithTest(fast, slow);
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject initialSetup_intrinsic_false(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_FALSE_SP.guardWithTest(fast, slow);
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject initialSetup_intrinsic_nil(SephCallSite site, MethodHandle fast, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_NIL_SP.guardWithTest(fast, slow);
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope);
    }

    public static SephObject intrinsic_if(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle test, MethodHandle then, MethodHandle _else) throws Throwable {
        if(((SephObject)test.invoke(thread, scope, true, true)).isTrue()) {
            return (SephObject)then.invoke(thread, scope, true, false);
        } else {
            return (SephObject)_else.invoke(thread, scope, true, false);
        }
    }

    public final static MethodHandle INTRINSIC_IF_MH = findStatic(SephCallSite.class, "intrinsic_if", ARGS_3_SIGNATURE);
    public static SephObject initialSetup_intrinsic_if(SephCallSite site, MethodHandle slow, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle test, MethodHandle then, MethodHandle _else) throws Throwable {
        MethodHandle guarded = thread.runtime.INTRINSIC_IF_SP.guardWithTest(INTRINSIC_IF_MH, slow);
        site.setTarget(guarded);
        return (SephObject)guarded.invoke(receiver, thread, scope, test, then, _else);
    }

    public static boolean eq(Object first, SephObject receiver) {
        return first == receiver.identity();
    }

    private final static MethodHandle EQ = findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, Object.class, SephObject.class));

    public static MethodHandle findStatic(Class target, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findStatic(target, name, type);
        } catch(NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class target, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findVirtual(target, name, type);
        } catch(NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findField(Class target, String name, Class type) {
        try {
            return MethodHandles.lookup().findGetter(target, name, type);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findArrayGetter(Class target) {
        try {
            return MethodHandles.arrayElementGetter(target);
        } catch(IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}// SephCallSite

