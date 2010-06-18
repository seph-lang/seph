package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface PersistentMap extends Iterable, Associative, Counted {
    PersistentMap associate(Object key, Object val);
    PersistentMap associateOrFail(Object key, Object val) throws Exception;
    PersistentMap without(Object key) throws Exception;
}
