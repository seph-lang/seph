/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Message;
import seph.lang.interpreter.MessageInterpreter;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LexicalScope {
    private final MessageInterpreter currentInterpreter;

    public LexicalScope(MessageInterpreter currentInterpreter) {
        this.currentInterpreter = currentInterpreter;
    }

    public SephObject evaluate(Message message) {
        return (SephObject)this.currentInterpreter.evaluate(message);
    }
}// LexicalScope
