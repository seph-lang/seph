/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.interpreter;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.LexicalScope;
import seph.lang.SThread;

import seph.lang.ast.Message;
import seph.lang.ast.LiteralMessage;
import seph.lang.ast.NamedMessage;
import seph.lang.ast.Terminator;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MessageInterpreter {
    private final SephObject ground;
    private final LexicalScope scope;

    public MessageInterpreter(final SephObject ground) {
        this.ground = ground;
        this.scope = new LexicalScope(this);
    }

    public MessageInterpreter(final SephObject ground, final LexicalScope parent) {
        this.ground = ground;
        this.scope = new LexicalScope(this, parent);
    }

    public LexicalScope newScope(SephObject ground) {
        return new MessageInterpreter(ground, scope).scope;
    }

    public Object evaluate(SThread thread, Message msg) {
        SephObject receiver = ground;
        SephObject lastReal = Runtime.NIL;
        Message currentMessage = msg;
        boolean first = true;

        while(currentMessage != null) {
            String name = currentMessage.name();

            Message next = currentMessage.next();
            
            if(currentMessage instanceof Terminator) {
                receiver = ground;
                first = true;
            } else {
                SephObject tmp = currentMessage.sendTo(thread, scope, receiver, first);
                first = false;
                if(next != null) {
                    while(tmp == SThread.TAIL_MARKER) {
                        tmp = thread.next.go(thread, thread.nextScope, thread.nextReceiver, thread.first);
                        if(thread.nextTail != null) {
                            tmp = thread.nextTail.goTail(thread);
                        } else {
                            tmp = thread.next.go(thread, thread.nextScope, thread.nextReceiver, thread.first);
                        }
                    }
                }

                if(tmp != null) {
                    receiver = lastReal = tmp;
                }
            }
            currentMessage = next;
        }

        return lastReal;
    }

    public Object evaluateFully(SThread thread, Message msg) {
        SephObject receiver = ground;
        SephObject lastReal = Runtime.NIL;
        Message currentMessage = msg;
        boolean first = true;

        while(currentMessage != null) {
            String name = currentMessage.name();

            if(currentMessage instanceof Terminator) {
                receiver = ground;
                first = true;
            } else {
                SephObject tmp = currentMessage.sendTo(thread, scope, receiver, first);
                first = false;
                while(tmp == SThread.TAIL_MARKER) {
                    if(thread.nextTail != null) {
                        tmp = thread.nextTail.goTail(thread);
                    } else {
                        tmp = thread.next.go(thread, thread.nextScope, thread.nextReceiver, thread.first);
                    }
                }

                if(tmp != null) {
                    receiver = lastReal = tmp;
                }
            }
            currentMessage = currentMessage.next();
        }

        return lastReal;
    }
}// MessageInterpreter
