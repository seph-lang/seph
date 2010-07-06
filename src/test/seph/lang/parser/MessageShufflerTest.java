/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Arrays;

import org.junit.Test;
import org.junit.Ignore;
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
public class MessageShufflerTest {
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

    @Test
    public void returns_the_same_message_for_a_single_simple_message() {
        Message m = msg("foo");
        assertThat(shuffle(m), is(equalTo(m)));
    }

    @Test
    public void returns_the_same_message_for_two_simple_messages_following_each_other() {
        Message m = msg("foo", msg("bar"));
        assertThat(shuffle(m), is(equalTo(m)));
    }

    @Test
    public void shuffles_a_plus_message() {
        Message m = msg("foo", msg("+", msg("bar")));
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_a_plus_message_with_preceding_messages() {
        Message m = msg("bux", msg("foo", msg("+", msg("bar"))));
        Message expected = msg("bux", msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar"))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_a_plus_message_with_following_messages() {
        Message m = msg("foo", msg("+", msg("bar", msg("something"))));
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("something"))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_a_plus_message_inside_of_an_argument_list() {
        Message m = msg("foo", PersistentList.create(Arrays.asList(msg("bar", msg("+", msg("bux", msg("foo")))), msg("blargus"))), msg("bar"));
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("bar", msg("+", PersistentList.create(Arrays.asList(msg("bux", msg("foo")))))), msg("blargus"))), msg("bar"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_unary_minus_correctly() {
        Message m = msg("-", msg("foo"));
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_minus_correctly_with_following_messages() {
        Message m = msg("-", msg("foo", msg("println")));
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_unary_minus_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println"));
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_unary_minus_several_times_over() {
        Message m = msg("-", msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println")));
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void shuffles_unary_binary_operator_in_simple_expression() {
        Message m = msg("map", PersistentList.create(Arrays.asList(msg("*", msg("foo")))));
        Message expected = msg("map", PersistentList.create(Arrays.asList(msg("*", PersistentList.create(Arrays.asList(msg("foo")))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_binary_operator_in_complex_expression() {
        Message m = msg("map", PersistentList.create(Arrays.asList(msg("*", msg("foo", msg("+", msg("five", msg("-", msg("thirteen", msg("/", msg("three")))))))))));
        Message expected = msg("map", PersistentList.create(Arrays.asList(msg("*", PersistentList.create(Arrays.asList(msg("foo"))), msg("+", PersistentList.create(Arrays.asList(msg("five"))), msg("-", PersistentList.create(Arrays.asList(msg("thirteen", msg("/", PersistentList.create(Arrays.asList(msg("three")))))))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_bang_correctly() {
        Message m = msg("!", msg("false"));
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_bang_correctly_with_following_messages() {
        Message m = msg("!", msg("false", msg("println")));
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_bang_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = msg("!", PersistentList.create(Arrays.asList(msg("false"))));
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_bang_in_expression() {
        Message m = msg("true", msg("&&", msg("!", msg("false"))));
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("!", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_tilde_correctly() {
        Message m = msg("~", msg("false"));
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_tilde_correctly_with_following_messages() {
        Message m = msg("~", msg("false", msg("println")));
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_tilde_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = msg("~", PersistentList.create(Arrays.asList(msg("false"))));
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_tilde_in_expression() {
        Message m = msg("true", msg("&&", msg("~", msg("false"))));
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("~", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_dollar_correctly() {
        Message m = msg("$", msg("false"));
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_dollar_correctly_with_following_messages() {
        Message m = msg("$", msg("false", msg("println")));
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_dollar_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = msg("$", PersistentList.create(Arrays.asList(msg("false"))));
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test @Ignore
    public void shuffles_unary_dollar_in_expression() {
        Message m = msg("true", msg("&&", msg("$", msg("false"))));
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("$", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    // Precedence
    // Stop at terminators
    // Assignment operators

//     describe("precedence",
//       it("should work correctly for + and *",
//         m = parse("2+3*4")
//         m should == "2 +(3 *(4))"
//       )

//       it("should work correctly for * and +",
//         m = parse("2*3+4")
//         m should == "2 *(3) +(4)"
//       )

//       it("should work correctly for + and * with spaces",
//         m = parse("2 + 3 * 4")
//         m should == "2 +(3 *(4))"
//       )

//       it("should work correctly for * and + with spaces",
//         m = parse("2 * 3 + 4")
//         m should == "2 *(3) +(4)"
//       )

//       it("should work correctly for + and /",
//         m = parse("2+3/4")
//         m should == "2 +(3 /(4))"
//       )

//       it("should work correctly for / and +",
//         m = parse("2/3+4")
//         m should == "2 /(3) +(4)"
//       )

//       it("should work correctly for + and / with spaces",
//         m = parse("2 + 3 / 4")
//         m should == "2 +(3 /(4))"
//       )

//       it("should work correctly for / and + with spaces",
//         m = parse("2 / 3 + 4")
//         m should == "2 /(3) +(4)"
//       )

//       it("should work correctly for - and *",
//         m = parse("2-3*4")
//         m should == "2 -(3 *(4))"
//       )

//       it("should work correctly for * and -",
//         m = parse("2*3-4")
//         m should == "2 *(3) -(4)"
//       )

//       it("should work correctly for - and * with spaces",
//         m = parse("2 - 3 * 4")
//         m should == "2 -(3 *(4))"
//       )

//       it("should work correctly for * and - with spaces",
//         m = parse("2 * 3 - 4")
//         m should == "2 *(3) -(4)"
//       )

//       it("should work correctly for - and /",
//         m = parse("2-3/4")
//         m should == "2 -(3 /(4))"
//       )

//       it("should work correctly for / and -",
//         m = parse("2/3-4")
//         m should == "2 /(3) -(4)"
//       )

//       it("should work correctly for - and / with spaces",
//         m = parse("2 - 3 / 4")
//         m should == "2 -(3 /(4))"
//       )

//       it("should work correctly for / and - with spaces",
//         m = parse("2 / 3 - 4")
//         m should == "2 /(3) -(4)"
//       )

//       it("should work correctly for unary minus",
//         m = parse("20 * -10")
//         m should == "20 *(-(10))"
//       )

//       it("should work correctly for unary plus",
//         m = parse("20 * +10")
//         m should == "20 *(+(10))"
//       )
//     )
//   )

}// MessageShufflerTest
