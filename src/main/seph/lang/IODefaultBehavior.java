/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.NamedMessage;
import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class IODefaultBehavior implements SephObject {
    public final static IODefaultBehavior instance = new IODefaultBehavior();

    @SephMethod
    public final static SephObject println(SThread thread, SephObject receiver) {
        SephObject asText = receiver.get("asText");
        if(asText.isActivatable()) {
            try {
                asText = (SephObject)asText.activationFor(0, false).invokeExact(receiver, thread, (LexicalScope)null);
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
        System.out.println(((Text)asText).text());
        return Runtime.NIL;
    }

    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.IODefaultBehaviorBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.IODefaultBehaviorBase.IDENTITY;
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
}// IODefaultBehavior
