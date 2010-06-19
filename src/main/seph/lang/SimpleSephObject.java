/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.PersistentMap;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SimpleSephObject implements SephObject {
    final PersistentMap meta;

    public SimpleSephObject(PersistentMap meta){
        this.meta = meta;
    }
    
    public SimpleSephObject(){
        meta = null;
    }

    final public PersistentMap meta(){
        return meta;
    }
    
    abstract public SimpleSephObject withMeta(PersistentMap meta);
}// SimpleSephObject
