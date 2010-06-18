package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
/* A class that implements Counted promises that it is a collection
 * that implement a constant-time count() */
public interface Counted {
    int count();
}
