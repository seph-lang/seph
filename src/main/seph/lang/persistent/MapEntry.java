package seph.lang.persistent;

import java.util.Iterator;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class MapEntry extends AMapEntry {
    final Object _key;
    final Object _val;

    public MapEntry(Object key, Object val) {
        this._key = key;
        this._val = val;
    }

    public Object key() {
        return _key;
    }
    
    public Object val() {
        return _val;
    }

    public Object getKey() {
        return key();
    }

    public Object getValue() {
        return val();
    }
}
