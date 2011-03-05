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
            //            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), invokeActivateWith(value, args), currentEntry));
        }
    }

    void installActivatableEntry(SephObject receiver, LexicalScope scope, MethodHandle value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            //            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), tailInvokeActivateWith(value, args), currentEntry));
        }
    }

    void installConstantEntry(SephObject receiver, LexicalScope scope, SephObject value, int args) {
        if(newEntry()) {
            MethodHandle currentEntry = getTarget();
            //            setTarget(MethodHandles.guardWithTest(eq(receiver, scope, args), constantValue(value, args), currentEntry));
        }        
    }

    // (SephObject receiver, SThread thread, LexicalScope scope, args...)SephObject

    public static boolean eq(SephObject first, SephObject receiver) {
        return first == receiver;
    }

    public static boolean eq(SephObject first, LexicalScope firstScope, int scopeVersion, SephObject receiver, LexicalScope scope) {
        return first == receiver && firstScope == scope && scopeVersion == scope.version();
    }

    private final static MethodHandle EQ_SIMPLE = Bootstrap.findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, SephObject.class, SephObject.class));
    private final static MethodHandle EQ_SCOPE =  Bootstrap.findStatic(SephCallSite.class, "eq", MethodType.methodType(boolean.class, SephObject.class, LexicalScope.class, int.class, SephObject.class, LexicalScope.class));

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
            return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ_SIMPLE.bindTo(receiver), 1, SThread.class, LexicalScope.class), 1, argumentsToDrop);
        } else {
            return MethodHandles.dropArguments(MethodHandles.dropArguments(EQ_SCOPE.bindTo(receiver).bindTo(scope).bindTo(scope.version()), 1, SThread.class), 2, argumentsToDrop);
        }
    }

    private MethodHandle invokeActivateWith(SephObject value, int args) {
        return null;
    }

    private MethodHandle tailInvokeActivateWith(MethodHandle value, int args) {
        return null;
    }

    public static SephObject constant(SephObject value) {
        return value;
    }

    private MethodHandle constantValue(SephObject value, int args) {
        Class[] argumentsToDrop = dropClasses(args);
        return MethodHandles.dropArguments(MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, value), 0, SephObject.class, SThread.class, LexicalScope.class), 0, argumentsToDrop);
    }
}// SephCallSite

