/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.Map;

import seph.lang.ast.Message;
import seph.lang.interpreter.MessageInterpreter;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.APersistentMap;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.RT;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LexicalScope {
    public final static LexicalScope ROOT = new LexicalScope(null) {
            public LexicalScope find(String name, LexicalScope def) {
                return def;
            }

            public SephObject get(String name) {
                return null;
            }
        };
    
    private final MessageInterpreter currentInterpreter;
    private final LexicalScope parent;

    public LexicalScope(MessageInterpreter currentInterpreter) {
        this(currentInterpreter, ROOT);
    }

    public LexicalScope(MessageInterpreter currentInterpreter, LexicalScope parent) {
        this.currentInterpreter = currentInterpreter;
        this.parent = parent;
    }

    public LexicalScope newScopeWith(SephObject ground) {
        return currentInterpreter.newScope(ground);
    }

    public SephObject evaluate(SThread thread, Message message) {
        return (SephObject)this.currentInterpreter.evaluate(thread, message);
    }

    public SephObject evaluateFully(SThread thread, Message message) {
        return (SephObject)this.currentInterpreter.evaluateFully(thread, message);
    }

    private IPersistentMap values = PersistentArrayMap.EMPTY;

    public LexicalScope find(String name, LexicalScope def) {
        if(values.containsKey(name)) {
            return this;
        } else {
            return parent.find(name, def);
        }
    }

    public void directlyAssign(String name, SephObject value) {
        values = values.associate(name, value);
    }

    public void assign(String name, SephObject value) {
        LexicalScope place = find(name, this);
        place.directlyAssign(name, value);
    }

    public SephObject get(String name) {
        SephObject result = (SephObject)values.valueAt(name);
        if(null == result) {
            result = parent.get(name);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        ISeq seq = RT.seq(values);
        String sep = "";
        for(;RT.next(seq) != null; seq = RT.next(seq)) {
            Map.Entry me = (Map.Entry)RT.first(seq);
            sb.append(me.getKey()).append(" => ").append(me.getValue()).append(sep);
            sep = ", ";
        }
        sb.append("}");
        if(parent != ROOT) {
            sb.append("(").append(parent).append(")");
        }

        return sb.toString();
    }
}// LexicalScope
