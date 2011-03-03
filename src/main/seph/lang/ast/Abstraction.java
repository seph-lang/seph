/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.DefaultAbstraction;
import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
import seph.lang.SThread;
import seph.lang.parser.Parser;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class Abstraction extends NamedMessage {
    public Abstraction(String name, IPersistentList arguments, Message next, String filename, int line, int position) {
        super(name, arguments, next, filename, line, position);
    }

    @Override
    public SephObject sendTo(SThread thread, LexicalScope scope, SephObject receiver, boolean first) {
        return DefaultAbstraction.createFrom(this, scope);
    }
}// Abstraction
