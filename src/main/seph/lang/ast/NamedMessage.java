/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.List;

import seph.lang.SephObject;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class NamedMessage implements Message, SephObject {
    private final String name;
    private final List<Message> arguments;
    private final Message next;

    final static List<Message> NO_ARGUMENTS = java.util.Arrays.<Message>asList();
    
    public NamedMessage(String name, List<Message> arguments, Message next) {
        this.name = name;
        this.arguments = arguments == null ? NO_ARGUMENTS : arguments;
        this.next = next;
    }

    public String name() {
        return this.name;
    }

    public List<Message> arguments() {
        return this.arguments;
    }

    public Message next() {
        return this.next;
    }

    public Message withNext(Message newNext) {
        return new NamedMessage(this.name, this.arguments, newNext);
    }

    public boolean isLiteral() {
        return false;
    }

    public SephObject literal() {
        return null;
    }
}// NamedMessage
