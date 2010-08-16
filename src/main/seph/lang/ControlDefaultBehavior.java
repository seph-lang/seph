/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.RT;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class ControlDefaultBehavior implements SephObject {
    public final static ControlDefaultBehavior instance = new ControlDefaultBehavior();

    @SephMethod(name="if", evaluateArguments=false)
    public final static SephObject _if(SThread thread, LexicalScope scope, IPersistentList arguments) {
        Message conditional = (Message)RT.first(arguments);
        SephObject result = scope.evaluateFully(thread, conditional);
        if(result.isTrue()) {
            ISeq seq = RT.next(arguments);
            if(null != seq) {
                return scope.evaluate(thread, (Message)RT.first(seq));
            } else {
                return Runtime.NIL;
            }
        } else {
            ISeq seq = RT.next(RT.next(arguments));
            if(null != seq) {
                return scope.evaluate(thread, (Message)RT.first(seq));
            } else {
                return Runtime.NIL;
            }
        }
    }

    public SephObject get(String cellName) {
        return seph.lang.bim.ControlDefaultBehaviorBase.get(cellName);
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
}// ControlDefaultBehavior
