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

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MessageInterpreter {
    private final SephObject ground;
    private final LexicalScope scope;

    public MessageInterpreter(final SephObject ground, Runtime runtime) {
        this.ground = ground;
        this.scope = new LexicalScope(this, runtime);
    }

    public MessageInterpreter(final SephObject ground, final LexicalScope parent, Runtime runtime) {
        this.ground = ground;
        this.scope = new LexicalScope(this, parent, runtime);
    }

    public LexicalScope newScope(SephObject ground) {
        return new MessageInterpreter(ground, scope, scope.runtime).scope;
    }

    public Object evaluate(SThread thread, Message msg) {
        SephObject receiver = ground;
        SephObject lastReal = Runtime.NIL;
        Message currentMessage = msg;
        boolean first = true;

        try {
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
                            MethodHandle tail = thread.tail;
                            thread.tail = null;
                            tmp = (SephObject)tail.invokeExact();
                        }
                    }

                    if(tmp != null) {
                        receiver = lastReal = tmp;
                    }
                }
                currentMessage = next;
            }
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }

        return lastReal;
    }

    public Object evaluateFully(SThread thread, Message msg) {
        SephObject receiver = ground;
        SephObject lastReal = Runtime.NIL;
        Message currentMessage = msg;
        boolean first = true;

        try {
            while(currentMessage != null) {
                String name = currentMessage.name();

                if(currentMessage instanceof Terminator) {
                    receiver = ground;
                    first = true;
                } else {
                    SephObject tmp = currentMessage.sendTo(thread, scope, receiver, first);
                    first = false;
                    while(tmp == SThread.TAIL_MARKER) {
                        MethodHandle tail = thread.tail;
                        thread.tail = null;
                        tmp = (SephObject)tail.invokeExact();
                    }

                    if(tmp != null) {
                        receiver = lastReal = tmp;
                    }
                }
                currentMessage = currentMessage.next();
            }
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
        
        return lastReal;
    }
}// MessageInterpreter
