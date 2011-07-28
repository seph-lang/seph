/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.util.*;

import seph.lang.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodType;
import static java.lang.invoke.MethodType.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodHandles.invoker;
import static java.lang.invoke.MethodHandles.exactInvoker;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodHandles.filterArguments;
import static java.lang.invoke.MethodHandles.foldArguments;
import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.identity;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.filterReturnValue;
import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.ActivationHelpers.*;
import static seph.lang.Types.*;

public class SephCallSite extends MutableCallSite {
    public static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type, Object... arguments) {
        if(name.intern() == "seph:pumpTailCall") {
            return new ConstantCallSite(PUMP_MH.bindTo(SThread.TAIL_MARKER));
            // return genMH(SThread.TAIL_MARKER);
        } else if(name.startsWith("seph:activationFor:")) {
            return new ConstantCallSite(createActivationForMH(name, (MethodHandle)arguments[0], (MethodHandle)arguments[1]));
        } else {
            return new SephCallSite(lookup, name, type, arguments);
        }
    }

    private static MethodHandle createActivationForMH(String name, MethodHandle specific, MethodHandle generic) {
        String[] pieces = name.split(":");
        String realName = decode(pieces[2]);
        int arity = Integer.valueOf(pieces[3]);
        boolean keywords = Boolean.valueOf(pieces[4]);

        MethodHandle testSpecificArity = dropArguments(dropArguments(insertArguments(INT_EQ, 0, arity), 0, Object.class), 2, boolean.class);
        MethodHandle testGenericArity  = dropArguments(dropArguments(insertArguments(INT_EQ, 0, -1), 0, Object.class), 2, boolean.class);
        MethodHandle testKeywords      = dropArguments(identity(boolean.class), 0, Object.class, int.class);
        MethodHandle thenSpecific      = dropArguments(BIND_TO.bindTo(specific), 1, int.class, boolean.class);
        MethodHandle thenGeneric       = dropArguments(BIND_TO.bindTo(generic), 1, int.class, boolean.class);

        MethodHandle arityProblem      = dropArguments(dropArguments(insertArguments(insertArguments(ARITY_ERROR, 0, arity), 1, realName), 0, Object.class), 2, boolean.class);

        MethodHandle guardedOnArity    = guardWithTest(testSpecificArity,
                                                       thenSpecific,
                                                       guardWithTest(testGenericArity,
                                                                     thenGeneric,
                                                                     arityProblem));
        if(keywords) {
            return guardWithTest(testKeywords,
                                 guardedOnArity,
                                 arityProblem);
        } else {
            return guardWithTest(testKeywords,
                                 arityProblem,
                                 guardedOnArity);
        }
    }

    public static SephObject pump(SephObject marker, SephObject receiver, SThread thread) throws Throwable {
        SephObject result = receiver;
        while(marker == result) {
            MethodHandle mh = thread.tail;
            result = (SephObject)mh.invokeExact();
        }
        return result;
    }

    public static CallSite genMH(SephObject marker) {
        MutableCallSite mcs = new MutableCallSite(methodType(SephObject.class, SephObject.class, SThread.class));
        MethodHandle _test = dropArguments(REF_EQ.bindTo(marker), 1, SThread.class);
        MethodHandle combiner = filterReturnValue(dropArguments(THREAD_TAIL_GETTER, 0, SephObject.class), exactInvoker(methodType(SephObject.class)));
        MethodHandle _then = foldArguments(dropArguments(mcs.dynamicInvoker(), 1, SephObject.class), combiner);
        MethodHandle _else = dropArguments(identity(SephObject.class), 1, SThread.class);
        MethodHandle pumper = guardWithTest(_test,
                                            _then,
                                            _else);
        mcs.setTarget(pumper);
        return mcs;
    }

    private final static MethodHandle PUMP_MH = findStatic(SephCallSite.class, "pump", methodType(SephObject.class, SephObject.class, SephObject.class, SThread.class));

    private final boolean lexical;
    private final Integer lexicalDepth;
    private final Integer lexicalIndex;
    private final String messageKind;
    private final String messageName;
    private final MethodHandle slowPath;
    private final Object[] argumentMHs;

    private SephCallSite(MethodHandles.Lookup lookup, String name, MethodType type, Object[] argumentMHs) {
        super(type);

        Object[] pieces = kindNameAndExtraInfo(name.split(":"));
        this.messageKind  = (String)pieces[0];
        this.messageName  = (String)pieces[1];
        this.lexical      = (Boolean)pieces[2];
        this.lexicalDepth = (Integer)pieces[3];
        this.lexicalIndex = (Integer)pieces[4];

        this.slowPath     = computeSlowPath();
        this.argumentMHs  = argumentMHs;

        if(lexical && "lookup".equals(messageKind)) {
            setTarget(computeLexicalLookupPath());
        } else if("invoke".equals(messageKind) || "tailInvoke".equals(messageKind)) {
            setTarget(computeInvokeOnCurrentObject());

        } else {
            setNeutral();
        }
    }
    
    private MethodHandle computeLexicalPath() {
        MethodHandle current = identity(LexicalScope.class);

        int currentDepth = lexicalDepth;
        while(currentDepth-- > 0) {
            current = filterArguments(current, 0, PARENT_SCOPE_METHOD);
        }
        
        MethodHandle valueMH = null;
        switch(lexicalIndex) {
        case 0:
            valueMH = filterArguments(SCOPE_0_GETTER_M, 0, current);
            break;
        case 1:
            valueMH = filterArguments(SCOPE_1_GETTER_M, 0, current);
            break;
        case 2:
            valueMH = filterArguments(SCOPE_2_GETTER_M, 0, current);
            break;
        case 3:
            valueMH = filterArguments(SCOPE_3_GETTER_M, 0, current);
            break;
        case 4:
            valueMH = filterArguments(SCOPE_4_GETTER_M, 0, current);
            break;
        case 5:
            valueMH = filterArguments(SCOPE_5_GETTER_M, 0, current);
            break;
        default:
            valueMH = filterArguments(insertArguments(SCOPE_N_GETTER_M, 0, lexicalIndex-6), 0, current);
            break;
        }

        MethodHandle all = null;
        if(messageKind == "message") {
            all = computeRegularMessage(type());
        } else if(messageKind == "tailMessage") {
            all = computeTailMessage(type());
        }
        return foldArguments(all,
                             dropArguments(dropArguments(valueMH, 0, SephObject.class, SThread.class), 3, type().dropParameterTypes(0, 3).parameterArray()));
    }

    private MethodHandle computeInvokeOnCurrentObject() {
        MethodHandle all = null;
        if(messageKind == "invoke") {
            all = computeRegularMessage(type().dropParameterTypes(0,1));
        } else if(messageKind == "tailInvoke") {
            all = computeTailMessage(type().dropParameterTypes(0,1));
        }
        return all;
    }

    private MethodHandle computeLexicalLookupPath() {
        MethodHandle current = identity(LexicalScope.class);

        int currentDepth = lexicalDepth;
        while(currentDepth-- > 0) {
            current = filterArguments(current, 0, PARENT_SCOPE_METHOD);
        }
        
        MethodHandle valueMH = null;
        switch(lexicalIndex) {
        case 0:
            valueMH = filterArguments(SCOPE_0_GETTER_M, 0, current);
            break;
        case 1:
            valueMH = filterArguments(SCOPE_1_GETTER_M, 0, current);
            break;
        case 2:
            valueMH = filterArguments(SCOPE_2_GETTER_M, 0, current);
            break;
        case 3:
            valueMH = filterArguments(SCOPE_3_GETTER_M, 0, current);
            break;
        case 4:
            valueMH = filterArguments(SCOPE_4_GETTER_M, 0, current);
            break;
        case 5:
            valueMH = filterArguments(SCOPE_5_GETTER_M, 0, current);
            break;
        default:
            valueMH = filterArguments(insertArguments(SCOPE_N_GETTER_M, 0, lexicalIndex-6), 0, current);
            break;
        }

        return valueMH;
    }

    private MethodHandle computeRegularMessage(MethodType theType) {
        MethodHandle _test = dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, theType.parameterArray());
                
        MethodHandle invoker = MethodHandles.exactInvoker(theType);
        MethodHandle _then = filterArguments(invoker, 0, insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords()));
        MethodHandle _else = dropArguments(identity(SephObject.class), 1, theType.parameterArray());
        return guardWithTest(_test, _then, _else);
    }

    private MethodHandle computeTailMessage(MethodType theType) {
        MethodHandle _test = dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, theType.parameterArray());
        MethodHandle _insertArguments = insertArguments(findStatic(MethodHandles.class, "insertArguments", methodType(MethodHandle.class, MethodHandle.class, int.class, Object[].class)), 1, 0).asCollector(Object[].class, theType.parameterCount()).asType(theType.insertParameterTypes(0, MethodHandle.class).changeReturnType(MethodHandle.class));
        MethodHandle _activationFor = insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords());
        MethodHandle _filtered = filterArguments( _insertArguments, 0 ,_activationFor);
        MethodHandle _then = foldArguments(dropArguments(dropArguments(SET_TAIL_MH, 1, SephObject.class, SephObject.class), 4, theType.dropParameterTypes(0, 2).parameterArray()), 
                                               _filtered);
        MethodHandle _else = dropArguments(identity(SephObject.class), 1, theType.parameterArray());
        return guardWithTest(_test, _then, _else);
    }

    private int arity = -2;
    private boolean keywords = false;;
    
    private void computeArity() {
        int diff = 3;
        if("invoke".equals(messageKind) || "tailInvoke".equals(messageKind)) {
            diff = 4;
        }
        int num = type().parameterCount() - diff;


        if(num == 0) {
            keywords = false;
            arity = 0;
        } else {
            Class<?>[] tp = type().parameterArray();
            int minus = 0;
            if(tp[tp.length-2] == String[].class) {
                keywords = true;
                minus = 2;
            }
            if(num == 1 && tp[diff] == MethodHandle[].class) {
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

    private static Object[] kindNameAndExtraInfo(Object[] nc) {
        String kind = ((String)nc[1]).intern();
        String name = decode((String)nc[2]).intern();
        boolean lexical   = nc.length == 6 && "lexical".equals(nc[3]);
        Integer lexicalDepth = lexical ? Integer.valueOf((String)nc[4]) : null;
        Integer lexicalIndex = lexical ? Integer.valueOf((String)nc[5]) : null;
        
        return new Object[] { kind, name, lexical, lexicalDepth, lexicalIndex };
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
        MethodHandle all = null;
        if(messageKind == "message") {
            all = computeRegularMessage(type());
        } else if(messageKind == "tailMessage") {
            all = computeTailMessage(type());
        } else {
            return null;
        }

        MethodHandle _nullCheck = dropArguments(insertArguments(CHECK_FOR_NULL_MH, 0, messageName), 
                                                2, 
                                                type().dropParameterTypes(0, 1).parameterArray());

        return foldArguments(foldArguments(all, _nullCheck),
                             dropArguments(insertArguments(findVirtual(SephObject.class, "get", methodType(SephObject.class, String.class)), 1, messageName), 1, type().dropParameterTypes(0, 1).parameterArray()));
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

    public static boolean eq(Object first, SephObject receiver) {
        return first == receiver.identity();
    }

    public static boolean ref_eq(SephObject first, SephObject second) {
        return first == second;
    }

    public static boolean int_eq(int first, int second) {
        return first == second;
    }

    private final static MethodHandle EQ = findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, Object.class, SephObject.class));
    private final static MethodHandle REF_EQ = findStatic(SephCallSite.class, "ref_eq", MethodType.methodType(boolean.class, SephObject.class, SephObject.class));
    private final static MethodHandle INT_EQ = findStatic(SephCallSite.class, "int_eq", MethodType.methodType(boolean.class, int.class, int.class));

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

    public static MethodHandle findArrayGetter(Class target) {
        try {
            return MethodHandles.arrayElementGetter(target);
        } catch(IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}// SephCallSite

