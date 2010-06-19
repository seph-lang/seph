package seph.lang.persistent;

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
}

