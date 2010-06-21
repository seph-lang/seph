package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface ITransientVector extends ITransientAssociative, Indexed {
    ITransientVector assocN(int i, Object val);
    ITransientVector pop();
}
