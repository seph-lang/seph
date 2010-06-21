package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class Box {
    public Object val;
    public Box(Object val) {
        this.val = val;
    }
}
