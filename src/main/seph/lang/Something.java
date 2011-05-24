/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents="Ground")
public class Something implements SephObject {
    public final static Something instance = new Something();

    public final SephObject parent1 = Ground.instance;

    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.SomethingBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.SomethingBase.IDENTITY;
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
}// Something
