/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.*;
import seph.lang.persistent.IPersistentList;

import java.dyn.MutableCallSite;
import java.dyn.MethodType;
import java.dyn.MethodHandle;
import java.dyn.MethodHandles;

import static seph.lang.compiler.CompilationHelpers.*;

public class SephCallSite extends MutableCallSite {
    private enum Morphicity {
        NILADIC, MONOMORPHIC, POLYMORPHIC, MEGAMORPHIC
    }        

    int numberOfGuards = 0;
    Morphicity morphicity = Morphicity.NILADIC;

    public SephCallSite(MethodType type) {
        super(type);
    }

    private boolean newEntry() {
        numberOfGuards++;
        if(numberOfGuards > 10) {
            morphicity = Morphicity.MEGAMORPHIC;
            return false;
        } else if(numberOfGuards > 1) {
            morphicity = Morphicity.POLYMORPHIC;
        } else {
            morphicity = Morphicity.MONOMORPHIC;
        }
        return true;
    }

    void installActivatableEntry(SephObject receiver, LexicalScope scope, SephObject value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), invokeActivateWith(value, args), currentEntry));
        }
    }

    void installActivatableEntry(SephObject receiver, LexicalScope scope, MethodHandle value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), tailInvokeActivateWith(value, args), currentEntry));
        }
    }

    void installConstantEntry(SephObject receiver, LexicalScope scope, SephObject value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), constantValue(value, args), currentEntry));
        }
    }

    public static boolean eq(Object first, SephObject receiver) {
        return first == receiver.identity();
    }

    public static boolean eq(Object first, LexicalScope firstScope, int scopeVersion, SephObject receiver, LexicalScope scope) {
        return first == receiver.identity() && firstScope == scope && scopeVersion == scope.version();
    }

    private final static MethodHandle EQ_SIMPLE = Bootstrap.findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, Object.class, SephObject.class));
    private final static MethodHandle EQ_SCOPE =  Bootstrap.findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, Object.class, LexicalScope.class, int.class, SephObject.class, LexicalScope.class));

    private final static Class[] INDETERMINATE = new Class[]{IPersistentList.class};
    private final static Class[] CLASS_0       = new Class[0];
    private final static Class[] CLASS_1       = new Class[]{MethodHandle.class};
    private final static Class[] CLASS_2       = new Class[]{MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_3       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_4       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};
    private final static Class[] CLASS_5       = new Class[]{MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class};

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

    private MethodHandle eq(SephObject receiver, LexicalScope scope, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        if(scope == null) {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ_SIMPLE.bindTo(receiver.identity()), 1, SThread.class, LexicalScope.class), 3, argumentsToDrop);
        } else {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.insertArguments(EQ_SCOPE, 0, receiver.identity(), scope, scope.version()), 1, SThread.class), 3, argumentsToDrop);
        }
    }

    private MethodHandle invokeActivateWith(SephObject value, int args) {
        return activateWithMH(args).bindTo(value);
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

    private final static MethodHandle INSTALL_METHOD_HANDLE_ARGS = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, IPersistentList.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG0 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG1 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG2 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG3 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG4 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));
    private final static MethodHandle INSTALL_METHOD_HANDLE_ARG5 = Bootstrap.findStatic(SephCallSite.class, "installMethodHandle", MethodType.methodType(SephObject.class, MethodHandle.class, SephObject.class, SThread.class, LexicalScope.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class, MethodHandle.class));

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

    private static MethodHandle activateWithMH(int num) {
        switch(num) {
        case -1:
            return Bootstrap.ACTIVATE_WITH_ARGS;
        case 0:
            return Bootstrap.ACTIVATE_WITH_ARG0;
        case 1:
            return Bootstrap.ACTIVATE_WITH_ARG1;
        case 2:
            return Bootstrap.ACTIVATE_WITH_ARG2;
        case 3:
            return Bootstrap.ACTIVATE_WITH_ARG3;
        case 4:
            return Bootstrap.ACTIVATE_WITH_ARG4;
        case 5:
            return Bootstrap.ACTIVATE_WITH_ARG5;
        default:
            return null;
        }
    }

    private MethodHandle tailInvokeActivateWith(MethodHandle value, int args) {
        MethodHandle mh = methodHandleForTail(args);
        return mh.bindTo(value);
    }

    private MethodHandle constantValue(SephObject value, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, value), 0, SephObject.class, SThread.class, LexicalScope.class), 0, argumentsToDrop);
    }
}// SephCallSite

