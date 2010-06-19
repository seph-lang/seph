package seph.lang.persistent;

import java.util.*;

import seph.lang.SimpleSephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public abstract class Seq extends SimpleSephObject implements ISeq, List {
    transient int hash = -1;

    protected Seq(PersistentMap meta) {
        super(meta);
    }

    protected Seq() {
    }

    public boolean equiv(Object obj){
        if(!(obj instanceof Sequential || obj instanceof List))
            return false;

        ISeq ms = RT.seq(obj);
        for(ISeq s = seq(); s != null; s = s.next(), ms = ms.next()) {
            if(ms == null || !Util.equiv(s.first(), ms.first()))
                return false;
		}
        return ms == null;
    }

    final public ISeq seq(){
        return this;
    }

    public ISeq cons(Object o){
        return new Cons(o, this);
    }

    public PersistentCollection empty(){
        return PersistentCons.EMPTY;
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Sequential || obj instanceof List))
            return false;
        ISeq ms = RT.seq(obj);
        for(ISeq s = seq(); s != null; s = s.next(), ms = ms.next()) {
            if(ms == null || !Util.equals(s.first(), ms.first()))
                return false;
		}
        return ms == null;
    }

    public int hashCode() {
        if(hash == -1) {
            int hash = 1;
            for(ISeq s = seq(); s != null; s = s.next()) {
                hash = 31 * hash + (s.first() == null ? 0 : s.first().hashCode());
			}
            this.hash = hash;
		}
        return hash;
    }

    public int count() {
        int i = 1;
        for(ISeq s = next(); s != null; s = s.next(), i++)
            if(s instanceof Counted)
                return i + s.count();
        return i;
    }

    public ISeq more() {
        ISeq s = next();
        if(s == null)
            return PersistentCons.EMPTY;
        return s;
    }

    public Object[] toArray(){
        return RT.seqToArray(seq());
    }

    public boolean add(Object o){
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o){
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection c){
        throw new UnsupportedOperationException();
    }

    public void clear(){
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection c){
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c){
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection c){
        for(Object o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    public Object[] toArray(Object[] a){
        if(a.length >= count()) {
            ISeq s = seq();
            for(int i = 0; s != null; ++i, s = s.next()) {
                a[i] = s.first();
            }
            if(a.length > count())
                a[count()] = null;
            return a;
        } else {
            return toArray();
        }
    }

    public int size(){
        return count();
    }

    public boolean isEmpty(){
        return seq() == null;
    }

    public boolean contains(Object o){
        for(ISeq s = seq(); s != null; s = s.next()) {
            if(Util.equiv(s.first(), o))
                return true;
        }
        return false;
    }

    public Iterator iterator(){
        return new SeqIterator(this);
    }

    private List reify(){
        return Collections.unmodifiableList(new ArrayList(this));
    }

    public List subList(int fromIndex, int toIndex){
        return reify().subList(fromIndex, toIndex);
    }

    public Object set(int index, Object element){
        throw new UnsupportedOperationException();
    }

    public Object remove(int index){
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o){
        ISeq s = seq();
        for(int i = 0; s != null; s = s.next(), i++) {
            if(Util.equiv(s.first(), o))
                return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o){
        return reify().lastIndexOf(o);
    }

    public ListIterator listIterator(){
        return reify().listIterator();
    }

    public ListIterator listIterator(int index){
        return reify().listIterator(index);
    }

    public Object get(int index){
        return RT.nth(this, index);
    }

    public void add(int index, Object element){
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection c){
        throw new UnsupportedOperationException();
    }
}
