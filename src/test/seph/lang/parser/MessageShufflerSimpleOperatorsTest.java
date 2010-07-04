/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
public class MessageShufflerSimpleOperatorsTest {
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

    private static Message shuffle(Message msg) {
        return new MessageShuffler().shuffle(msg);
    }

    private String operator;
 
    public MessageShufflerSimpleOperatorsTest(String operator) {
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
                                           { "nand" }, { "xor" }, { "nor" }, { "return" }};
        return Arrays.asList(data);
    }

    @Test
    public void operator_should_be_translated_correctly_as_argument() {
        Message m = msg("method", PersistentList.create(Arrays.asList(msg("foo", msg(operator, msg("bar"))))));
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_as_nested_argument() {
        Message m = msg("method1", PersistentList.create(Arrays.asList(msg("method2", PersistentList.create(Arrays.asList(msg("foo", msg(operator, msg("bar")))))))));
        Message expected = msg("method1", PersistentList.create(Arrays.asList(msg("method2", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_within_something_else() {
        Message m = msg("method", PersistentList.create(Arrays.asList(msg("n"), msg("if", PersistentList.create(Arrays.asList(msg("foo", msg(operator, msg("bar"))), msg("n"), msg("n")))))));
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("n"), msg("if", PersistentList.create(Arrays.asList(msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar"))))), msg("n"), msg("n")))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void operator_should_be_translated_correctly_in_simple_version() {
        Message m = msg("foo", msg(operator, msg("bar")));
        Message expected = msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void operator_should_not_be_translated_when_already_given_argument() {
        Message m = msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))));
        Message expected = msg("foo", msg(operator, PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }
}// MessageShufflerSimpleOperatorsTest
