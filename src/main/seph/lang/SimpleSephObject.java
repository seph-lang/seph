/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SimpleSephObject implements SephObject {
    final IPersistentMap meta;
    final IPersistentMap cells;

    public SimpleSephObject(IPersistentMap meta, IPersistentMap cells) {
        this.meta = meta;
        this.cells = cells;
    }

    public SimpleSephObject(IPersistentMap meta) {
        this(meta, PersistentArrayMap.EMPTY);
    }
    
    public SimpleSephObject() {
        this(PersistentArrayMap.EMPTY, PersistentArrayMap.EMPTY);
    }

    final public IPersistentMap meta(){
        return meta;
    }

    public SephObject get(String cellName) {
        return (SephObject)cells.valueAt(cellName);
    }

    public final static Symbol activatable = new Symbol();
    
    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }

    public SephObject activateWith(SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// SimpleSephObject
