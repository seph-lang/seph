package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface Lookup {
    Object valueAt(Object key);
    Object valueAt(Object key, Object notFound);
}
