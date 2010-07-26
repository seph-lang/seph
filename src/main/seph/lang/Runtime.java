/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import seph.lang.ast.Message;
import seph.lang.parser.Parser;
import seph.lang.parser.StringUtils;
import seph.lang.interpreter.MessageInterpreter;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Runtime {
    public final static SephObject TRUE = new SimpleSephObject() {
            public String toString() {
                return "true";
            }
        };
    public final static SephObject NIL  = new SimpleSephObject() {
            public String toString() {
                return "nil";
            }
        };

    public Text newText(String stringBeforeEscapeMangling) {
        return new Text(new StringUtils().replaceEscapes(stringBeforeEscapeMangling));
    }

    public Text newUnescapedText(String text) {
        return new Text(text);
    }

    public Regexp newRegexp(String pattern, String flags) {
        return Regexp.create(new StringUtils().replaceRegexpEscapes(pattern), flags);
    }

    public Object evaluateStream(String name, Reader reader) throws ControlFlow, IOException {
        Message msg = (Message)new Parser(this, reader, name).parseFully().seq().first();
        return new MessageInterpreter(this, new Ground()).evaluate(msg);
    }

    public Object evaluateFile(File f) throws ControlFlow, IOException {
        return evaluateStream(f.getCanonicalPath(), new InputStreamReader(new FileInputStream(f), "UTF-8"));
    }

    public Object evaluateFile(String filename) throws ControlFlow, IOException {
        return evaluateStream(filename, new InputStreamReader(new FileInputStream(new File(filename)), "UTF-8"));
    }
}// Runtime
