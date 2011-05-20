/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.Map;

import seph.lang.ast.Message;
import seph.lang.persistent.IPersistentMap;
import seph.lang.persistent.APersistentMap;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.RT;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LexicalScope {
    private final LexicalScope parent;
    public final Runtime runtime;
    public volatile int version = 0;

    public LexicalScope(LexicalScope parent, Runtime runtime) {
        this(parent, runtime, new String[0]);
    }

    private LexicalScope(LexicalScope parent, Runtime runtime, String[] names) {
        this.parent = parent;
        this.runtime = runtime;
        this.names = names;
        this.values = new SephObject[names.length];
    }

    public LexicalScope newScopeWith(String[] names) {
        return new LexicalScope(this, runtime, names);
    }

    private SephObject[] values;
    private String[] names;

    public final void assign(int depth, int index, SephObject value) {
        if(depth == 0) {
            version++;
            values[index] = value;
        } else {
            parent.assign(depth - 1, index, value);
        }
    }

    public final SephObject get(int depth, int index) {
        if(depth == 0) {
            return values[index];
        } else {
            return parent.get(depth - 1, index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        String sep = "";
        for(int i = 0;i < values.length; i++) {
            sb.append(names[i]).append(" => ").append(values[i]).append(sep);
            sep = ", ";
        }
        sb.append("}");
        if(parent != null) {
            sb.append("(").append(parent).append(")");
        }

        return sb.toString();
    }
}// LexicalScope
