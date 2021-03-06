/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.ISeq;

import seph.lang.ast.Message;
import static seph.lang.SimpleSephObject.activatable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SephMethodObject implements SephObject {
    final IPersistentMap meta;

    public SephMethodObject() {
        meta = new PersistentArrayMap(new Object[]{activatable, Runtime.TRUE});
    }

    public SephObject get(String cellName) {
        return null;
    }

    public Object identity() {
        return this;
    }

    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }

    public boolean isTrue() {
        return true;
    }

    public static ArgumentResult parseAndEvaluateArguments(SThread thread, LexicalScope scope, MethodHandle[] arguments, int posArity, boolean restPos, boolean restKey) {
        List<SephObject> restp = restPos ? new ArrayList<SephObject>(arguments.length - posArity) : (List<SephObject>)null;
        List<String> namesk = restKey ? new LinkedList<String>() : (List<String>)null;
        List<MethodHandle> valuesk = restKey ? new LinkedList<MethodHandle>() : (List<MethodHandle>)null;
        int currentArg = 0;
        String name;
        boolean collectingRestp = restPos && currentArg > posArity;
        ArgumentResult result = new ArgumentResult();
        
        try {
            for(MethodHandle toEvaluate : arguments) {
                Message current = (Message)((SephObject)toEvaluate.invokeExact((SThread)null, (LexicalScope)null, false, true));

                if(restKey && (name = current.name()).endsWith(":")) {
                    name = name.substring(0, name.length()-1);
                    SephObject part = (SephObject)toEvaluate.invokeExact(thread, scope, true, true);
                    namesk.add(name);
                    valuesk.add(MethodHandles.dropArguments(MethodHandles.constant(SephObject.class, part), 0, SThread.class, LexicalScope.class, boolean.class, boolean.class));
                } else {
                    SephObject part = (SephObject)toEvaluate.invokeExact(thread, scope, true, true);

                    if(collectingRestp) {
                        restp.add(part);
                    } else {
                        result.assign(currentArg, part);
                        currentArg++;
                        collectingRestp = restPos && currentArg > posArity;
                    }
                }
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }

        if(restPos) {
            result.restPositional = restp.toArray(new SephObject[0]);
        }

        if(restKey) {
            result.restKeywordNames = namesk.toArray(new String[0]);
            result.restKeywordMHs = valuesk.toArray(new MethodHandle[0]);
        }

        return result;
    }

    public static ArgumentResult parseAndEvaluateArgumentsUneval(SThread thread, LexicalScope scope, MethodHandle[] arguments, int posArity) {
        int currentArg = 0;
        String name;
        ArgumentResult result = new ArgumentResult();
        
        try {
            for(MethodHandle xx : arguments) {
                result.assign(currentArg, xx);
                currentArg++;
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
        public SephObject[] restPositional;
        public String[] restKeywordNames;
        public MethodHandle[] restKeywordMHs;

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
