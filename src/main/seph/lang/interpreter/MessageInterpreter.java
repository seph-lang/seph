/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.interpreter;

import seph.lang.Runtime;
import seph.lang.SephObject;

import seph.lang.ast.Message;
import seph.lang.ast.LiteralMessage;
import seph.lang.ast.NamedMessage;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MessageInterpreter {
    private final Runtime runtime;
    private final SephObject ground;

    public MessageInterpreter(final Runtime runtime, final SephObject ground) {
        this.runtime = runtime;
        this.ground = ground;
    }

    public Object evaluate(Message msg) {
        SephObject receiver = ground;
        Message currentMessage = msg;

        while(currentMessage != null) {
            receiver = currentMessage.sendTo(receiver, runtime);
            currentMessage = currentMessage.next();

            if(currentMessage != null && currentMessage.name().equals(".")) {
                receiver = ground;
                currentMessage = currentMessage.next();
            }
        }

        return receiver;
    }
}// MessageInterpreter
