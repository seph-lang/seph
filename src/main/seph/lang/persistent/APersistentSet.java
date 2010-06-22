package seph.lang.persistent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public abstract class APersistentSet  implements IPersistentSet, Collection, Set, Serializable {
    int _hash = -1;
    final IPersistentMap impl;

    protected APersistentSet(IPersistentMap impl){
        this.impl = impl;
    }

    public boolean contains(Object key) {
        return impl.containsKey(key);
    }

    public Object get(Object key) {
        return impl.valueAt(key);
    }

    public int count() {
        return impl.count();
    }

    public ISeq seq() {
        return RT.keys(impl);
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Set))
            return false;
        Set m = (Set) obj;

        if(m.size() != count() || m.hashCode() != hashCode())
            return false;

        for(Object aM : m) {
            if(!contains(aM))
                return false;
        }
        return true;
    }

    public boolean equiv(Object o) {
        return equals(o);
    }

    public int hashCode() {
        if(_hash == -1) {
            int hash = 0;
            for(ISeq s = seq(); s != null; s = s.next()) {
                Object e = s.first();
                hash +=  Util.hash(e);
            }
            this._hash = hash;
        }
        return _hash;
    }

    public Object[] toArray() {
        return RT.seqToArray(seq());
    }

    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection c) {
        for(Object o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    public Object[] toArray(Object[] a) {
        if(a.length >= count()) {
            ISeq s = seq();
            for(int i = 0; s != null; ++i, s = s.next()) {
                a[i] = s.first();
            }
            if(a.length > count())
                a[count()] = null;
            return a;
        } else
            return toArray();
    }

    public int size() {
        return count();
    }

    public boolean isEmpty() {
        return count() == 0;
    }

    public Iterator iterator() {
        return new SeqIterator(seq());
    }
}
