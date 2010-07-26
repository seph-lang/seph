/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Message;
import seph.lang.interpreter.MessageInterpreter;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.PersistentArrayMap;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LexicalScope {
    public final static LexicalScope EMPTY = new LexicalScope(null);
    
    private final MessageInterpreter currentInterpreter;

    public LexicalScope(MessageInterpreter currentInterpreter) {
        this.currentInterpreter = currentInterpreter;
    }

    public SephObject evaluate(Message message) {
        return (SephObject)this.currentInterpreter.evaluate(message);
    }

    public SephObject evaluate(SephObject ground, Message message) {
        return (SephObject)this.currentInterpreter.withGround(ground).evaluate(message);
    }

    private IPersistentMap values = PersistentArrayMap.EMPTY;

    public void assign(String name, SephObject value) {
        values = values.associate(name, value);
    }

    public SephObject get(String name) {
        return (SephObject)values.valueAt(name);
    }
}// LexicalScope
