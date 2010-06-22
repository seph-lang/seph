package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface ITransientMap extends ITransientAssociative, Counted {
    ITransientMap associate(Object key, Object val);
    ITransientMap without(Object key);
    IPersistentMap persistent();
}
