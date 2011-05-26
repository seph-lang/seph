/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import org.jregex.Pattern;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Regexp implements SephObject { 
    private final String pattern;
    private final Pattern regexp;
    private final String flags;

    private Regexp(String pattern, Pattern regexp, String flags) {
        this.pattern = pattern;
        this.regexp = regexp;
        this.flags = flags;
    }

    public static Regexp create(String pattern, String flags) {
        return new Regexp(pattern, new Pattern(pattern, flags), flags);
    }

    public String pattern() {
        return this.pattern;
    }

    public Pattern regexp() {
        return this.regexp;
    }

    public String flags() {
        return this.flags;
    }



    @Override
    public SephObject get(String cellName) {
        return null;
    }

    @Override
    public Object identity() {
        return this;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        return ActivationHelpers.noActivateFor(this, arity, keywords);
    }
}// Regexp
