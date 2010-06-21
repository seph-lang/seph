package seph.lang.persistent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import seph.lang.SephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class PersistentArrayMap extends APersistentMap implements SephObject, EditableCollection {
    final Object[] array;
    static final int HASHTABLE_THRESHOLD = 16;

    public static final PersistentArrayMap EMPTY = new PersistentArrayMap();
    private final PersistentMap _meta;

    static public PersistentMap create(Map other) {
        ITransientMap ret = EMPTY.asTransient();
        for(Object o : other.entrySet()) {
            Map.Entry e = (Entry) o;
            ret = ret.associate(e.getKey(), e.getValue());
        }
        return ret.persistent();
    }

    protected PersistentArrayMap() {
        this.array = new Object[]{};
        this._meta = null;
    }

    public PersistentArrayMap withMeta(PersistentMap meta) {
        return new PersistentArrayMap(meta, array);
    }

    PersistentArrayMap create(Object... init) {
        return new PersistentArrayMap(meta(), init);
    }

    PersistentMap createHT(Object[] init) {
        return PersistentHashMap.create(meta(), init);
    }

    static public PersistentArrayMap createWithCheck(Object[] init) {
        for(int i=0;i< init.length;i += 2) {
            for(int j=i+2;j<init.length;j += 2) {
                if(equalKey(init[i],init[j]))
                    throw new IllegalArgumentException("Duplicate key: " + init[i]);
            }
        }
        return new PersistentArrayMap(init);
    }

    /**
     * This ctor captures/aliases the passed array, so do not modify later
     *
     * @param init {key1,val1,key2,val2,...}
     */
    public PersistentArrayMap(Object[] init) {
        this.array = init;
        this._meta = null;
    }


    public PersistentArrayMap(PersistentMap meta, Object[] init) {
        this._meta = meta;
        this.array = init;
    }

    public int count(){
        return array.length / 2;
    }

    public boolean containsKey(Object key) {
        return indexOf(key) >= 0;
    }

    public IMapEntry entryAt(Object key) {
        int i = indexOf(key);
        if(i >= 0)
            return new MapEntry(array[i],array[i+1]);
        return null;
    }

    public PersistentMap associateOrFail(Object key, Object val) throws Exception {
        int i = indexOf(key);
        Object[] newArray;
        if(i >= 0) {
            throw new Exception("Key already present");
        }
        else {
            if(array.length > HASHTABLE_THRESHOLD)
                return createHT(array).associateOrFail(key, val);
            newArray = new Object[array.length + 2];
            if(array.length > 0)
                System.arraycopy(array, 0, newArray, 2, array.length);
            newArray[0] = key;
            newArray[1] = val;
        }
        return create(newArray);
    }

    public PersistentMap associate(Object key, Object val) {
        int i = indexOf(key);
        Object[] newArray;
        if(i >= 0) {
            if(array[i + 1] == val) //no change, no op
                return this;
            newArray = array.clone();
            newArray[i + 1] = val;
        } else {
            if(array.length > HASHTABLE_THRESHOLD)
                return createHT(array).associate(key, val);
            newArray = new Object[array.length + 2];
            if(array.length > 0)
                System.arraycopy(array, 0, newArray, 2, array.length);
            newArray[0] = key;
            newArray[1] = val;
        }
        return create(newArray);
    }

    public PersistentMap without(Object key) {
        int i = indexOf(key);
        if(i >= 0) {
            int newlen = array.length - 2;
            if(newlen == 0)
                return empty();
            Object[] newArray = new Object[newlen];
            for(int s = 0, d = 0; s < array.length; s += 2) {
                if(!equalKey(array[s], key)) {
                    newArray[d] = array[s];
                    newArray[d + 1] = array[s + 1];
                    d += 2;
                }
            }
            return create(newArray);
        }
        return this;
    }

    public PersistentMap empty() {
        return (PersistentMap) EMPTY.withMeta(meta());
    }

    final public Object valueAt(Object key, Object notFound) {
        int i = indexOf(key);
        if(i >= 0)
            return array[i + 1];
        return notFound;
    }

    public Object valueAt(Object key) {
        return valueAt(key, null);
    }

    public int capacity() {
        return count();
    }

    private int indexOf(Object key) {
        for(int i = 0; i < array.length; i += 2) {
            if(equalKey(array[i], key))
                return i;
        }
        return -1;
    }

    static boolean equalKey(Object k1, Object k2) {
        if(k1 == null)
            return k2 == null;
        return k1.equals(k2);
    }

    public Iterator iterator(){
        return new Iter(array);
    }

    public ISeq seq(){
        if(array.length > 0)
            return new MapSeq(array, 0);
        return null;
    }

    public PersistentMap meta(){
        return _meta;
    }

    static class MapSeq extends Seq implements Counted {
        final Object[] array;
        final int i;

        MapSeq(Object[] array, int i) {
            this.array = array;
            this.i = i;
        }

        public MapSeq(PersistentMap meta, Object[] array, int i) {
            super(meta);
            this.array = array;
            this.i = i;
        }

        public Object first() {
            return new MapEntry(array[i],array[i+1]);
        }

        public ISeq next() {
            if(i + 2 < array.length)
                return new MapSeq(array, i + 2);
            return null;
        }

        public int count() {
            return (array.length - i) / 2;
        }

        public MapSeq withMeta(PersistentMap meta) {
            return new MapSeq(meta, array, i);
        }
    }

    static class Iter implements Iterator {
        Object[] array;
        int i;

        //for iterator
        Iter(Object[] array) {
            this(array, -2);
        }

        //for entryAt
        Iter(Object[] array, int i) {
            this.array = array;
            this.i = i;
        }

        public boolean hasNext() {
            return i < array.length - 2;
        }

        public Object next() {
            i += 2;
            return new MapEntry(array[i],array[i+1]);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public ITransientMap asTransient() {
        return new TransientArrayMap(array);
    }

    static final class TransientArrayMap extends ATransientMap {
        int len;
        final Object[] array;
        Thread owner;

        public TransientArrayMap(Object[] array) {
            this.owner = Thread.currentThread();
            this.array = new Object[Math.max(HASHTABLE_THRESHOLD, array.length)];
            System.arraycopy(array, 0, this.array, 0, array.length);
            this.len = array.length;
        }
	
        private int indexOf(Object key) {
            for(int i = 0; i < len; i += 2) {
                if(equalKey(array[i], key))
                    return i;
            }
            return -1;
        }

        ITransientMap doAssoc(Object key, Object val) {
            int i = indexOf(key);
            if(i >= 0) {
                if(array[i + 1] != val) //no change, no op
                    array[i + 1] = val;
            } else {
                if(len >= array.length)
                    return PersistentHashMap.create(array).asTransient().associate(key, val);
                array[len++] = key;
                array[len++] = val;
            }
            return this;
        }

        ITransientMap doWithout(Object key) {
            int i = indexOf(key);
            if(i >= 0) {
                if(len >= 2) {
                    array[i] = array[len - 2];
                    array[i + 1] = array[len - 1];
                }
                len -= 2;
            }
            return this;
        }

        Object doValAt(Object key, Object notFound) {
            int i = indexOf(key);
            if (i >= 0)
                return array[i + 1];
            return notFound;
        }

        int doCount() {
            return len / 2;
        }
	
        PersistentMap doPersistent() {
            ensureEditable();
            owner = null;
            Object[] a = new Object[len];
            System.arraycopy(array,0,a,0,len);
            return new PersistentArrayMap(a);
        }

        void ensureEditable() {
            if(owner == Thread.currentThread())
                return;
            if(owner != null)
                throw new IllegalAccessError("Transient used by non-owner thread");
            throw new IllegalAccessError("Transient used after persistent! call");
        }
    }
}
