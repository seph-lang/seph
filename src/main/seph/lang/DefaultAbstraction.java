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
    private LexicalScope capture;

    private DefaultAbstraction(Message code, LexicalScope capture) {
        super(PersistentArrayMap.EMPTY.associate(activatable, Runtime.TRUE));
        this.code = code;
        this.capture = capture;
    }

    public final static DefaultAbstraction createFrom(Abstraction message, LexicalScope scope) {
        Message code = (Message)message.arguments().seq().first();
        return new DefaultAbstraction(code, scope);
    }

    public SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        return capture.newScopeWith(receiver).evaluate(code);
    }
}// DefaultAbstraction
