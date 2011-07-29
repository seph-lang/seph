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
    private final boolean printBytecode;

    public final static boolean DEFAULT_TAIL_CALL_OPTIMIZATION = true;
    public final static boolean DEFAULT_LEXICAL_METHOD_HANDLE_LOOKUP = true;
    public final static boolean DEFAULT_LEXICAL_METHOD_HANDLE_INVOKE = true;
    public final static boolean DEFAULT_PRINT_BYTECODE = false;


    public SephConfig() {
        this(DEFAULT_TAIL_CALL_OPTIMIZATION, 
             DEFAULT_LEXICAL_METHOD_HANDLE_LOOKUP, 
             DEFAULT_LEXICAL_METHOD_HANDLE_INVOKE,
             DEFAULT_PRINT_BYTECODE);
    }

    public SephConfig(boolean tco, boolean lexmh, boolean lexinvokemh, boolean printBytecode) {
        this.tco = tco;
        this.lexmh = lexmh;
        this.lexinvokemh = lexinvokemh;
        this.printBytecode = printBytecode;
    }

    public boolean doTailCallOptimization() {
        return tco;
    }

    public SephConfig withTailCallOptimization() {
        return new SephConfig(true, lexmh, lexinvokemh, printBytecode);
    }

    public SephConfig withoutTailCallOptimization() {
        return new SephConfig(false, lexmh, lexinvokemh, printBytecode);
    }



    public boolean doLexicalMethodHandleLookup() {
        return lexmh;
    }

    public SephConfig withLexicalMethodHandleLookup() {
        return new SephConfig(tco, true, lexinvokemh, printBytecode);
    }

    public SephConfig withoutLexicalMethodHandleLookup() {
        return new SephConfig(tco, false, lexinvokemh, printBytecode);
    }



    public boolean doLexicalMethodHandleInvoke() {
        return lexinvokemh;
    }

    public SephConfig withLexicalMethodHandleInvoke() {
        return new SephConfig(tco, lexmh, true, printBytecode);
    }

    public SephConfig withoutLexicalMethodHandleInvoke() {
        return new SephConfig(tco, lexmh, false, printBytecode);
    }



    public boolean doPrintBytecode() {
        return printBytecode;
    }

    public SephConfig withPrintBytecode() {
        return new SephConfig(tco, lexmh, lexinvokemh, true);
    }

    public SephConfig withoutPrintBytecode() {
        return new SephConfig(tco, lexmh, lexinvokemh, false);
    }
}// SephConfig
