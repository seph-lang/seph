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

import static seph.lang.compiler.CompilationHelpers.*;
import static seph.lang.Types.*;
import static seph.lang.compiler.Bootstrap.*;

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
        setNeutral();
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

    MethodHandle computeSlowPath() {
        if(intrinsic) {
            //            System.err.println("WARNING, not implemented slow path for intrinsics yet");
        }

            if(messageKind == "message") {
                MethodHandle _test = MethodHandles.dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, type().parameterArray());
                MethodHandle _then = MethodHandles.filterArguments(MethodHandles.invoker(type()), 0, MethodHandles.insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords()));
                MethodHandle _else = MethodHandles.dropArguments(MethodHandles.identity(SephObject.class), 1, type().parameterArray());
                MethodHandle all = MethodHandles.guardWithTest(_test, _then, _else);
                // TODO: the call to get here will potentially return null for missing attributes. Do a void returning fold in order to check for exceptions
                MethodHandle complete = MethodHandles.foldArguments(all, MethodHandles.dropArguments(MethodHandles.insertArguments(findVirtual(SephObject.class, "get", methodType(SephObject.class, String.class)), 1, messageName), 1, type().dropParameterTypes(0, 1).parameterArray()));

                return complete;
            } else if(messageKind == "tailMessage") {
                MethodHandle _test = MethodHandles.dropArguments(findVirtual(SephObject.class, "isActivatable", methodType(boolean.class)), 1, type().parameterArray());
                MethodHandle _insertArguments = MethodHandles.insertArguments(findStatic(MethodHandles.class, "insertArguments", methodType(MethodHandle.class, MethodHandle.class, int.class, Object[].class)), 1, 0).asCollector(Object[].class, type().parameterCount()).asType(type().insertParameterTypes(0, MethodHandle.class).changeReturnType(MethodHandle.class));
                MethodHandle _activationFor = MethodHandles.insertArguments(findVirtual(SephObject.class, "activationFor", methodType(MethodHandle.class, int.class, boolean.class)), 1, arity(), keywords());
                MethodHandle _filtered = MethodHandles.filterArguments( _insertArguments, 0 ,_activationFor);
                MethodHandle _then = MethodHandles.foldArguments(MethodHandles.dropArguments(MethodHandles.dropArguments(SET_TAIL_MH, 1, SephObject.class, SephObject.class), 4, type().dropParameterTypes(0, 2).parameterArray()), 
                                                                 _filtered);
                MethodHandle _else = MethodHandles.dropArguments(MethodHandles.identity(SephObject.class), 1, type().parameterArray());
                MethodHandle all = MethodHandles.guardWithTest(_test, _then, _else);
                // // TODO: the call to get here will potentially return null for missing attributes. Do a void returning fold in order to check for exceptions
                MethodHandle complete = MethodHandles.foldArguments(all, MethodHandles.dropArguments(MethodHandles.insertArguments(findVirtual(SephObject.class, "get", methodType(SephObject.class, String.class)), 1, messageName), 1, type().dropParameterTypes(0, 1).parameterArray()));

                return complete;
            } else {
                return null;
            }
    }

    public void setNeutral() {
        specializedMisses = 0;
        setTarget(MethodHandles.filterArguments(slowPath, 0, computeProfiler(false)));
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
        elsePath = MethodHandles.filterArguments(elsePath, 0, computeProfiler(true));
        TypeProfile[] cases = allProfiles.cases();
        int casesToCode = cases.length;
        for(int i = casesToCode - 1; i >= 0; i--) {
            MethodHandle[] guardAndFastPath = computeGuardAndFastPath(cases[i]);
            if(guardAndFastPath == null) {
                continue;
            }
            MethodHandle guard = guardAndFastPath[0]; 
            MethodHandle fastPath = guardAndFastPath[1];

            elsePath = MethodHandles.guardWithTest(guard, fastPath, elsePath);
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

            if(intrinsic) {
                //                System.err.println("WARNING, not implemented fast path for intrinsic or tailMessages");
            }
            
            Class[] argumentsToDrop = type().dropParameterTypes(0, 1).parameterArray();
            MethodHandle guard = MethodHandles.dropArguments(EQ.bindTo(((TypeProfile.ForIdentity)prof).matchIdentity()), 1, argumentsToDrop);
            
            SephObject receiver = ((TypeProfile.ForIdentity)prof).matchValue();
            SephObject value = receiver.get(messageName);

            if(value.isActivatable()) {
                MethodHandle activation = value.activationFor(arity(), keywords());
                if(messageKind == "message") {
                    fastPath = activation;
                } else {
                    MethodHandle one = MethodHandles.insertArguments(INSERT_ARGUMENTS_MH, 0, activation, 0);
                    MethodHandle two = one.asCollector(Object[].class, type().parameterCount());
                    MethodHandle three = two.asType(type().changeReturnType(MethodHandle.class));
                    MethodHandle four = MethodHandles.dropArguments(MethodHandles.dropArguments(SET_TAIL_MH, 1, SephObject.class), 3, type().dropParameterTypes(0, 2).parameterArray());
                    MethodHandle folded = MethodHandles.foldArguments(four, three);
                    fastPath = folded;
                }
            } else {
                fastPath = MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, value), 0, type().parameterArray());
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
        profiler = MethodHandles.insertArguments(profiler, 0, this);
        return profiler;
    }





    // public static SephObject slowMessage(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, Object[] args) throws Throwable {
    //     SephObject value = receiver.get(name);
    //     if(null == value) {
    //         throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
    //     }
    //     if(value.isActivatable()) {
    //         MethodHandle a = value.activationFor(site.arity(), site.keywords()).asSpreader(Object[].class, args.length);
    //         return (SephObject)a.invoke(receiver, thread, scope, args);
    //     }
    //     return value;
    // }

    // public static SephObject slowTailMessage(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, Object[] args) {
    //     SephObject value = receiver.get(name);
    //     if(null == value) {
    //         throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
    //     }
    //     if(value.isActivatable()) {
    //         MethodHandle h = value.activationFor(site.arity(), site.keywords()).asSpreader(Object[].class, args.length);
    //         h = MethodHandles.insertArguments(h, 0, receiver, thread, scope, args);
    //         thread.tail = h;
    //         return SThread.TAIL_MARKER;
    //     }
    //     return value;
    // }
































    // private boolean newEntry() {
    //     numberOfGuards++;
    //     if(numberOfGuards > 10) {
    //         morphicity = Morphicity.MEGAMORPHIC;
    //         return false;
    //     } else if(numberOfGuards > 1) {
    //         morphicity = Morphicity.POLYMORPHIC;
    //     } else {
    //         morphicity = Morphicity.MONOMORPHIC;
    //     }
    //     return true;
    // }

    // void installActivatableEntry(SephObject receiver, MethodHandle value, int args, boolean tail) {
    //     if(newEntry()) {
    //         MethodHandle currentEntry = getTarget();
    //         if(tail) {
    //             setTarget(MethodHandles.guardWithTest(eq(receiver, args), tailInvokeActivateWith(value, args), currentEntry));
    //         } else {
    //             setTarget(MethodHandles.guardWithTest(eq(receiver, args), value, currentEntry));
    //         }
    //     }
    // }

    // void installConstantEntry(SephObject receiver, SephObject value, int args) {
    //     if(newEntry()) {
    //         MethodHandle currentEntry = getTarget();
    //         setTarget(MethodHandles.guardWithTest(eq(receiver, args), constantValue(value, args), currentEntry));
    //     }
    // }

    // void installActivatableEntryWithKeywords(SephObject receiver, MethodHandle value, int args, boolean tail) {
    //     if(newEntry()) {
    //         MethodHandle currentEntry = getTarget();
    //         if(tail) {
    //             setTarget(MethodHandles.guardWithTest(eqKeywords(receiver, args), tailInvokeActivateWithKeywords(value, args), currentEntry));
    //         } else {
    //             setTarget(MethodHandles.guardWithTest(eqKeywords(receiver, args), value, currentEntry));
    //         }
    //     }
    // }

    // void installConstantEntryWithKeywords(SephObject receiver, SephObject value, int args) {
    //     if(newEntry()) {
    //         MethodHandle currentEntry = getTarget();
    //         setTarget(MethodHandles.guardWithTest(eqKeywords(receiver, args), constantValueKeywords(value, args), currentEntry));
    //     }
    // }

    public static boolean eq(Object first, SephObject receiver) {
        return first == receiver.identity();
    }

    private final static MethodHandle EQ = Bootstrap.findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, Object.class, SephObject.class));

    private final static Class[] INDETERMINATE = new Class[]{IPersistentList.class};
    private final static Class[] CLASS_0       = new Class[0];
    private final static Class[] CLASS_1       = new Class[]{MethodHandle.class};
    private final static Class[] CLASS_2       = new Class[]{MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_3       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_4       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_5       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};

    private final static Class[] INDETERMINATE_K = new Class[]{IPersistentList.class, String[].class, MethodHandle[].class};
    private final static Class[] CLASS_0_K       = new Class[]{String[].class, MethodHandle[].class};
    private final static Class[] CLASS_1_K       = new Class[]{MethodHandle.class, String[].class, MethodHandle[].class};
    private final static Class[] CLASS_2_K       = new Class[]{MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    private final static Class[] CLASS_3_K       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    private final static Class[] CLASS_4_K       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};
    private final static Class[] CLASS_5_K       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class};

    private static Class[] dropClasses(int num) {
        switch(num) {
        case -1:
            return INDETERMINATE;
        case 0:
            return CLASS_0;
        case 1:
            return CLASS_1;
        case 2:
            return CLASS_2;
        case 3:
            return CLASS_3;
        case 4:
            return CLASS_4;
        case 5:
            return CLASS_5;
        default:
            return null;
        }
    }

    private static Class[] dropClassesKeywords(int num) {
        switch(num) {
        case -1:
            return INDETERMINATE_K;
        case 0:
            return CLASS_0_K;
        case 1:
            return CLASS_1_K;
        case 2:
            return CLASS_2_K;
        case 3:
            return CLASS_3_K;
        case 4:
            return CLASS_4_K;
        case 5:
            return CLASS_5_K;
        default:
            return null;
        }
    }

    private MethodHandle eq(SephObject receiver, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ.bindTo(receiver.identity()), 1, SThread.class, LexicalScope.class), 3, argumentsToDrop);
    }

    private MethodHandle eqKeywords(SephObject receiver, int args) {
        Class[] argumentsToDrop = dropClassesKeywords(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ.bindTo(receiver.identity()), 1, SThread.class, LexicalScope.class), 3, argumentsToDrop);
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, args);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2, arg3);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, args, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    public static SephObject installMethodHandle(MethodHandle mh, SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        thread.tail = MethodHandles.insertArguments(mh, 0, receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywordArguments);
        return SThread.TAIL_MARKER;
    }

    private final static MethodHandle INSTALL_METHOD_HANDLE_ARGS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG0 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG1 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG2 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG3 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG4 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG5 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));

    private final static MethodHandle INSTALL_METHOD_HANDLE_ARGS_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG0_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG1_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG2_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG3_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG4_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG5_KEYWORDS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, String[].class, MethodHandle[].class));

    private static MethodHandle methodHandleForTail(int num) {
        switch(num) {
        case -1:
            return INSTALL_METHOD_HANDLE_ARGS;
        case 0:
            return INSTALL_METHOD_HANDLE_ARG0;
        case 1:
            return INSTALL_METHOD_HANDLE_ARG1;
        case 2:
            return INSTALL_METHOD_HANDLE_ARG2;
        case 3:
            return INSTALL_METHOD_HANDLE_ARG3;
        case 4:
            return INSTALL_METHOD_HANDLE_ARG4;
        case 5:
            return INSTALL_METHOD_HANDLE_ARG5;
        default:
            return null;
        }
    }

    private static MethodHandle methodHandleForTailKeywords(int num) {
        switch(num) {
        case -1:
            return INSTALL_METHOD_HANDLE_ARGS_KEYWORDS;
        case 0:
            return INSTALL_METHOD_HANDLE_ARG0_KEYWORDS;
        case 1:
            return INSTALL_METHOD_HANDLE_ARG1_KEYWORDS;
        case 2:
            return INSTALL_METHOD_HANDLE_ARG2_KEYWORDS;
        case 3:
            return INSTALL_METHOD_HANDLE_ARG3_KEYWORDS;
        case 4:
            return INSTALL_METHOD_HANDLE_ARG4_KEYWORDS;
        case 5:
            return INSTALL_METHOD_HANDLE_ARG5_KEYWORDS;
        default:
            return null;
        }
    }

    private MethodHandle tailInvokeActivateWith(MethodHandle value, int args) {
        MethodHandle mh = methodHandleForTail(args);
        return mh.bindTo(value);
    }

    private MethodHandle tailInvokeActivateWithKeywords(MethodHandle value, int args) {
        MethodHandle mh = methodHandleForTailKeywords(args);
        return mh.bindTo(value);
    }

    private MethodHandle constantValue(SephObject value, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, value), 0, SephObject.class, SThread.class, LexicalScope.class), 0, argumentsToDrop);
    }

    private MethodHandle constantValueKeywords(SephObject value, int args) {
        Class[] argumentsToDrop = dropClassesKeywords(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, value), 0, SephObject.class, SThread.class, LexicalScope.class), 0, argumentsToDrop);
    }
}// SephCallSite

