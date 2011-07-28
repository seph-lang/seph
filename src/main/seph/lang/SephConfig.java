/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SephConfig {
    private final boolean tco;
    private final boolean lexmh;
    private final boolean lexinvokemh;

    public final static boolean DEFAULT_TAIL_CALL_OPTIMIZATION = true;
    public final static boolean DEFAULT_LEXICAL_METHOD_HANDLE_LOOKUP = true;
    public final static boolean DEFAULT_LEXICAL_METHOD_HANDLE_INVOKE = true;


    public SephConfig() {
        this(DEFAULT_TAIL_CALL_OPTIMIZATION, 
             DEFAULT_LEXICAL_METHOD_HANDLE_LOOKUP, 
             DEFAULT_LEXICAL_METHOD_HANDLE_INVOKE);
    }

    public SephConfig(boolean tco, boolean lexmh, boolean lexinvokemh) {
        this.tco = tco;
        this.lexmh = lexmh;
        this.lexinvokemh = lexinvokemh;
    }

    public boolean doTailCallOptimization() {
        return tco;
    }

    public SephConfig withTailCallOptimization() {
        return new SephConfig(true, lexmh, lexinvokemh);
    }

    public SephConfig withoutTailCallOptimization() {
        return new SephConfig(false, lexmh, lexinvokemh);
    }



    public boolean doLexicalMethodHandleLookup() {
        return lexmh;
    }

    public SephConfig withLexicalMethodHandleLookup() {
        return new SephConfig(tco, true, lexinvokemh);
    }

    public SephConfig withoutLexicalMethodHandleLookup() {
        return new SephConfig(tco, false, lexinvokemh);
    }



    public boolean doLexicalMethodHandleInvoke() {
        return lexinvokemh;
    }

    public SephConfig withLexicalMethodHandleInvoke() {
        return new SephConfig(tco, lexmh, true);
    }

    public SephConfig withoutLexicalMethodHandleInvoke() {
        return new SephConfig(tco, lexmh, false);
    }
}// SephConfig
