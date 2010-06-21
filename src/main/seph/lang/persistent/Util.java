package seph.lang.persistent;

import java.math.BigInteger;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class Util {
    static public boolean equiv(Object k1, Object k2){
        if(k1 == k2) {
            return true;
        }
        if(k1 != null) {
            if(k1 instanceof PersistentCollection && k2 instanceof PersistentCollection) {
                return ((PersistentCollection)k1).equiv(k2);
            }
            return k1.equals(k2);
		}
        return false;
    }

    static public boolean equals(Object k1, Object k2){
        if(k1 == k2)
            return true;
        return k1 != null && k1.equals(k2);
    }

    static public Object ret1(Object ret, Object nil){
		return ret;
    }

    static public ISeq ret1(ISeq ret, Object nil){
		return ret;
    }

    static public boolean isInteger(Object x){
        return x instanceof Integer
			|| x instanceof Long
			|| x instanceof BigInteger;
    }

    static public int compare(Object k1, Object k2) {
        if(k1 == k2)
            return 0;
        if(k1 != null) {
            if(k2 == null)
                return 1;
            if(k1 instanceof Number)
                return Numbers.compare((Number) k1, (Number) k2);
            return ((Comparable) k1).compareTo(k2);
		}
        return -1;
    }
}

