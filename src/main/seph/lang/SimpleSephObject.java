/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentMap;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SimpleSephObject implements SephObject {
    final IPersistentMap meta;

    public SimpleSephObject(IPersistentMap meta){
        this.meta = meta;
    }
    
    public SimpleSephObject(){
        meta = null;
    }

    final public IPersistentMap meta(){
        return meta;
    }
    
    abstract public SimpleSephObject withMeta(IPersistentMap meta);
}// SimpleSephObject
