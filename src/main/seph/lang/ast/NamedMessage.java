/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
import seph.lang.SThread;
import seph.lang.parser.Parser;
import seph.lang.parser.StaticScope;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;
import seph.lang.ActivationHelpers;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class NamedMessage implements Message, SephObject {
    protected final String name;
    protected final IPersistentList arguments;
    protected final Message next;

    protected final String filename;
    protected final int line;
    protected final int position;

    final static IPersistentList NO_ARGUMENTS = PersistentList.EMPTY;

    public final static NamedMessage create(String name, IPersistentList arguments, Message next, String filename, int line, int position, StaticScope scope) {
        if(name == null) {
            return new NamedMessage(null, arguments, next, filename, line, position);
        }

        if(name.equals(".")) {
            return new Terminator(name, arguments, next, filename, line, position);
        } else if(Parser.DEFAULT_ASSIGNMENT_OPERATORS.containsKey(name)) {
            if(scope != null) {
                return new Assignment(name, arguments, next, filename, line, position, scope);
            } else {
                assert false : "Trying to create an abstraction with no scope information...";
                return null;
            }
        } else if(name.equals("#")) {
            if(scope != null) {
                return new Abstraction(name, arguments, next, filename, line, position, scope);
            } else {
                assert false : "Trying to create an abstraction with no scope information...";
                return null;
            }
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
        return NamedMessage.create(this.name, this.arguments, newNext, filename, line, position, null);
    }

    public Message withArguments(IPersistentList args) {
        return NamedMessage.create(this.name, args, this.next, filename, line, position, null);
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

    @Override
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

    @Override
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
}// NamedMessage
