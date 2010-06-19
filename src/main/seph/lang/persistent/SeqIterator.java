package seph.lang.persistent;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class SeqIterator implements Iterator {
    ISeq seq;
    
    public SeqIterator(ISeq seq) {
        this.seq = seq;
    }

    public boolean hasNext(){
        return seq != null;
    }

    public Object next() throws NoSuchElementException {
        if(seq == null)
            throw new NoSuchElementException();
        Object ret = RT.first(seq);
        seq = RT.next(seq);
        return ret;
    }

    public void remove(){
        throw new UnsupportedOperationException();
    }
}

