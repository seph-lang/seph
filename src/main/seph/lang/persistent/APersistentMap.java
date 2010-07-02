package seph.lang.persistent;

import java.io.Serializable;
import java.util.*;

import seph.lang.SimpleSephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public abstract class APersistentMap extends SimpleSephObject implements IPersistentMap, Map, Iterable, Serializable {
    int _hash = -1;

    protected APersistentMap(IPersistentMap meta) {
        super(meta);
    }

    protected APersistentMap() {
    }

    public IPersistentCollection cons(Object o){
        if(o instanceof Map.Entry) {
            Map.Entry e = (Map.Entry) o;
            return associate(e.getKey(), e.getValue());
        } else if(o instanceof IPersistentVector) {
            IPersistentVector v = (IPersistentVector) o;
            if(v.count() != 2)
                throw new IllegalArgumentException("Vector arg to map conj must be a pair");
            return associate(v.at(0), v.at(1));
        }

        IPersistentMap ret = this;
        for(ISeq es = RT.seq(o); es != null; es = es.next()) {
            Map.Entry e = (Map.Entry) es.first();
            ret = ret.associate(e.getKey(), e.getValue());
        }
        return ret;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Map))
            return false;
        Map m = (Map) obj;

        if(m.size() != size() || m.hashCode() != hashCode())
            return false;

        for(ISeq s = seq(); s != null; s = s.next()) {
            Map.Entry e = (Map.Entry) s.first();
            boolean found = m.containsKey(e.getKey());

            if(!found || !Util.equals(e.getValue(), m.get(e.getKey())))
                return false;
        }

        return true;
    }

    public boolean equiv(Object obj) {
        if(!(obj instanceof Map))
            return false;
        Map m = (Map) obj;

        if(m.size() != size())
            return false;

        for(ISeq s = seq(); s != null; s = s.next()) {
            Map.Entry e = (Map.Entry) s.first();
            boolean found = m.containsKey(e.getKey());
            
            if(!found || !Util.equiv(e.getValue(), m.get(e.getKey())))
                return false;
        }

        return true;
    }

    public int hashCode(){
        if(_hash == -1) {
            int hash = 0;
            for(ISeq s = seq(); s != null; s = s.next()) {
                Map.Entry e = (Map.Entry) s.first();
                hash += (e.getKey() == null ? 0 : e.getKey().hashCode()) ^
                    (e.getValue() == null ? 0 : e.getValue().hashCode());
            }
            this._hash = hash;
        }
        return _hash;
    }

    static public class KeySeq extends ASeq {
        ISeq seq;

        static public KeySeq create(ISeq seq){
            if(seq == null)
                return null;
            return new KeySeq(seq);
        }

        private KeySeq(ISeq seq){
            this.seq = seq;
        }

        private KeySeq(IPersistentMap meta, ISeq seq){
            super(meta);
            this.seq = seq;
        }

        public Object first(){
            return ((Map.Entry) seq.first()).getKey();
        }

        public ISeq next(){
            return create(seq.next());
        }

        public KeySeq withMeta(IPersistentMap meta){
            return new KeySeq(meta, seq);
        }
    }

    static public class ValSeq extends ASeq {
        ISeq seq;

        static public ValSeq create(ISeq seq){
            if(seq == null)
                return null;
            return new ValSeq(seq);
        }

        private ValSeq(ISeq seq){
            this.seq = seq;
        }

        private ValSeq(IPersistentMap meta, ISeq seq){
            super(meta);
            this.seq = seq;
        }

        public Object first(){
            return ((Map.Entry) seq.first()).getValue();
        }

        public ISeq next(){
            return create(seq.next());
        }

        public ValSeq withMeta(IPersistentMap meta){
            return new ValSeq(meta, seq);
        }
    }

    public void clear(){
        throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value){
        return values().contains(value);
    }

    public Set entrySet(){
        return new AbstractSet(){
            public Iterator iterator(){
                return APersistentMap.this.iterator();
            }

            public int size(){
                return count();
            }

            public int hashCode(){
                return APersistentMap.this.hashCode();
            }

            public boolean contains(Object o){
                if(o instanceof Entry) {
                    Entry e = (Entry) o;
                    Entry found = entryAt(e.getKey());
                    if(found != null && Util.equals(found.getValue(), e.getValue()))
                        return true;
                }
                return false;
            }
        };
    }

    public Object get(Object key){
        return valueAt(key);
    }

    public boolean isEmpty(){
        return count() == 0;
    }

    public Set keySet(){
        return new AbstractSet(){
            public Iterator iterator(){
                final Iterator mi = APersistentMap.this.iterator();

                return new Iterator(){
                    public boolean hasNext(){
                        return mi.hasNext();
                    }

                    public Object next(){
                        Entry e = (Entry) mi.next();
                        return e.getKey();
                    }

                    public void remove(){
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public int size(){
                return count();
            }

            public boolean contains(Object o){
                return APersistentMap.this.containsKey(o);
            }
        };
    }

    public Object put(Object key, Object value){
        throw new UnsupportedOperationException();
    }

    public void putAll(Map t){
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key){
        throw new UnsupportedOperationException();
    }

    public int size(){
        return count();
    }

    public Collection values(){
        return new AbstractCollection(){
            public Iterator iterator(){
                final Iterator mi = APersistentMap.this.iterator();

                return new Iterator(){
                    public boolean hasNext(){
                        return mi.hasNext();
                    }

                    public Object next(){
                        Entry e = (Entry) mi.next();
                        return e.getValue();
                    }

                    public void remove(){
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public int size(){
                return count();
            }
        };
    }
}
