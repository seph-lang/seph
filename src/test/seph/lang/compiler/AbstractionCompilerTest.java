/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.lang.reflect.Method;
import java.io.*;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.*;
import seph.lang.parser.*;

import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class AbstractionCompilerTest {
    private ControlFlow error;

    private Message parse(String input) {
        return parse(input, "<eval>");
    }

    private Message parse(String input, String sourcename) {
        try {
            return ((Message)new Parser(new seph.lang.Runtime(), new StringReader(input), sourcename).parseFully().seq().first());
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        } catch(ControlFlow cf) {
            this.error = cf;
            return null;
        }
    }

    @Test
    public void ensure_generated_argument_method_can_return_the_argument_code_unevaluated() throws Exception, ControlFlow {
        Message code = parse("foobar(x y z, bar foo 42)");
        SephObject so = AbstractionCompiler.compile(code, LexicalScope.ROOT);
        Method m = so.getClass().getMethod("argument_0_0", SephObject.class, SThread.class, LexicalScope.class, boolean.class);
        Message result = (Message)m.invoke(null, null, null, null, false);
        
        assertEquals("x", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertEquals("y", result.next().name());
        assertEquals(PersistentList.EMPTY, result.next().arguments());
        assertEquals("z", result.next().next().name());
        assertEquals(PersistentList.EMPTY, result.next().next().arguments());
        assertNull(result.next().next().next());

        m = so.getClass().getMethod("argument_0_1", SephObject.class, SThread.class, LexicalScope.class, boolean.class);
        result = (Message)m.invoke(null, null, null, null, false);

        assertEquals("bar", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertEquals("foo", result.next().name());
        assertEquals(PersistentList.EMPTY, result.next().arguments());
        assertEquals(true, result.next().next().isLiteral());
        assertEquals(PersistentList.EMPTY, result.next().next().arguments());
        assertNull(result.next().next().next());
    }
}// AbstractionCompilerTest


