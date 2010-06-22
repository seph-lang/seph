package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IPersistentMap extends Iterable, Associative, Counted {
    IPersistentMap associate(Object key, Object val);
    IPersistentMap associateOrFail(Object key, Object val) throws Exception;
    IPersistentMap without(Object key) throws Exception;
}
