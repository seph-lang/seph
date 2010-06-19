package seph.lang.persistent;

import java.util.*;
import java.lang.reflect.Array;
import java.util.regex.Matcher;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class RT {
    static public final Object[] EMPTY_ARRAY = new Object[0];

    public static int count(Object o){
        if(o instanceof Counted) {
            return ((Counted) o).count();
        }
        return countFrom(Util.ret1(o, o = null));
    }

    static int countFrom(Object o){
        if(o == null) {
            return 0;
        } else if(o instanceof PersistentCollection) {
            ISeq s = seq(o);
            o = null;
            int i = 0;
            for(; s != null; s = s.next()) {
                if(s instanceof Counted)
                    return i + s.count();
                i++;
            }
            return i;
        } else if(o instanceof CharSequence) {
            return ((CharSequence) o).length();
        } else if(o instanceof Collection) {
            return ((Collection) o).size();
        } else if(o instanceof Map) {
            return ((Map) o).size();
        } else if(o.getClass().isArray()) {
            return Array.getLength(o);
        }

        throw new UnsupportedOperationException("count not supported on this type: " + o.getClass().getSimpleName());
    }

    static public ISeq seq(Object coll){
        if(coll instanceof Seq) {
            return (Seq) coll;
        } else {
            return seqFrom(coll);
        }
    }

    static ISeq seqFrom(Object coll){
        if(coll instanceof Seqable) {
            return ((Seqable) coll).seq();
        } else if(coll == null) {
            return null;
        } else {
            Class c = coll.getClass();
            Class sc = c.getSuperclass();
            throw new IllegalArgumentException("Don't know how to create ISeq from: " + c.getName());
        }
    }

    static public Object nth(Object coll, int n) {
        if(coll instanceof Indexed) {
            return ((Indexed) coll).at(n);
        }
        return nthFrom(Util.ret1(coll, coll = null), n);
    }

    static Object nthFrom(Object coll, int n){
        if(coll == null)
            return null;
        else if(coll instanceof CharSequence)
            return Character.valueOf(((CharSequence) coll).charAt(n));
        else if(coll instanceof RandomAccess)
            return ((List) coll).get(n);
        else if(coll instanceof Matcher)
            return ((Matcher) coll).group(n);
        else if(coll instanceof Map.Entry) {
            Map.Entry e = (Map.Entry) coll;
            if(n == 0)
                return e.getKey();
            else if(n == 1)
                return e.getValue();
            throw new IndexOutOfBoundsException();
        } else if(coll instanceof Sequential) {
            ISeq seq = RT.seq(coll);
            coll = null;
            for(int i = 0; i <= n && seq != null; ++i, seq = seq.next()) {
                if(i == n)
                    return seq.first();
            }
            throw new IndexOutOfBoundsException();
        }
        else
            throw new UnsupportedOperationException("nth not supported on this type: " + coll.getClass().getSimpleName());
    }

    static public Object nth(Object coll, int n, Object notFound){
        if(coll instanceof Indexed) {
            Indexed v = (Indexed) coll;
			return v.at(n, notFound);
        }
        return nthFrom(coll, n, notFound);
    }

    static Object nthFrom(Object coll, int n, Object notFound){
        if(coll == null)
            return notFound;
        else if(n < 0)
            return notFound;
        else if(coll instanceof CharSequence) {
            CharSequence s = (CharSequence) coll;
            if(n < s.length())
                return Character.valueOf(s.charAt(n));
            return notFound;
        }
        else if(coll instanceof RandomAccess) {
            List list = (List) coll;
            if(n < list.size())
                return list.get(n);
            return notFound;
        }
        else if(coll instanceof Matcher) {
            Matcher m = (Matcher) coll;
            if(n < m.groupCount())
                return m.group(n);
            return notFound;
        }
        else if(coll instanceof Map.Entry) {
            Map.Entry e = (Map.Entry) coll;
            if(n == 0)
                return e.getKey();
            else if(n == 1)
                return e.getValue();
            return notFound;
        }
        else if(coll instanceof Sequential) {
            ISeq seq = RT.seq(coll);
            coll = null;
            for(int i = 0; i <= n && seq != null; ++i, seq = seq.next()) {
                if(i == n)
                    return seq.first();
            }
            return notFound;
        }
        else
            throw new UnsupportedOperationException("nth not supported on this type: " + coll.getClass().getSimpleName());
    }

    static public Object[] seqToArray(ISeq seq){
        int len = length(seq);
        Object[] ret = new Object[len];
        for(int i = 0; seq != null; ++i, seq = seq.next())
            ret[i] = seq.first();
        return ret;
    }

    static public int length(ISeq list){
        int i = 0;
        for(ISeq c = list; c != null; c = c.next()) {
            i++;
        }
        return i;
    }

    static public Object first(Object x) {
        if(x instanceof ISeq)
            return ((ISeq) x).first();
        ISeq seq = seq(x);
        if(seq == null)
            return null;
        return seq.first();
    }

    static public ISeq next(Object x) {
        if(x instanceof ISeq)
            return ((ISeq) x).next();
        ISeq seq = seq(x);
        if(seq == null)
            return null;
        return seq.next();
    }
}
