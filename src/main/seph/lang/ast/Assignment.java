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
public final class Assignment extends NamedMessage {
    private static enum ActualAssignment {
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
                SephObject value = NamedMessage.create("+", new PersistentList(right), null, left.filename(), left.line(), left.position()).go(thread, scope, leftValue);
                scope.assign(left.name(), value);
                return value;
            }
        };

        public abstract SephObject assignOp(SThread thread, Message left, Message right, LexicalScope scope);
    }

    private ActualAssignment assgn;

    public Assignment(String name, IPersistentList arguments, Message next, String filename, int line, int position) {
        super(name, arguments, next, filename, line, position);

        if(name.equals("=")) {
            assgn = ActualAssignment.EQ;
        } else if(name.equals("+=")) {
            assgn = ActualAssignment.PLUS_EQ;
        } else {
            assgn = null;
        }
    }

    @Override
    public SephObject sendTo(SThread thread, LexicalScope scope, SephObject receiver) {
        Message left = (Message)arguments().seq().first();
        Message right = (Message)arguments().seq().next().first();
        return assgn.assignOp(thread, left, right, scope);
    }
}// Assignment
