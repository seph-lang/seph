package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IPersistentVector extends Associative, Sequential, Indexed {
    int length();
    IPersistentVector assocN(int i, Object val);
    IPersistentVector cons(Object o);
}
