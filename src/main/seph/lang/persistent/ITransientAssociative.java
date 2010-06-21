package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface ITransientAssociative extends ITransientCollection, Lookup {
    ITransientAssociative associate(Object key, Object val);
}
