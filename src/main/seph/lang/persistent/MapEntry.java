package seph.lang.persistent;

import java.util.Map;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface MapEntry extends Map.Entry {
    Object key();
    Object val();
}
