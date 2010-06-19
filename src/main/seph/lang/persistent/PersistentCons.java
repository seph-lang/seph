package seph.lang.persistent;

import java.io.Serializable;
import java.util.*;

import seph.lang.SimpleSephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class PersistentCons extends Seq implements PersistentList, List, Counted  {
    private final Object first;
    private final PersistentList rest;
    private final int count;

    final public static EmptyList EMPTY = new EmptyList(null);

    public PersistentCons(Object first){
        this.first = first;
        this.rest = null;
        this.count = 1;
    }

    PersistentCons(PersistentMap meta, Object first, PersistentList rest, int count) {
        super(meta);
        this.first = first;
        this.rest = rest;
        this.count = count;
    }

    public static PersistentList create(List init) {
        PersistentList ret = EMPTY;
        for(ListIterator i = init.listIterator(init.size()); i.hasPrevious();) {
            ret = (PersistentList)ret.cons(i.previous());
		}
        return ret;
    }

    public Object first() {
        return this.first;
    }

    public ISeq next() {
        if(count == 1) {
            return null;
        }
        return (ISeq) rest;
    }

    public Object peek(){
        return first();
    }

    public PersistentList pop(){
        if(rest == null) {
            return EMPTY.withMeta(meta());
        }
        return rest;
    }

    public int count(){
        return count;
    }

    public PersistentCons cons(Object o){
        return new PersistentCons(meta(), o, this, count + 1);
    }

    public PersistentCollection empty() {
        return EMPTY.withMeta(meta());
    }

    public PersistentCons withMeta(PersistentMap metaArg){
        if(metaArg != meta())
            return new PersistentCons(metaArg, first, rest, count);
        return this;
    }

    static class EmptyList extends SimpleSephObject implements PersistentList, List, ISeq, Counted {
        public int hashCode(){
            return 1;
        }

        public boolean equals(Object o) {
            return (o instanceof Sequential || o instanceof List) && RT.seq(o) == null;
        }

        public boolean equiv(Object o) {
            return equals(o);
        }
	
        EmptyList(PersistentMap meta) {
            super(meta);
        }

        public Object first() {
            return null;
        }

        public ISeq next() {
            return null;
        }

        public ISeq more() {
            return this;
        }

        public PersistentCons cons(Object o) {
            return new PersistentCons(meta(), o, null, 1);
        }

        public PersistentCollection empty() {
            return this;
        }

        public EmptyList withMeta(PersistentMap meta) {
            if(meta != meta())
                return new EmptyList(meta);
            return this;
        }

        public Object peek() {
            return null;
        }

        public PersistentList pop() {
            throw new IllegalStateException("Can't pop empty list");
        }

        public int count() {
            return 0;
        }

        public ISeq seq() {
            return null;
        }

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean contains(Object o) {
            return false;
        }

        public Iterator iterator() {
            return new Iterator() {
                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    throw new NoSuchElementException();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public Object[] toArray() {
            return RT.EMPTY_ARRAY;
        }

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection collection) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection collection) {
            return collection.isEmpty();
        }

        public Object[] toArray(Object[] objects){
            if(objects.length > 0) {
                objects[0] = null;
            }
            return objects;
        }

        private List reify() {
            return Collections.unmodifiableList(new ArrayList(this));
        }

        public List subList(int fromIndex, int toIndex) {
            return reify().subList(fromIndex, toIndex);
        }

        public Object set(int index, Object element) {
            throw new UnsupportedOperationException();
        }

        public Object remove(int index) {
            throw new UnsupportedOperationException();
        }

        public int indexOf(Object o) {
            ISeq s = seq();
            for(int i = 0; s != null; s = s.next(), i++)
                {
                    if(Util.equiv(s.first(), o))
                        return i;
                }
            return -1;
        }

        public int lastIndexOf(Object o) {
            return reify().lastIndexOf(o);
        }

        public ListIterator listIterator() {
            return reify().listIterator();
        }

        public ListIterator listIterator(int index) {
            return reify().listIterator(index);
        }

        public Object get(int index) {
            return RT.nth(this, index);
        }

        public void add(int index, Object element) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int index, Collection c) {
            throw new UnsupportedOperationException();
        }
    }
}
