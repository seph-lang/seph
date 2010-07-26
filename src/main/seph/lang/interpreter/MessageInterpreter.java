/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.interpreter;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.LexicalScope;

import seph.lang.ast.Message;
import seph.lang.ast.LiteralMessage;
import seph.lang.ast.NamedMessage;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MessageInterpreter {
    private final Runtime runtime;
    private final SephObject ground;
    private final LexicalScope scope;

    public MessageInterpreter(final Runtime runtime, final SephObject ground) {
        this.runtime = runtime;
        this.ground = ground;
        this.scope = new LexicalScope(this);
    }

    public MessageInterpreter(final Runtime runtime, final SephObject ground, final LexicalScope parent) {
        this.runtime = runtime;
        this.ground = ground;
        this.scope = new LexicalScope(this, parent);
    }

    public LexicalScope newScope(SephObject ground) {
        return new MessageInterpreter(this.runtime, ground, scope).scope;
    }

    public Object evaluate(Message msg) {
        SephObject receiver = ground;
        SephObject lastReal = Runtime.NIL;
        Message currentMessage = msg;

        while(currentMessage != null) {
            String name = currentMessage.name();

            if(name.equals(".")) {
                receiver = ground;
            } else {
                SephObject tmp = currentMessage.sendTo(scope, receiver, runtime);
                if(tmp != null) {
                    lastReal = receiver = tmp;
                }
            }
            currentMessage = currentMessage.next();
        }

        return lastReal;
    }
}// MessageInterpreter
