/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.SephObject;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class LiteralMessage implements Message, SephObject {
    private final SephObject literal;
    private final Message next;

    public LiteralMessage(SephObject literal, Message next) {
        this.literal = literal;
        this.next = next;
    }

    public String name() {
        return "<literal:" + literal.getClass().getName() + ">";
    }

    public Message[] arguments() {
        return NamedMessage.NO_ARGUMENTS;
    }

    public Message next() {
        return this.next;
    }

    public Message withNext(Message newNext) {
        return new LiteralMessage(this.literal, newNext);
    }

    public boolean isLiteral() {
        return true;
    }

    public SephObject literal() {
        return this.literal;
    }
}// LiteralMessage
