/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class LiteralMessage implements Message, SephObject {
    private final SephObject literal;
    private final Message next;

    private final String filename;
    private final int line;
    private final int position;

    public LiteralMessage(SephObject literal, Message next, String filename, int line, int position) {
        this.literal = literal;
        this.next = next;
        this.filename = filename;
        this.line = line;
        this.position = position;
    }

    public String name() {
        return "<literal:" + literal.getClass().getName() + ">";
    }

    public IPersistentList arguments() {
        return NamedMessage.NO_ARGUMENTS;
    }

    public Message next() {
        return this.next;
    }

    public Message withNext(Message newNext) {
        return new LiteralMessage(this.literal, newNext, filename, line, position);
    }

    public boolean isLiteral() {
        return true;
    }

    public SephObject literal() {
        return this.literal;
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

    public String toString() {
        return literal + (next == null ? "" : " " + next.toString());
    }

    public SephObject sendTo(SephObject receiver, Runtime runtime) {
        return literal;
    }


    public SephObject get(String cellName) {
        return null;
    }

    public boolean isActivatable() {
        return false;
    }

    public SephObject activateWith(SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// LiteralMessage
