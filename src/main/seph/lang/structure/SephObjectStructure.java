/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;
import static seph.lang.SimpleSephObject.activatable;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SephObjectStructure implements SephObject {
    final IPersistentMap meta;

    public SephObjectStructure(IPersistentMap meta) {
        this.meta = meta;
    }

    final public IPersistentMap meta(){
        return meta;
    }

    @Override
    public boolean isActivatable() {
        return meta != null && meta.valueAt(activatable) == seph.lang.Runtime.TRUE;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        return ActivationHelpers.noActivateFor(this, arity, keywords);
    }

    @Override
    public Object identity() {
        return this;
    }
}// SephObjectStructure
