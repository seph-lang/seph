/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;

import static seph.lang.SimpleSephObject.activatable;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SephMethodObject implements SephObject {
    final IPersistentMap meta;

    public SephMethodObject() {
        meta = new PersistentArrayMap(new Object[]{activatable, Runtime.TRUE});
    }

    public SephObject get(String cellName) {
        return null;
    }

    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }
}// SephMethodObject
