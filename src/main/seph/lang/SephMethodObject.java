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

    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }

    public boolean isTrue() {
        return true;
    }

    protected ArgumentResult parseAndEvaluateArguments(SThread thread, LexicalScope scope, IPersistentList arguments, int posArity, boolean restPos, boolean restKey) {
        List restp = restPos ? new ArrayList(arguments.count() - posArity) : (List)null;
        IPersistentMap restk = restKey ? PersistentArrayMap.EMPTY : (IPersistentMap)null;
        ISeq argsLeft = arguments.seq();
        int currentArg = 0;
        String name;
        boolean collectingRestp = restPos && currentArg > posArity;
        ArgumentResult result = new ArgumentResult();

        while(argsLeft != null) {
            Message current = (Message)argsLeft.first();
            if(restKey && (name = current.name()).endsWith(":")) {
                name = name.substring(0, name.length()-1);
                current = current.next();
                SephObject part = scope.evaluate(thread, current);
                restk = restk.associate(name, part);
            } else {
                if(collectingRestp) {
                    restp.add(scope.evaluate(thread, current));
                } else {
                    SephObject part = scope.evaluate(thread, current);
                    result.assign(currentArg, part);
                    currentArg++;
                    collectingRestp = restPos && currentArg > posArity;
                }
            }
            argsLeft = argsLeft.next();
        }

        if(restPos) {
            result.restPositional = PersistentList.create(restp);
        }

        if(restKey) {
            result.restKeywords = restk;
        }

        return result;
    }

    protected static final class ArgumentResult {
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
