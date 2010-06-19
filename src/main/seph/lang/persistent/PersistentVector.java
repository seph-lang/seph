package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface PersistentVector extends Associative, Sequential, PersistentCollection, Indexed {
    int length();
    PersistentVector assocAt(int i, Object val);
    PersistentVector cons(Object o);
}
