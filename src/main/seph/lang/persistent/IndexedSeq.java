package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IndexedSeq extends ISeq, Counted {
    public int index();
}
