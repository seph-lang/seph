/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.persistent.RT;
import seph.lang.persistent.ISeq;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class ControlDefaultBehavior implements SephObject {
    public final static ControlDefaultBehavior instance = new ControlDefaultBehavior();

    public static SephObject evaluateArgument(Object possibleArgument, LexicalScope scope, SThread thread, boolean fully) {
        try {
            MethodHandle mh = (MethodHandle)possibleArgument;
            return (SephObject)mh.invoke(thread, scope, true, fully);
        } catch(Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @SephMethod(name="if", evaluateArguments=false)
    public final static SephObject _if(SThread thread, LexicalScope scope, MethodHandle condition, MethodHandle then, MethodHandle _else) {
        // new Exception().printStackTrace();
        SephObject result = evaluateArgument(condition, scope, thread, true);

        if(result.isTrue()) {
            if(null != then) {
                return evaluateArgument(then, scope, thread, false);
            } else {
                return Runtime.NIL;
            }
        } else {
            if(null != _else) {
                return evaluateArgument(_else, scope, thread, false);
            } else {
                return Runtime.NIL;
            }
        }
    }

    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.ControlDefaultBehaviorBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.ControlDefaultBehaviorBase.IDENTITY;
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
}// ControlDefaultBehavior
