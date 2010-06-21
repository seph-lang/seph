package seph.lang.persistent;

import java.util.*;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public interface IMapEntry extends Map.Entry {
    Object key();
    Object val();
}
