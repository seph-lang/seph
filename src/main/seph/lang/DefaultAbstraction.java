/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Abstraction;
import seph.lang.ast.Message;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultAbstraction extends SimpleSephObject {
    private Message code;

    private DefaultAbstraction(Message code) {
        super(PersistentArrayMap.EMPTY.associate(activatable, Runtime.TRUE));
        this.code = code;
    }

    public final static DefaultAbstraction createFrom(Abstraction message, LexicalScope scope) {
        Message code = (Message)message.arguments().seq().first();
        return new DefaultAbstraction(code);
    }

    public SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        return scope.evaluate(receiver, code);
    }
}// DefaultAbstraction
