/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents={"Base", "DefaultBehavior"})
public class SephGround implements SephObject {
    public final static SephGround instance = new SephGround();

    public final SephObject parent1 = Base.instance;
    public final SephObject parent2 = DefaultBehavior.instance;


    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.SephGroundBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.SephGroundBase.IDENTITY;
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
}// SephGround
