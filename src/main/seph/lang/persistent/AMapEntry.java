package seph.lang.persistent;

import java.io.StringWriter;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public abstract class AMapEntry extends APersistentVector implements IMapEntry {
    public Object at(int i){
        if(i == 0)
            return key();
        else if(i == 1)
            return val();
        else
            throw new IndexOutOfBoundsException();
    }

    private IPersistentVector asVector(){
        return LazilyPersistentVector.createOwning(key(), val());
    }

    public IPersistentVector assocN(int i, Object val){
        return asVector().assocN(i, val);
    }

    public int count(){
        return 2;
    }

    public ISeq seq(){
        return asVector().seq();
    }

    public IPersistentVector cons(Object o){
        return asVector().cons(o);
    }

    public PersistentCollection empty(){
        return null;
    }

    public IPersistentVector pop(){
        return LazilyPersistentVector.createOwning(key());
    }

    public Object setValue(Object value){
        throw new UnsupportedOperationException();
    }
}
