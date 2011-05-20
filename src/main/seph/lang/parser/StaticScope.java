/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.List;
import java.util.LinkedList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class StaticScope {
    private StaticScope parent;
    private List<String> names = new LinkedList<>();

    public StaticScope(StaticScope parent) {
        this.parent = parent;
    }

    public StaticScope getParent() {
        return this.parent;
    }

    private static class ScopeEntry {
        public final int depth;
        public final int index;
        public ScopeEntry(int depth, int index) {
            this.depth = depth;
            this.index = index;
        }

        public int toInt() {
            return (depth << 16) | index;
        }
    }

    private ScopeEntry findName(String name, int depth) {
        int ix = names.indexOf(name);
        if(ix == -1) {
            if(parent == null) {
                return null;
            } else {
                return parent.findName(name, depth + 1);
            }
        } else {
            return new ScopeEntry(depth, ix);
        }
    }

    public int addOrFindName(String name) {
        ScopeEntry existing = findName(name, 0);
        if(existing == null) {
            existing = new ScopeEntry(0, names.size());
            names.add(name);
        }
        return existing.toInt();
    }

    @Override
    public String toString() {
        return "Scope<" +  names + (parent == null ? ">" : " || parent=" + parent + ">");
    }
}// StaticScope
