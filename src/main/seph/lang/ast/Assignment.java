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
        EQ, PLUS_EQ;
    }

    private ActualAssignment assgn;
    private StaticScope scope;
    public Assignment(String name, IPersistentList arguments, Message next, String filename, int line, int position, StaticScope scope) {
        super(name, arguments, next, filename, line, position);

        if(name.equals("=")) {
            assgn = ActualAssignment.EQ;
        } else if(name.equals("+=")) {
            assgn = ActualAssignment.PLUS_EQ;
        } else {
            assgn = null;
        }
        
        scope.addName(((Message)arguments.seq().first()).name());
        this.scope = scope;
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
}// Assignment
