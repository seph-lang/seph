/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Set;
import java.util.HashSet;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class StaticScope {
    private StaticScope parent;
    private Set<String> names = new HashSet<>();
    private Set<String> shadowing = new HashSet<>();

    public Set<String> getNames() {
        return names;
    }

    public Set<String> getShadowing() {
        return shadowing;
    }

    public StaticScope(StaticScope parent) {
        this.parent = parent;
    }

    public StaticScope getParent() {
        return this.parent;
    }

    public void addName(String name) {
        names.add(name);
    }

    public void addShadowing(String name) {
        shadowing.add(name);
    }

    @Override
    public String toString() {
        return "Scope<" +  names + (parent == null ? ">" : " || parent=" + parent + ">");
    }
}// StaticScope
