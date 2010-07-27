/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.structure;

import seph.lang.*;
import seph.lang.persistent.*;
import static seph.lang.SimpleSephObject.activatable;

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

    public boolean isActivatable() {
        return meta != null && meta.valueAt(activatable) == seph.lang.Runtime.TRUE;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// SephObjectStructure
