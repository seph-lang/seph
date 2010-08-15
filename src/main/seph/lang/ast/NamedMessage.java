/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

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
public class NamedMessage implements Message, SephObject {
    private final String name;
    private final IPersistentList arguments;
    private final Message next;

    private final String filename;
    private final int line;
    private final int position;

    final static IPersistentList NO_ARGUMENTS = PersistentList.EMPTY;

    public final static NamedMessage create(String name, IPersistentList arguments, Message next, String filename, int line, int position) {
        if(name == null) {
            return new NamedMessage(null, arguments, next, filename, line, position);
        }

        if(name.equals(".")) {
            return new Terminator(name, arguments, next, filename, line, position);
        } else if(Parser.DEFAULT_ASSIGNMENT_OPERATORS.containsKey(name)) {
            return new Assignment(name, arguments, next, filename, line, position);
        } else if(name.equals("#")) {
            return new Abstraction(name, arguments, next, filename, line, position);
        } else {
            return new NamedMessage(name, arguments, next, filename, line, position);
        }
    }

    NamedMessage(String name, IPersistentList arguments, Message next, String filename, int line, int position) {
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
        return NamedMessage.create(this.name, this.arguments, newNext, filename, line, position);
    }

    public Message withArguments(IPersistentList args) {
        return NamedMessage.create(this.name, args, this.next, filename, line, position);
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

    public boolean equals(Object other) {
        boolean ret = this == other;
        if(!ret && other != null && (other instanceof NamedMessage)) {
            NamedMessage lm = (NamedMessage)other;
            ret = (this.name == null ? lm.name == null : this.name.equals(lm.name)) &&
                (this.arguments == null ? lm.arguments == null : this.arguments.equals(lm.arguments)) &&
                (this.next == null ? lm.next == null : this.next.equals(lm.next));
        }
        return ret;
    }

    public SephObject sendTo(SThread thread, LexicalScope scope, SephObject receiver) {
        if(next == null) {
            thread.next = this;
            thread.nextScope = scope;
            thread.nextReceiver = receiver;
            return SThread.TAIL_MARKER;
        }

        return go(thread, scope, receiver);
    }

    public SephObject go(SThread thread, LexicalScope scope, SephObject receiver) {
        SephObject value = scope.get(name);

        if(null == value) {
            value = receiver.get(name);
        }

        if(null == value) {
            throw new RuntimeException(" *** couldn't find: " + name + " on " + receiver);
        }

        if(value.isActivatable()) {
            return value.activateWith(thread, scope, receiver, arguments);
        }

        return value;
    }

    public SephObject get(String cellName) {
        return null;
    }

    public boolean isActivatable() {
        return false;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(SThread thread, LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// NamedMessage
