package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface Indexed extends Counted {
    Object at(int i);
    Object at(int i, Object notFound);
}
