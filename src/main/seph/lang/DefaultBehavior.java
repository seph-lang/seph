/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents={"IODefaultBehavior", "ControlDefaultBehavior"})
public class DefaultBehavior implements SephObject {
    public final static DefaultBehavior instance = new DefaultBehavior();

    public final SephObject parent1 = IODefaultBehavior.instance;
    public final SephObject parent2 = ControlDefaultBehavior.instance;

    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.DefaultBehaviorBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.DefaultBehaviorBase.IDENTITY;
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

    @SephMethod(name="")
    public final static SephObject blank(SephObject receiver, SephObject arg) {
        return arg;
    }
}// DefaultBehavior
