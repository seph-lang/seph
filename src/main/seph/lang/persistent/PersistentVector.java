package seph.lang.persistent;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import seph.lang.SephObject;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class PersistentVector extends APersistentVector implements SephObject, EditableCollection {
    static class Node implements Serializable {
        transient final AtomicReference<Thread> edit;
        final Object[] array;

        Node(AtomicReference<Thread> edit, Object[] array){
            this.edit = edit;
            this.array = array;
        }

        Node(AtomicReference<Thread> edit){
            this.edit = edit;
            this.array = new Object[32];
        }
    }

    final static AtomicReference<Thread> NOEDIT = new AtomicReference<Thread>(null);
    final static Node EMPTY_NODE = new Node(NOEDIT, new Object[32]);

    final int cnt;
    final int shift;
    final Node root;
    final Object[] tail;
    final IPersistentMap _meta;

    public final static PersistentVector EMPTY = new PersistentVector(0, 5, EMPTY_NODE, new Object[]{});

    static public PersistentVector create(ISeq items){
        TransientVector ret = EMPTY.asTransient();
        for(; items != null; items = items.next())
            ret = ret.conj(items.first());
        return ret.persistent();
    }

    static public PersistentVector create(List items){
        TransientVector ret = EMPTY.asTransient();
        for(Object item : items)
            ret = ret.conj(item);
        return ret.persistent();
    }

    static public PersistentVector create(Object... items){
        TransientVector ret = EMPTY.asTransient();
        for(Object item : items)
            ret = ret.conj(item);
        return ret.persistent();
    }

    PersistentVector(int cnt, int shift, Node root, Object[] tail){
        this._meta = null;
        this.cnt = cnt;
        this.shift = shift;
        this.root = root;
        this.tail = tail;
    }

    PersistentVector(IPersistentMap meta, int cnt, int shift, Node root, Object[] tail){
        this._meta = meta;
        this.cnt = cnt;
        this.shift = shift;
        this.root = root;
        this.tail = tail;
    }

    public TransientVector asTransient(){
        return new TransientVector(this);
    }

    final int tailoff(){
        if(cnt < 32)
            return 0;
        return ((cnt - 1) >>> 5) << 5;
    }

    public Object[] arrayFor(int i){
        if(i >= 0 && i < cnt) {
            if(i >= tailoff())
                return tail;
            Node node = root;
            for(int level = shift; level > 0; level -= 5)
                node = (Node) node.array[(i >>> level) & 0x01f];
            return node.array;
        }
        throw new IndexOutOfBoundsException();
    }

    public Object at(int i){
        Object[] node = arrayFor(i);
        return node[i & 0x01f];
    }

    public Object at(int i, Object notFound){
        if(i >= 0 && i < cnt)
            return at(i);
        return notFound;
    }

    public PersistentVector assocN(int i, Object val){
        if(i >= 0 && i < cnt) {
            if(i >= tailoff()) {
                Object[] newTail = new Object[tail.length];
                System.arraycopy(tail, 0, newTail, 0, tail.length);
                newTail[i & 0x01f] = val;

                return new PersistentVector(meta(), cnt, shift, root, newTail);
            }

            return new PersistentVector(meta(), cnt, shift, doAssoc(shift, root, i, val), tail);
        }
        if(i == cnt)
            return cons(val);
        throw new IndexOutOfBoundsException();
    }

    private static Node doAssoc(int level, Node node, int i, Object val){
        Node ret = new Node(node.edit,node.array.clone());
        if(level == 0) {
                ret.array[i & 0x01f] = val;
        } else {
            int subidx = (i >>> level) & 0x01f;
            ret.array[subidx] = doAssoc(level - 5, (Node) node.array[subidx], i, val);
        }
        return ret;
    }

    public int count(){
        return cnt;
    }

    public PersistentVector withMeta(IPersistentMap meta){
        return new PersistentVector(meta, cnt, shift, root, tail);
    }

    public IPersistentMap meta(){
        return _meta;
    }

    public PersistentVector cons(Object val){
        int i = cnt;
        if(cnt - tailoff() < 32) {
            Object[] newTail = new Object[tail.length + 1];
            System.arraycopy(tail, 0, newTail, 0, tail.length);
            newTail[tail.length] = val;
            return new PersistentVector(meta(), cnt + 1, shift, root, newTail);
        }
        Node newroot;
        Node tailnode = new Node(root.edit,tail);
        int newshift = shift;
        if((cnt >>> 5) > (1 << shift)) {
            newroot = new Node(root.edit);
            newroot.array[0] = root;
            newroot.array[1] = newPath(root.edit,shift, tailnode);
            newshift += 5;
        } else
            newroot = pushTail(shift, root, tailnode);
        return new PersistentVector(meta(), cnt + 1, newshift, newroot, new Object[]{val});
    }

    private Node pushTail(int level, Node parent, Node tailnode){
        int subidx = ((cnt - 1) >>> level) & 0x01f;
        Node ret = new Node(parent.edit, parent.array.clone());
        Node nodeToInsert;
        if(level == 5) {
            nodeToInsert = tailnode;
        } else {
            Node child = (Node) parent.array[subidx];
            nodeToInsert = (child != null)?
                pushTail(level-5,child, tailnode)
                :newPath(root.edit,level-5, tailnode);
        }
        ret.array[subidx] = nodeToInsert;
        return ret;
    }

    private static Node newPath(AtomicReference<Thread> edit, int level, Node node) {
        if(level == 0)
            return node;
        Node ret = new Node(edit);
        ret.array[0] = newPath(edit, level - 5, node);
        return ret;
    }

    public ISeq seq(){
        if(count() == 0)
            return null;
        return new ChunkedSeq(this,0,0);
    }

    static public final class ChunkedSeq extends ASeq {
        public final PersistentVector vec;
        final Object[] node;
        final int i;
        public final int offset;

        public ChunkedSeq(PersistentVector vec, int i, int offset){
            this.vec = vec;
            this.i = i;
            this.offset = offset;
            this.node = vec.arrayFor(i);
        }

        ChunkedSeq(IPersistentMap meta, PersistentVector vec, Object[] node, int i, int offset){
            super(meta);
            this.vec = vec;
            this.node = node;
            this.i = i;
            this.offset = offset;
        }

        ChunkedSeq(PersistentVector vec, Object[] node, int i, int offset){
            this.vec = vec;
            this.node = node;
            this.i = i;
            this.offset = offset;
        }

        public ChunkedSeq withMeta(IPersistentMap meta){
            if(meta == meta())
                return this;
            return new ChunkedSeq(meta, vec, node, i, offset);
        }

        public Object first(){
            return node[offset];
        }

        public ISeq chunkedNext(){
            if(i + node.length < vec.cnt)
                return new ChunkedSeq(vec,i+ node.length,0);
            return null;
		}

        public ISeq next(){
            if(offset + 1 < node.length)
                return new ChunkedSeq(vec, node, i, offset + 1);
            return chunkedNext();
        }
    }

    public IPersistentCollection empty(){
        return EMPTY.withMeta(meta());
    }

    public PersistentVector pop(){
        if(cnt == 0)
            throw new IllegalStateException("Can't pop empty vector");
        if(cnt == 1)
            return EMPTY.withMeta(meta());
        if(cnt-tailoff() > 1) {
            Object[] newTail = new Object[tail.length - 1];
            System.arraycopy(tail, 0, newTail, 0, newTail.length);
            return new PersistentVector(meta(), cnt - 1, shift, root, newTail);
        }
        Object[] newtail = arrayFor(cnt - 2);

        Node newroot = popTail(shift, root);
        int newshift = shift;
        if(newroot == null) {
            newroot = EMPTY_NODE;
        }
        if(shift > 5 && newroot.array[1] == null) {
            newroot = (Node) newroot.array[0];
            newshift -= 5;
        }
        return new PersistentVector(meta(), cnt - 1, newshift, newroot, newtail);
    }

    private Node popTail(int level, Node node){
        int subidx = ((cnt-2) >>> level) & 0x01f;
        if(level > 5) {
            Node newchild = popTail(level - 5, (Node) node.array[subidx]);
            if(newchild == null && subidx == 0)
                return null;
            else {
                Node ret = new Node(root.edit, node.array.clone());
                ret.array[subidx] = newchild;
                return ret;
            }
        } else if(subidx == 0) {
            return null;
        } else {
            Node ret = new Node(root.edit, node.array.clone());
            ret.array[subidx] = null;
            return ret;
        }
    }

    static final class TransientVector implements ITransientVector, Counted {
        int cnt;
        int shift;
        Node root;
        Object[] tail;

        TransientVector(int cnt, int shift, Node root, Object[] tail){
            this.cnt = cnt;
            this.shift = shift;
            this.root = root;
            this.tail = tail;
        }

        TransientVector(PersistentVector v){
            this(v.cnt, v.shift, editableRoot(v.root), editableTail(v.tail));
        }

        public int count(){
            ensureEditable();
            return cnt;
        }
	
        Node ensureEditable(Node node){
            if(node.edit == root.edit)
                return node;
            return new Node(root.edit, node.array.clone());
        }

        void ensureEditable(){
            Thread owner = root.edit.get();
            if(owner == Thread.currentThread())
                return;
            if(owner != null)
                throw new IllegalAccessError("Transient used by non-owner thread");
            throw new IllegalAccessError("Transient used after persistent! call");
        }

        static Node editableRoot(Node node){
            return new Node(new AtomicReference<Thread>(Thread.currentThread()), node.array.clone());
        }

        public PersistentVector persistent(){
            ensureEditable();
            root.edit.set(null);
            Object[] trimmedTail = new Object[cnt-tailoff()];
            System.arraycopy(tail,0,trimmedTail,0,trimmedTail.length);
            return new PersistentVector(cnt, shift, root, trimmedTail);
        }

        static Object[] editableTail(Object[] tl){
            Object[] ret = new Object[32];
            System.arraycopy(tl,0,ret,0,tl.length);
            return ret;
        }

        public TransientVector conj(Object val){
            ensureEditable();
            int i = cnt;
            if(i - tailoff() < 32) {
                tail[i & 0x01f] = val;
                ++cnt;
                return this;
            }
            Node newroot;
            Node tailnode = new Node(root.edit, tail);
            tail = new Object[32];
            tail[0] = val;
            int newshift = shift;
            //overflow root?
            if((cnt >>> 5) > (1 << shift)) {
                newroot = new Node(root.edit);
                newroot.array[0] = root;
                newroot.array[1] = newPath(root.edit,shift, tailnode);
                newshift += 5;
            } else
                newroot = pushTail(shift, root, tailnode);
            root = newroot;
            shift = newshift;
            ++cnt;
            return this;
        }

        private Node pushTail(int level, Node parent, Node tailnode){
            parent = ensureEditable(parent);
            int subidx = ((cnt - 1) >>> level) & 0x01f;
            Node ret = parent;
            Node nodeToInsert;
            if(level == 5) {
                nodeToInsert = tailnode;
            } else {
                Node child = (Node) parent.array[subidx];
                nodeToInsert = (child != null) ?
                    pushTail(level - 5, child, tailnode)
                    : newPath(root.edit, level - 5, tailnode);
            }
            ret.array[subidx] = nodeToInsert;
            return ret;
        }

        final private int tailoff(){
            if(cnt < 32)
                return 0;
            return ((cnt-1) >>> 5) << 5;
        }

        private Object[] arrayFor(int i){
            if(i >= 0 && i < cnt) {
                if(i >= tailoff())
                    return tail;
                Node node = root;
                for(int level = shift; level > 0; level -= 5)
                    node = (Node) node.array[(i >>> level) & 0x01f];
                return node.array;
            }
            throw new IndexOutOfBoundsException();
        }

        public Object valueAt(Object key){
            return valueAt(key, null);
        }

        public Object valueAt(Object key, Object notFound){
            ensureEditable();
            if(Util.isInteger(key)) {
                int i = ((Number) key).intValue();
                if(i >= 0 && i < cnt)
                    return at(i);
            }
            return notFound;
        }

        public Object invoke(Object arg1) throws Exception{
            if(Util.isInteger(arg1))
                return at(((Number) arg1).intValue());
            throw new IllegalArgumentException("Key must be integer");
        }

        public Object at(int i){
            ensureEditable();
            Object[] node = arrayFor(i);
            return node[i & 0x01f];
        }

        public Object at(int i, Object notFound){
            if(i >= 0 && i < count())
                return at(i);
            return notFound;
        }

        public TransientVector assocN(int i, Object val){
            ensureEditable();
            if(i >= 0 && i < cnt) {
                if(i >= tailoff()) {
                    tail[i & 0x01f] = val;
                    return this;
                }

                root = doAssoc(shift, root, i, val);
                return this;
            }
            if(i == cnt)
                return conj(val);
            throw new IndexOutOfBoundsException();
        }

        public TransientVector associate(Object key, Object val){
            if(Util.isInteger(key)) {
                int i = ((Number) key).intValue();
                return assocN(i, val);
            }
            throw new IllegalArgumentException("Key must be integer");
        }

        private Node doAssoc(int level, Node node, int i, Object val){
            node = ensureEditable(node);
            Node ret = node;
            if(level == 0) {
                ret.array[i & 0x01f] = val;
            } else {
                int subidx = (i >>> level) & 0x01f;
                ret.array[subidx] = doAssoc(level - 5, (Node) node.array[subidx], i, val);
            }
            return ret;
        }

        public TransientVector pop(){
            ensureEditable();
            if(cnt == 0)
                throw new IllegalStateException("Can't pop empty vector");
            if(cnt == 1) {
                cnt = 0;
                return this;
            }
            int i = cnt - 1;

            if((i & 0x01f) > 0) {
                --cnt;
                return this;
            }

            Object[] newtail = arrayFor(cnt - 2);

            Node newroot = popTail(shift, root);
            int newshift = shift;
            if(newroot == null) {
                newroot = new Node(root.edit);
            }
            if(shift > 5 && newroot.array[1] == null) {
                newroot = ensureEditable((Node) newroot.array[0]);
                newshift -= 5;
            }
            root = newroot;
            shift = newshift;
            --cnt;
            tail = newtail;
            return this;
        }

        private Node popTail(int level, Node node){
            node = ensureEditable(node);
            int subidx = ((cnt - 2) >>> level) & 0x01f;
            if(level > 5) {
                Node newchild = popTail(level - 5, (Node) node.array[subidx]);
                if(newchild == null && subidx == 0)
                    return null;
                else {
                    Node ret = node;
                    ret.array[subidx] = newchild;
                    return ret;
                }
            } else if(subidx == 0)
                return null;
            else {
                Node ret = node;
                ret.array[subidx] = null;
                return ret;
            }
        }
    }
}
