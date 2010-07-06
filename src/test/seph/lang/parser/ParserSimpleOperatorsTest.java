/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Arrays;
import java.util.Collection;
import java.io.StringReader;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.Regexp;
import seph.lang.ControlFlow;
import seph.lang.Text;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.ast.LiteralMessage;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@RunWith(Parameterized.class)
public class ParserSimpleOperatorsTest {
    private Message parse(String input) {
        return parse(input, "<eval>");
    }

    private Message parse(String input, String sourcename) {
        try {
            return ((Message)new Parser(new Runtime(), new StringReader(input), sourcename).parseFully().seq().first());
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        } catch(ControlFlow cf) {
            return null;
        }
    }

    private static Message msg(String name) {
        return new NamedMessage(name, null, null, null, -1, -1);
    }

    private static Message msg(String name, Message next) {
        return new NamedMessage(name, null, next, null, -1, -1);
    }

    private static Message msg(String name, IPersistentList args, Message next) {
        return new NamedMessage(name, args, next, null, -1, -1);
    }

    private static Message msg(String name, IPersistentList args) {
        return new NamedMessage(name, args, null, null, -1, -1);
    }

    private String operator;
 
    public ParserSimpleOperatorsTest(String operator) {
	    this.operator = operator;
    }
 
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { "<=>" }, { "<" }, { ">" }, { "<=" }, { ">=" }, { "≤" }, { "≥" }, { "!=" }, 
                                           { "≠" }, { "==" }, { "===" }, { "=~" }, { "!~" }, { "-" }, { "+" }, { "*" }, 
                                           { "/" }, { "**" }, { "%" }, { "=>" }, { ".." }, { "..." }, { "<<" }, { ">>" }, 
                                           { "&" }, { "|" }, { "^" }, { "&&" }, { "||" }, { "?&" }, { "?|" }, { "or" }, 
                                           { "and" }, { "->" }, { "+>" }, { "!>" }, { "<>" }, { "&>" }, { "%>" }, { "#>" }, 
                                           { "@>" }, { "/>" }, { "*>" }, { "?>" }, { "|>" }, { "^>" }, { "~>" }, { "->>" }, 
                                           { "+>>" }, { "!>>" }, { "<>>" }, { "&>>" }, { "%>>" }, { "#>>" }, { "@>>" }, { "/>>" }, 
                                           { "*>>" }, { "?>>" }, { "|>>" }, { "^>>" }, { "~>>" }, { "=>>" }, { "**>" }, { "**>>" }, 
                                           { "&&>" }, { "&&>>" }, { "||>" }, { "||>>" }, { "$>" }, { "$>>" }, { "<->" }, { "<-" },
                                           { "nand" }, { "xor" }, { "nor" }, { "return" }, { "∪" }, { "∩" }, { "⊂" }, { "⊃" }, { "⊆" }, { "⊇" },
                                           { "?" }};
        return Arrays.asList(data);
    }

    @Test
    public void operator_should_be_translated_correctly_as_argument() {
        Message m = parse("method(foo " + operator + " bar)");
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_as_nested_argument() {
        Message m = parse("method1(method2(foo " + operator + " bar))");
        Message expected = msg("method1", PersistentList.create(Arrays.asList(msg("method2", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_within_something_else() {
        Message m = parse("method(n, if(foo " + operator + " bar, n, n))");
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("n"), msg("if", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar"))))), msg("n"), msg("n")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_in_simple_version() {
        Message m = parse("foo " + operator + " bar");
        Message expected = msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void operator_should_not_be_translated_when_already_given_argument() {
        Message m = parse("foo " + operator + "(bar)");
        Message expected = msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(m, is(equalTo(expected)));
    }
}// ParserSimpleOperatorsTest
