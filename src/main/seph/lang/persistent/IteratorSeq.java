package seph.lang.persistent;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Iterator;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */

public class IteratorSeq extends ASeq {
    final Iterator iter;
    final State state;

    static class State{
        volatile Object val;
        volatile Object _rest;
    }

    public static IteratorSeq create(Iterator iter) {
        if(iter.hasNext())
            return new IteratorSeq(iter);
        return null;
    }

    IteratorSeq(Iterator iter) {
        this.iter = iter;
        state = new State();
        this.state.val = state;
        this.state._rest = state;
    }

    IteratorSeq(IPersistentMap meta, Iterator iter, State state) {
        super(meta);
        this.iter = iter;
        this.state = state;
    }

    public Object first() {
        if(state.val == state)
            synchronized(state) {
                if(state.val == state)
                    state.val = iter.next();
            }
        return state.val;
    }

    public ISeq next() {
        if(state._rest == state)
            synchronized(state) {
                if(state._rest == state) {
                    first();
                    state._rest = create(iter);
                }
            }
        return (ISeq) state._rest;
    }

    public IteratorSeq withMeta(IPersistentMap meta) {
        return new IteratorSeq(meta, iter, state);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new NotSerializableException(getClass().getName());
    }
}
