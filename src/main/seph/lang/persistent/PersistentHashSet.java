package seph.lang.persistent;

import java.util.List;

import seph.lang.SephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class PersistentHashSet extends APersistentSet implements SephObject, EditableCollection {
    static public final PersistentHashSet EMPTY = new PersistentHashSet(null, PersistentHashMap.EMPTY);

    public static PersistentHashSet create(Object... init) {
        PersistentHashSet ret = EMPTY;
        for(int i = 0; i < init.length; i++) {
            ret = (PersistentHashSet) ret.cons(init[i]);
        }
        return ret;
    }

    public static PersistentHashSet create(List init) {
        PersistentHashSet ret = EMPTY;
        for(Object key : init) {
            ret = (PersistentHashSet) ret.cons(key);
        }
        return ret;
    }

    static public PersistentHashSet create(ISeq items) {
        PersistentHashSet ret = EMPTY;
        for(; items != null; items = items.next()) {
            ret = (PersistentHashSet) ret.cons(items.first());
        }
        return ret;
    }

    public static PersistentHashSet createWithCheck(Object... init) {
        PersistentHashSet ret = EMPTY;
        for(int i = 0; i < init.length; i++) {
            ret = (PersistentHashSet) ret.cons(init[i]);
            if(ret.count() != i + 1)
                throw new IllegalArgumentException("Duplicate key: " + init[i]);
        }
        return ret;
    }

    public static PersistentHashSet createWithCheck(List init) {
        PersistentHashSet ret = EMPTY;
        int i=0;
        for(Object key : init) {
            ret = (PersistentHashSet) ret.cons(key);
            if(ret.count() != i + 1)
                throw new IllegalArgumentException("Duplicate key: " + key);		
            ++i;
        }
        return ret;
    }

    static public PersistentHashSet createWithCheck(ISeq items) {
        PersistentHashSet ret = EMPTY;
        for(int i=0; items != null; items = items.next(), ++i) {
            ret = (PersistentHashSet) ret.cons(items.first());
            if(ret.count() != i + 1)
                throw new IllegalArgumentException("Duplicate key: " + items.first());
        }
        return ret;
    }

    PersistentHashSet(IPersistentMap meta, IPersistentMap impl) {
        super(meta, impl);
    }

    public IPersistentSet disjoin(Object key) throws Exception{
        if(contains(key))
            return new PersistentHashSet(meta(),impl.without(key));
        return this;
    }

    public IPersistentSet cons(Object o) {
        if(contains(o))
            return this;
        return new PersistentHashSet(meta(),impl.associate(o,o));
    }

    public IPersistentCollection empty() {
        return EMPTY.withMeta(meta());	
    }

    public PersistentHashSet withMeta(IPersistentMap meta) {
        return new PersistentHashSet(meta, impl);
    }

    public ITransientCollection asTransient() {
        return new TransientHashSet(((PersistentHashMap) impl).asTransient());
    }

    static final class TransientHashSet extends ATransientSet {
        TransientHashSet(ITransientMap impl) {
            super(impl);
        }

        public IPersistentCollection persistent() {
            return new PersistentHashSet(null, impl.persistent());
        }
    }
}
