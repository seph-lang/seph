/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.List;

import seph.lang.SephObject;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.PersistentCons;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class NamedMessage implements Message, SephObject {
    private final String name;
    private final PersistentList arguments;
    private final Message next;

    private final String filename;
    private final int line;
    private final int position;

    final static PersistentList NO_ARGUMENTS = PersistentCons.EMPTY;
    
    public NamedMessage(String name, PersistentList arguments, Message next, String filename, int line, int position) {
        this.name = name;
        this.arguments = arguments == null ? NO_ARGUMENTS : arguments;
        this.next = next;
        this.filename = filename;
        this.line = line;
        this.position = position;
    }

    public String name() {
        return this.name;
    }

    public PersistentList arguments() {
        return this.arguments;
    }

    public Message next() {
        return this.next;
    }

    public Message withNext(Message newNext) {
        return new NamedMessage(this.name, this.arguments, newNext, filename, line, position);
    }

    public boolean isLiteral() {
        return false;
    }

    public SephObject literal() {
        return null;
    }

    public String filename() {
        return this.filename;
    }

    public int line() {
        return this.line;
    }

    public int position() {
        return this.position;
    }
}// NamedMessage
