package seph.lang.persistent;

import java.io.Serializable;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public final class Cons extends Seq {
    private final Object first;
    private final ISeq more;

    public Cons(Object first, ISeq more){
        this.first = first;
        this.more = more;
    }

    public Cons(PersistentMap meta, Object first, ISeq more){
        this.first = first;
        this.more = more;
    }

    public Object first(){
        return first;
    }

    public ISeq next(){
        return more().seq();
    }

    public ISeq more(){
        if(more == null) {
            return PersistentCons.EMPTY;
        }
        return more;
    }
    
    public int count(){
        return 1 + RT.count(more);
    }

    public Cons withMeta(PersistentMap meta){
        return new Cons(meta, first, more);
    }
}
