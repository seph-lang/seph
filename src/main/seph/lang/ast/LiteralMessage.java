/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
import seph.lang.SThread;
import seph.lang.persistent.IPersistentList;
import seph.lang.ActivationHelpers;

import java.lang.invoke.MethodHandle;

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

    public Message withArguments(IPersistentList args) {
        return this;
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

    @Override
    public String toString() {
        return literal + (next == null ? "" : " " + next.toString());
    }

    @Override
    public boolean equals(Object other) {
        boolean ret = this == other;
        if(!ret && other != null && (other instanceof LiteralMessage)) {
            LiteralMessage lm = (LiteralMessage)other;
            ret = (this.literal == null ? lm.literal == null : this.literal.equals(lm.literal)) &&
                (this.next == null ? lm.next == null : this.next.equals(lm.next));
        }
        return ret;
    }

    @Override
    public SephObject get(String cellName) {
        return null;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        return ActivationHelpers.noActivateFor(this, arity, keywords);
    }

    @Override
    public Object identity() {
        return this;
    }
}// LiteralMessage
