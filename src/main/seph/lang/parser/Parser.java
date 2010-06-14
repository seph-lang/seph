/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.io.Reader;

import seph.lang.Runtime;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Parser {
    private final Runtime runtime;
    private final Reader reader;

    public Parser(Runtime runtime, Reader reader) {
        this.runtime = runtime;
        this.reader = reader;
    }

    public Message parseFully() {
        return new NamedMessage(".", null, null);
    }
}// Parser
