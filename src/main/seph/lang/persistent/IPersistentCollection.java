package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IPersistentCollection extends Seqable {
    int count();
    IPersistentCollection cons(Object o);
    IPersistentCollection empty();
    boolean equiv(Object o);
}
