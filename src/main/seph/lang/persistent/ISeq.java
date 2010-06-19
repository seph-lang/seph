package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface ISeq extends PersistentCollection, Sequential {
    Object first();
    ISeq next();
    ISeq more();
    ISeq cons(Object o);
}

