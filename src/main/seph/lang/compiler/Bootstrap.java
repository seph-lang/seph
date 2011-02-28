/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import seph.lang.SephObject;
import seph.lang.SThread;
import seph.lang.LexicalScope;

import seph.lang.persistent.PersistentList;

import java.dyn.CallSite;
import java.dyn.MethodHandle;
import java.dyn.MethodHandles;
import java.dyn.MethodType;

import static seph.lang.compiler.CompilationHelpers.*;

public class Bootstrap {
    public final static MethodType BOOTSTRAP_SIGNATURE      = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
    public final static String     BOOTSTRAP_SIGNATURE_DESC = sig(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);

    public static CallSite basicSephBootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        //        System.err.println("lookup: " + lookup + ", name: " + name + ", type: " + type);
        SephCallSite site = new SephCallSite(type);
        MethodType fallbackType = type.insertParameterTypes(0, SephCallSite.class, String.class);
        MethodHandle fallback = MethodHandles.insertArguments(findStatic(Bootstrap.class, "fallback", fallbackType), 0, site, name);
        site.setTarget(fallback);
        return site;
    }

    public static SephObject fallback(SephCallSite site, String name, SThread thread, LexicalScope scope, SephObject receiver) {
        //        System.err.println("Calling method " + name + " on: " + receiver + " with lexical scope: " + scope);
        SephObject value = scope.get(name);

        if(null == value) {
            value = receiver.get(name);
        }

        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }

        if(value.isActivatable()) {
            return value.activateWith(thread, scope, receiver, PersistentList.EMPTY);
        }

        return value;
    }

    private static MethodHandle findStatic(Class target, String name, MethodType type) {
        try {
            return MethodHandles.lookup().findStatic(target, name, type);
        } catch(NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch(IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}