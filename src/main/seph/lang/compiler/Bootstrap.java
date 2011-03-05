/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.SephObject;
import seph.lang.SThread;
import seph.lang.LexicalScope;
import seph.lang.TailCallable;

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

    private static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type, String boostrapType) {
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, boostrapType, fallbackType), 0, site, decode(name));
        site.setTarget(fallback);
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

    public static SephObject tailCallFallback(SephCallSite site, String name, SephObject receiver, SThread thread, LexicalScope scope, IPersistentList args) {
        SephObject value = receiver.get(name);
        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }
        if(value.isActivatable()) {
            site.installActivatableEntry(receiver, null, (TailCallable)value, -1);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.arguments    = args;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, args);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 0);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 0;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 1);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 1;
                thread.arg0         = arg0;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 2);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 2;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 3);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 3;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 4);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 4;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                thread.arg3         = arg3;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
            }
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
            site.installActivatableEntry(receiver, null, (TailCallable)value, 5);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 5;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                thread.arg3         = arg3;
                thread.arg4         = arg4;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, -1);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.arguments    = args;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, args);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 0);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 0;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 1);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 1;
                thread.arg0         = arg0;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 2);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 2;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 3);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 3;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 4);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 4;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                thread.arg3         = arg3;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
            }
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
            site.installActivatableEntry(receiver, scope, (TailCallable)value, 5);
            if(value instanceof TailCallable) {
                thread.nextTail     = (TailCallable)value;
                thread.nextReceiver = receiver;
                thread.nextScope    = scope;
                thread.nextArity    = 5;
                thread.arg0         = arg0;
                thread.arg1         = arg1;
                thread.arg2         = arg2;
                thread.arg3         = arg3;
                thread.arg4         = arg4;
                return SThread.TAIL_MARKER;
            } else {
                System.err.println("OOPS. tail call on value that isn't tail callable: " + name);
                return value.activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            }
        } else {
            site.installConstantEntry(receiver, scope, value, 5);
        }
        return value;
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
