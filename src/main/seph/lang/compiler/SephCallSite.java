/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.*;
import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

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

    void installActivatableEntryWithKeywords(SephObject receiver, LexicalScope scope, SephObject value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles. guardWithTest(eqKeywords(receiver, scope, args), invokeActivateWithKeywords(value, args), currentEntry));
        }
    }

    void installActivatableEntryWithKeywords(SephObject receiver, LexicalScope scope, MethodHandle value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles.guardWithTest(eqKeywords(receiver, scope, args), tailInvokeActivateWithKeywords(value, args), currentEntry));
        }
    }

    void installConstantEntryWithKeywords(SephObject receiver, LexicalScope scope, SephObject value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            setTarget(MethodHandles.guardWithTest(eqKeywords(receiver, scope, args), constantValueKeywords(value, args), currentEntry));
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

    private MethodHandle eq(SephObject receiver, LexicalScope scope, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        if(scope == null) {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ_SIMPLE.bindTo(receiver.identity()), 1, SThread.class, LexicalScope.class), 3, argumentsToDrop);
        } else {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.insertArguments(EQ_SCOPE, 0, receiver.identity(), scope, scope.version()), 1, SThread.class), 3, argumentsToDrop);
        }
    }

    private MethodHandle eqKeywords(SephObject receiver, LexicalScope scope, int args) {
        Class[] argumentsToDrop = dropClassesKeywords(args);
        if(scope == null) {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ_SIMPLE.bindTo(receiver.identity()), 1, SThread.class, LexicalScope.class), 3, argumentsToDrop);
        } else {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.insertArguments(EQ_SCOPE, 0, receiver.identity(), scope, scope.version()), 1, SThread.class), 3, argumentsToDrop);
        }
    }

    private MethodHandle invokeActivateWith(SephObject value, int args) {
        return activateWithMH(args).bindTo(value);
    }

    private MethodHandle invokeActivateWithKeywords(SephObject value, int args) {
        return activateWithMHKeywords(args).bindTo(value);
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

    private static MethodHandle activateWithMHKeywords(int num) {
        switch(num) {
        case -1:
            return Bootstrap.ACTIVATE_WITH_ARGS_KEYWORDS;
        case 0:
            return Bootstrap.ACTIVATE_WITH_ARG0_KEYWORDS;
        case 1:
            return Bootstrap.ACTIVATE_WITH_ARG1_KEYWORDS;
        case 2:
            return Bootstrap.ACTIVATE_WITH_ARG2_KEYWORDS;
        case 3:
            return Bootstrap.ACTIVATE_WITH_ARG3_KEYWORDS;
        case 4:
            return Bootstrap.ACTIVATE_WITH_ARG4_KEYWORDS;
        case 5:
            return Bootstrap.ACTIVATE_WITH_ARG5_KEYWORDS;
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

