package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface PersistentCollection {
    int count();
    PersistentCollection cons(Object o);
    PersistentCollection empty();
    boolean equiv(Object o);
}
