/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.List;
import java.util.ArrayList;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.ISeq;

import seph.lang.ast.Message;
import static seph.lang.SimpleSephObject.activatable;

import java.dyn.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SephMethodObject implements SephObject, TailCallable {
    final IPersistentMap meta;

    public SephMethodObject() {
        meta = new PersistentArrayMap(new Object[]{activatable, Runtime.TRUE});
    }

    public SephObject get(String cellName) {
        return null;
    }

    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }

    public boolean isTrue() {
        return true;
    }

    @Override
    public SephObject goTail(SThread thread) {
        SephObject receiver = thread.nextReceiver;
        LexicalScope scope = thread.nextScope;
        IPersistentList arguments = thread.arguments;
        thread.nextReceiver = null;
        thread.nextScope = null;
        thread.arguments = null;
        thread.nextTail = null;
        return activateWith(receiver, thread, scope, arguments);
    }

    public static ArgumentResult parseAndEvaluateArguments(SThread thread, LexicalScope scope, IPersistentList arguments, int posArity, boolean restPos, boolean restKey) {
        List restp = restPos ? new ArrayList(arguments.count() - posArity) : (List)null;
        IPersistentMap restk = restKey ? PersistentArrayMap.EMPTY : (IPersistentMap)null;
        ISeq argsLeft = arguments.seq();
        int currentArg = 0;
        String name;
        boolean collectingRestp = restPos && currentArg > posArity;
        ArgumentResult result = new ArgumentResult();
        
        try {
            while(argsLeft != null) {
                Object o = argsLeft.first();
                Message current;
                MethodHandle toEvaluate = null;
                if(o instanceof MethodHandle) {
                    toEvaluate = (MethodHandle)o;
                    current = (Message)((SephObject)toEvaluate.invokeExact((SThread)null, (LexicalScope)null, false));
                } else {
                    current = (Message)o;
                }

                if(restKey && (name = current.name()).endsWith(":")) {
                    name = name.substring(0, name.length()-1);
                    current = current.next();
                    SephObject part = scope.evaluateFully(thread, current);
                    restk = restk.associate(name, part);
                } else {
                    SephObject part = null;
                    if(toEvaluate == null) {
                        part = scope.evaluateFully(thread, current);
                    } else {
                        part = (SephObject)toEvaluate.invokeExact(thread, scope, true);
                    }

                    if(collectingRestp) {
                        restp.add(part);
                    } else {
                        result.assign(currentArg, part);
                        currentArg++;
                        collectingRestp = restPos && currentArg > posArity;
                    }
                }
                argsLeft = argsLeft.next();
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }

        if(restPos) {
            result.restPositional = PersistentList.create(restp);
        }

        if(restKey) {
            result.restKeywords = restk;
        }

        return result;
    }

    public static final class ArgumentResult {
        public SephObject arg0;
        public SephObject arg1;
        public SephObject arg2;
        public SephObject arg3;
        public SephObject arg4;
        public IPersistentList restPositional;
        public IPersistentMap restKeywords;

        public final void assign(int index, SephObject part) {
            switch(index) {
            case 0:
                arg0 = part;
                break;
            case 1:
                arg1 = part;
                break;
            case 2:
                arg2 = part;
                break;
            case 3:
                arg3 = part;
                break;
            case 4:
                arg4 = part;
                break;
            }
        }
    }
}// SephMethodObject
