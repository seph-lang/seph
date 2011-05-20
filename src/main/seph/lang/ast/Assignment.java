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

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public final class Assignment extends NamedMessage {
    public static enum ActualAssignment {
        EQ {
            public SephObject assignOp(SThread thread, Message left, Message right, LexicalScope scope) {
                SephObject value = scope.evaluateFully(thread, right);
                scope.assign(left.name(), value);
                return value;
            }
        },
        PLUS_EQ {
            public SephObject assignOp(SThread thread, Message left, Message right, LexicalScope scope) {
                SephObject leftValue = scope.evaluateFully(thread, left);
                SephObject value = NamedMessage.create("+", new PersistentList(right), null, left.filename(), left.line(), left.position(), null).go(thread, scope, leftValue, false);
                scope.assign(left.name(), value);
                return value;
            }
        };

        public abstract SephObject assignOp(SThread thread, Message left, Message right, LexicalScope scope);
    }

    private ActualAssignment assgn;
    private final StaticScope scope;
    private final int place;

    public Assignment(String name, IPersistentList arguments, Message next, String filename, int line, int position, StaticScope scope) {
        super(name, arguments, next, filename, line, position);

        if(name.equals("=")) {
            assgn = ActualAssignment.EQ;
        } else if(name.equals("+=")) {
            assgn = ActualAssignment.PLUS_EQ;
        } else {
            assgn = null;
        }

        this.scope = scope;
        this.place = scope.addOrFindName(((Message)arguments().seq().first()).name());
        // System.err.println("Found the place in: " + scope);
        // System.err.println("  depth: " + (place >> 16));
        // System.err.println("  index: " + (place & 0xFFFF));
    }

    public ActualAssignment getAssignment() {
        return assgn;
    }

    @Override
    public Message withNext(Message newNext) {
        return NamedMessage.create(this.name, this.arguments, newNext, filename, line, position, scope);
    }

    @Override
    public Message withArguments(IPersistentList args) {
        return NamedMessage.create(this.name, args, this.next, filename, line, position, scope);
    }

    @Override
    public SephObject sendTo(SThread thread, LexicalScope scope, SephObject receiver, boolean first) {
        Message left = (Message)arguments().seq().first();
        Message right = (Message)arguments().seq().next().first();
        return assgn.assignOp(thread, left, right, scope);
    }
}// Assignment
