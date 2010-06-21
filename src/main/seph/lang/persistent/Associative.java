package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface Associative extends PersistentCollection, Lookup {
    boolean containsKey(Object key);
    IMapEntry entryAt(Object key);
    Associative associate(Object key, Object val);
}
