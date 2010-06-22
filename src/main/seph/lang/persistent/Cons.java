package seph.lang.persistent;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public final class Cons extends ASeq {
    private final Object first;
    private final ISeq more;

    public Cons(Object first, ISeq more){
        this.first = first;
        this.more = more;
    }

    public Cons(IPersistentMap meta, Object first, ISeq more){
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
            return PersistentList.EMPTY;
        }
        return more;
    }
    
    public int count(){
        return 1 + RT.count(more);
    }

    public Cons withMeta(IPersistentMap meta){
        return new Cons(meta, first, more);
    }
}
