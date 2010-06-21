package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface ITransientCollection {
    ITransientCollection conj(Object val);
    PersistentCollection persistent();
}
