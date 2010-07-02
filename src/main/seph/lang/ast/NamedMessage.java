/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class NamedMessage implements Message, SephObject {
    private final String name;
    private final IPersistentList arguments;
    private final Message next;

    private final String filename;
    private final int line;
    private final int position;

    final static IPersistentList NO_ARGUMENTS = PersistentList.EMPTY;
    
    public NamedMessage(String name, IPersistentList arguments, Message next, String filename, int line, int position) {
        this.name = name == null ? null : name.intern();
        this.arguments = arguments == null ? NO_ARGUMENTS : arguments;
        this.next = next;
        this.filename = filename;
        this.line = line;
        this.position = position;
    }

    public String name() {
        return this.name;
    }

    public IPersistentList arguments() {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        if(arguments.seq() != null) {
            sb.append("(");
            String sep = "";
            for(ISeq seq = arguments.seq(); seq != null; seq = seq.next()) {
                sb.append(sep).append(seq.first());
                sep = ", ";
            }
            sb.append(")");
        }
        if(next != null) {
            sb.append(" ").append(next);
        }
        return sb.toString();
    }

    public SephObject sendTo(SephObject receiver, Runtime runtime) {
        SephObject value = receiver.get(name);
        if(value == null) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }

        if(value.isActivatable()) {
            return value.activateWith(receiver, arguments);
        }
        return value;
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
}// NamedMessage
