/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.DefaultAbstraction;
import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
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

    public SephObject sendTo(LexicalScope scope, SephObject receiver, Runtime runtime) {
        return DefaultAbstraction.createFrom(this, scope);
    }
}// Abstraction
