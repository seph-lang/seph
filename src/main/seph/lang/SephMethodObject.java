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
import seph.lang.compiler.Bootstrap;

import java.dyn.MethodHandle;
import java.dyn.MethodHandles;
import java.dyn.MethodType;

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

        if(thread.nextArity > -1) {
            int arity = thread.nextArity;
            MethodHandle arg0 = thread.arg0;
            MethodHandle arg1 = thread.arg1;
            MethodHandle arg2 = thread.arg2;
            MethodHandle arg3 = thread.arg3;
            MethodHandle arg4 = thread.arg4;
            thread.clean();
            switch(arity) {
            case 0:
                return activateWith(receiver, thread, scope);
            case 1: 
                return activateWith(receiver, thread, scope, arg0);
            case 2:
                return activateWith(receiver, thread, scope, arg0, arg1);
            case 3:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2);
            case 4:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
            case 5:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            default:
                // Can't happen
                throw new RuntimeException("Should never happen");
            }
        } else {
            IPersistentList arguments = thread.arguments;
            thread.clean();
            return activateWith(receiver, thread, scope, arguments);
        }
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
                    current = (Message)((SephObject)toEvaluate.invokeExact((SThread)null, (LexicalScope)null, false, true));
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
                        part = (SephObject)toEvaluate.invokeExact(thread, scope, true, true);
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

    public static SephObject evaluateMessage(Message msg, SThread thread, LexicalScope scope, boolean evalMessage, boolean ignored) {
        if(evalMessage) {
            return scope.evaluateFully(thread, msg);
        } else {
            return (SephObject)msg;
        }
    }

    public static MethodHandle evaluateMH = Bootstrap.findStatic(SephMethodObject.class, "evaluateMessage", MethodType.methodType(SephObject.class, Message.class, SThread.class, LexicalScope.class, boolean.class, boolean.class));

    public static MethodHandle ensureMH(Object o) {
        if(o instanceof MethodHandle) {
            return (MethodHandle)o;
        } else {
            return evaluateMH.bindTo((Message)o);
        }
    }

    public static ArgumentResult parseAndEvaluateArgumentsUneval(SThread thread, LexicalScope scope, IPersistentList arguments, int posArity) {
        ISeq argsLeft = arguments.seq();
        int currentArg = 0;
        String name;
        ArgumentResult result = new ArgumentResult();
        
        try {
            while(argsLeft != null) {
                Object o = argsLeft.first();
                MethodHandle xx = ensureMH(o);

                result.assign(currentArg, xx);
                currentArg++;
                argsLeft = argsLeft.next();
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }

        return result;
    }

    public static final class ArgumentResult {
        public SephObject arg0;
        public SephObject arg1;
        public SephObject arg2;
        public SephObject arg3;
        public SephObject arg4;
        public MethodHandle argMH0;
        public MethodHandle argMH1;
        public MethodHandle argMH2;
        public MethodHandle argMH3;
        public MethodHandle argMH4;
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

        public final void assign(int index, MethodHandle part) {
            switch(index) {
            case 0:
                argMH0 = part;
                break;
            case 1:
                argMH1 = part;
                break;
            case 2:
                argMH2 = part;
                break;
            case 3:
                argMH3 = part;
                break;
            case 4:
                argMH4 = part;
                break;
            }
        }
    }
}// SephMethodObject
