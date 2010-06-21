package seph.lang.persistent;

import java.util.Collection;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class LazilyPersistentVector {
    static public IPersistentVector createOwning(Object... items) {
        if(items.length == 0)
            return PersistentVector.EMPTY;
        else if(items.length <= 32)
            return new PersistentVector(items.length, 5, PersistentVector.EMPTY_NODE,items);
        return PersistentVector.create(items);
    }

    static public IPersistentVector create(Collection coll) {
        if(!(coll instanceof ISeq) && coll.size() <= 32)
            return createOwning(coll.toArray());
        return PersistentVector.create(RT.seq(coll));
    }
}
