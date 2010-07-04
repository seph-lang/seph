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



    // Operator: <=>

    @Test
    public void compare_operator_should_be_translated_correctly_as_argument() {
        Message m = msg("method", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", msg("bar"))))));
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void compare_operator_should_be_translated_correctly_as_nested_argument() {
        Message m = msg("method1", PersistentList.create(Arrays.asList(msg("method2", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", msg("bar")))))))));
        Message expected = msg("method1", PersistentList.create(Arrays.asList(msg("method2", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar")))))))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void compare_operator_should_be_translated_correctly_within_something_else() {
        Message m = msg("method", PersistentList.create(Arrays.asList(msg("n"), msg("if", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", msg("bar"))), msg("n"), msg("n")))))));
        Message expected = msg("method", PersistentList.create(Arrays.asList(msg("n"), msg("if", PersistentList.create(Arrays.asList(msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar"))))), msg("n"), msg("n")))))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void compare_operator_should_be_translated_correctly_in_simple_version() {
        Message m = msg("foo", msg("<=>", msg("bar")));
        Message expected = msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

    @Test
    public void compare_operator_should_not_be_translated_when_already_given_argument() {
        Message m = msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar")))));
        Message expected = msg("foo", msg("<=>", PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(shuffle(m), is(equalTo(expected)));
    }

//     describe("<",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1<2)")
//         m should == "method(1 <(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1<2))")
//         m should == "method(method(1 <(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1<2, n, n))")
//         m should == "method(n, if(1 <(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1<2")
//         m should == "1 <(2)"
//       )

//       it("should be translated correctly in infix, starting with letter",
//         m = parse("a<2")
//         m should == "a <(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1<(2)")
//         m should == "1 <(2)"

//         m = parse("1 <(2)")
//         m should == "1 <(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 < 2")
//         m should == "1 <(2)"
//       )
//     )

//     describe(">",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1>2)")
//         m should == "method(1 >(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1>2))")
//         m should == "method(method(1 >(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1>2, n, n))")
//         m should == "method(n, if(1 >(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1>2")
//         m should == "1 >(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1>(2)")
//         m should == "1 >(2)"

//         m = parse("1 >(2)")
//         m should == "1 >(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 > 2")
//         m should == "1 >(2)"
//       )
//     )

//     describe("<=",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1<=2)")
//         m should == "method(1 <=(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1<=2))")
//         m should == "method(method(1 <=(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1<=2, n, n))")
//         m should == "method(n, if(1 <=(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1<=2")
//         m should == "1 <=(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1<=(2)")
//         m should == "1 <=(2)"

//         m = parse("1 <=(2)")
//         m should == "1 <=(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 <= 2")
//         m should == "1 <=(2)"
//       )
//     )

//     describe(">=",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1>=2)")
//         m should == "method(1 >=(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1>=2))")
//         m should == "method(method(1 >=(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1>=2, n, n))")
//         m should == "method(n, if(1 >=(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1>=2")
//         m should == "1 >=(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1>=(2)")
//         m should == "1 >=(2)"

//         m = parse("1 >=(2)")
//         m should == "1 >=(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 >= 2")
//         m should == "1 >=(2)"
//       )
//     )

//     describe("≤",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1 ≤ 2)")
//         m should == "method(1 ≤(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1 ≤ 2))")
//         m should == "method(method(1 ≤(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1 ≤ 2, n, n))")
//         m should == "method(n, if(1 ≤(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1 ≤ 2")
//         m should == "1 ≤(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1 ≤(2)")
//         m should == "1 ≤(2)"

//         m = parse("1 ≤(2)")
//         m should == "1 ≤(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 ≤ 2")
//         m should == "1 ≤(2)"
//       )
//     )

//     describe("≥",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1 ≥ 2)")
//         m should == "method(1 ≥(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1 ≥ 2))")
//         m should == "method(method(1 ≥(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1 ≥ 2, n, n))")
//         m should == "method(n, if(1 ≥(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1 ≥ 2")
//         m should == "1 ≥(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1 ≥(2)")
//         m should == "1 ≥(2)"

//         m = parse("1 ≥(2)")
//         m should == "1 ≥(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 ≥ 2")
//         m should == "1 ≥(2)"
//       )
//     )

//     describe("!=",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1!=2)")
//         m should == "method(1 !=(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1!=2))")
//         m should == "method(method(1 !=(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1!=2, n, n))")
//         m should == "method(n, if(1 !=(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1!=2")
//         m should == "1 !=(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1!=(2)")
//         m should == "1 !=(2)"

//         m = parse("1 !=(2)")
//         m should == "1 !=(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 != 2")
//         m should == "1 !=(2)"
//       )
//     )

//     describe("≠",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1 ≠ 2)")
//         m should == "method(1 ≠(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1 ≠ 2))")
//         m should == "method(method(1 ≠(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1 ≠ 2, n, n))")
//         m should == "method(n, if(1 ≠(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1 ≠ 2")
//         m should == "1 ≠(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1 ≠(2)")
//         m should == "1 ≠(2)"

//         m = parse("1 ≠(2)")
//         m should == "1 ≠(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 ≠ 2")
//         m should == "1 ≠(2)"
//       )
//     )

//     describe("==",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1==2)")
//         m should == "method(1 ==(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1==2))")
//         m should == "method(method(1 ==(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1==2, n, n))")
//         m should == "method(n, if(1 ==(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1==2")
//         m should == "1 ==(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1==(2)")
//         m should == "1 ==(2)"

//         m = parse("1 ==(2)")
//         m should == "1 ==(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 == 2")
//         m should == "1 ==(2)"
//       )
//     )

//     describe("===",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1===2)")
//         m should == "method(1 ===(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1===2))")
//         m should == "method(method(1 ===(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1===2, n, n))")
//         m should == "method(n, if(1 ===(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1===2")
//         m should == "1 ===(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1===(2)")
//         m should == "1 ===(2)"

//         m = parse("1 ===(2)")
//         m should == "1 ===(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 === 2")
//         m should == "1 ===(2)"
//       )
//     )

//     describe("=~",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1=~2)")
//         m should == "method(1 =~(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1=~2))")
//         m should == "method(method(1 =~(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1=~2, n, n))")
//         m should == "method(n, if(1 =~(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1=~2")
//         m should == "1 =~(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1=~(2)")
//         m should == "1 =~(2)"

//         m = parse("1 =~(2)")
//         m should == "1 =~(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 =~ 2")
//         m should == "1 =~(2)"
//       )
//     )

//     describe("!~",
//       it("should be translated correctly inside a method definition",
//         m = parse("method(1!~2)")
//         m should == "method(1 !~(2))"
//       )

//       it("should be translated correctly inside a nested method definition",
//         m = parse("method(method(1!~2))")
//         m should == "method(method(1 !~(2)))"
//       )

//       it("should be translated correctly inside a method definition with something else",
//         m = parse("method(n, if(1!~2, n, n))")
//         m should == "method(n, if(1 !~(2), n, n))"
//       )

//       it("should be translated correctly in infix",
//         m = parse("1!~2")
//         m should == "1 !~(2)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("1!~(2)")
//         m should == "1 !~(2)"

//         m = parse("1 !~(2)")
//         m should == "1 !~(2)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("1 !~ 2")
//         m should == "1 !~(2)"
//       )
//     )


//     describe("unary -",
//       it("should parse correctly for a simple case",
//         m = parse("-1")
//         m should == "-(1)"
//       )

//       it("should parse correctly for a simple case with message s) after",
//         m = parse("-1 println")
//         m should == "-(1) println"
//       )

//       it("should parse correctly for a simple case with message s) after and parenthesis",
//         m = parse("-(1) println")
//         m should == "-(1) println"
//       )

//       it("should parse correctly for a larger number",
//         m = parse("-12342353453")
//         m should == "-(12342353453)"
//       )

//       it("should parse correctly several times over",
//         m = parse("- -(1)")
//         m should == "-(-(1))"
//       )
//     )

//     describe("unary binary operators",
//       it("should work for a simple expression",
//         m = parse("map(*2)")
//         m should == "map(*(2))"
//       )

//       it("should work for a more complicated expression",
//         m = parse("map(*4+5-13/3)")
//         m should == "map(*(4) +(5) -(13 /(3)))"
//       )
//     )

//     describe("-",
//       it("should be translated correctly in infix",
//         m = parse("2-1")
//         m should == "2 -(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2-(1)")
//         m should == "2 -(1)"

//         m = parse("2 -(1)")
//         m should == "2 -(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 - 1")
//         m should == "2 -(1)"
//       )
//     )

//     describe("+",
//       it("should be translated correctly in infix",
//         m = parse("2+1")
//         m should == "2 +(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2+(1)")
//         m should == "2 +(1)"

//         m = parse("2 +(1)")
//         m should == "2 +(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 + 1")
//         m should == "2 +(1)"
//       )

//       it("should work correctly when given as an argument with newlines",
//         m = parse("[1,2] fold(
// +
// )") should == "[](1, 2) fold(+)"
//       )
//     )

//     describe("*",
//       it("should be translated correctly in infix",
//         m = parse("2*1")
//         m should == "2 *(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2*(1)")
//         m should == "2 *(1)"

//         m = parse("2 *(1)")
//         m should == "2 *(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 * 1")
//         m should == "2 *(1)"
//       )
//     )

//     describe("**",
//       it("should be translated correctly in infix",
//         m = parse("2**1")
//         m should == "2 **(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2**(1)")
//         m should == "2 **(1)"

//         m = parse("2 **(1)")
//         m should == "2 **(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ** 1")
//         m should == "2 **(1)"
//       )
//     )

//     describe("/",
//       it("should be translated correctly in infix",
//         m = parse("2/1")
//         m should == "2 /(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2/(1)")
//         m should == "2 /(1)"

//         m = parse("2 /(1)")
//         m should == "2 /(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 / 1")
//         m should == "2 /(1)"
//       )
//     )

//     describe("%",
//       it("should be translated correctly in infix",
//         m = parse("2%1")
//         m should == "2 %(1)"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2%(1)")
//         m should == "2 %(1)"

//         m = parse("2 %(1)")
//         m should == "2 %(1)"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 % 1")
//         m should == "2 %(1)"
//       )
//     )


//     describe("=>",
//       it("should be correctly translated in infix",
//         m = parse("2=>1")
//         m should == "2 =>(1)"

//         m = parse("\"foo\"=>\"bar\"")
//         m should == "\"foo\" =>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2=>(1)")
//         m should == "2 =>(1)"

//         m = parse("2 =>(1)")
//         m should == "2 =>(1)"

//         m = parse("\"foo\"=>(\"bar\")")
//         m should == "\"foo\" =>(\"bar\")"

//         m = parse("\"foo\" =>(\"bar\")")
//         m should == "\"foo\" =>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 => 1")
//         m should == "2 =>(1)"

//         m = parse("\"foo\" => \"bar\"")
//         m should == "\"foo\" =>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 => 1 => 0")
//         m should == "2 =>(1) =>(0)"

//         m = parse("\"foo\" => \"bar\" => \"quux\"")
//         m should == "\"foo\" =>(\"bar\") =>(\"quux\")"
//       )
//     )

//     describe("..",
//       it("should be correctly translated in infix",
//         m = parse("2..1")
//         m should == "2 ..(1)"

//         m = parse("\"foo\"..\"bar\"")
//         m should == "\"foo\" ..(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2..(1)")
//         m should == "2 ..(1)"

//         m = parse("2 ..(1)")
//         m should == "2 ..(1)"

//         m = parse("\"foo\"..(\"bar\")")
//         m should == "\"foo\" ..(\"bar\")"

//         m = parse("\"foo\" ..(\"bar\")")
//         m should == "\"foo\" ..(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 .. 1")
//         m should == "2 ..(1)"

//         m = parse("\"foo\" .. \"bar\"")
//         m should == "\"foo\" ..(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 .. 1 .. 0")
//         m should == "2 ..(1) ..(0)"

//         m = parse("\"foo\" .. \"bar\" .. \"quux\"")
//         m should == "\"foo\" ..(\"bar\") ..(\"quux\")"
//       )
//     )

//     describe("...",
//       it("should be correctly translated in infix",
//         m = parse("2...1")
//         m should == "2 ...(1)"

//         m = parse("\"foo\"...\"bar\"")
//         m should == "\"foo\" ...(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2...(1)")
//         m should == "2 ...(1)"

//         m = parse("2 ...(1)")
//         m should == "2 ...(1)"

//         m = parse("\"foo\"...(\"bar\")")
//         m should == "\"foo\" ...(\"bar\")"

//         m = parse("\"foo\" ...(\"bar\")")
//         m should == "\"foo\" ...(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ... 1")
//         m should == "2 ...(1)"

//         m = parse("\"foo\" ... \"bar\"")
//         m should == "\"foo\" ...(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ... 1 ... 0")
//         m should == "2 ...(1) ...(0)"

//         m = parse("\"foo\" ... \"bar\" ... \"quux\"")
//         m should == "\"foo\" ...(\"bar\") ...(\"quux\")"
//       )
//     )

//     describe("<<",
//       it("should be correctly translated in infix",
//         m = parse("2<<1")
//         m should == "2 <<(1)"

//         m = parse("\"foo\"<<\"bar\"")
//         m should == "\"foo\" <<(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2<<(1)")
//         m should == "2 <<(1)"

//         m = parse("2 <<(1)")
//         m should == "2 <<(1)"

//         m = parse("\"foo\"<<(\"bar\")")
//         m should == "\"foo\" <<(\"bar\")"

//         m = parse("\"foo\" <<(\"bar\")")
//         m should == "\"foo\" <<(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 << 1")
//         m should == "2 <<(1)"

//         m = parse("\"foo\" << \"bar\"")
//         m should == "\"foo\" <<(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 << 1 << 0")
//         m should == "2 <<(1) <<(0)"

//         m = parse("\"foo\" << \"bar\" << \"quux\"")
//         m should == "\"foo\" <<(\"bar\") <<(\"quux\")"
//       )
//     )


//     describe(">>",
//       it("should be correctly translated in infix",
//         m = parse("2>>1")
//         m should == "2 >>(1)"

//         m = parse("\"foo\">>\"bar\"")
//         m should == "\"foo\" >>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2>>(1)")
//         m should == "2 >>(1)"

//         m = parse("2 >>(1)")
//         m should == "2 >>(1)"

//         m = parse("\"foo\">>(\"bar\")")
//         m should == "\"foo\" >>(\"bar\")"

//         m = parse("\"foo\" >>(\"bar\")")
//         m should == "\"foo\" >>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 >> 1")
//         m should == "2 >>(1)"

//         m = parse("\"foo\" >> \"bar\"")
//         m should == "\"foo\" >>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 >> 1 >> 0")
//         m should == "2 >>(1) >>(0)"

//         m = parse("\"foo\" >> \"bar\" >> \"quux\"")
//         m should == "\"foo\" >>(\"bar\") >>(\"quux\")"
//       )
//     )

//     describe("&",
//       it("should be correctly translated in infix",
//         m = parse("2&1")
//         m should == "2 &(1)"

//         m = parse("\"foo\"&\"bar\"")
//         m should == "\"foo\" &(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&(1)")
//         m should == "2 &(1)"

//         m = parse("2 &(1)")
//         m should == "2 &(1)"

//         m = parse("\"foo\"&(\"bar\")")
//         m should == "\"foo\" &(\"bar\")"

//         m = parse("\"foo\" &(\"bar\")")
//         m should == "\"foo\" &(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 & 1")
//         m should == "2 &(1)"

//         m = parse("\"foo\" & \"bar\"")
//         m should == "\"foo\" &(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 & 1 & 0")
//         m should == "2 &(1) &(0)"

//         m = parse("\"foo\" & \"bar\" & \"quux\"")
//         m should == "\"foo\" &(\"bar\") &(\"quux\")"
//       )
//     )

//     describe("|",
//       it("should be correctly translated in infix",
//         m = parse("2|1")
//         m should == "2 |(1)"

//         m = parse("\"foo\"|\"bar\"")
//         m should == "\"foo\" |(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2|(1)")
//         m should == "2 |(1)"

//         m = parse("2 |(1)")
//         m should == "2 |(1)"

//         m = parse("\"foo\"|(\"bar\")")
//         m should == "\"foo\" |(\"bar\")"

//         m = parse("\"foo\" |(\"bar\")")
//         m should == "\"foo\" |(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 | 1")
//         m should == "2 |(1)"

//         m = parse("\"foo\" | \"bar\"")
//         m should == "\"foo\" |(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 | 1 | 0")
//         m should == "2 |(1) |(0)"

//         m = parse("\"foo\" | \"bar\" | \"quux\"")
//         m should == "\"foo\" |(\"bar\") |(\"quux\")"
//       )
//     )

//     describe("^",
//       it("should be correctly translated in infix",
//         m = parse("2^1")
//         m should == "2 ^(1)"

//         m = parse("\"foo\"^\"bar\"")
//         m should == "\"foo\" ^(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2^(1)")
//         m should == "2 ^(1)"

//         m = parse("2 ^(1)")
//         m should == "2 ^(1)"

//         m = parse("\"foo\"^(\"bar\")")
//         m should == "\"foo\" ^(\"bar\")"

//         m = parse("\"foo\" ^(\"bar\")")
//         m should == "\"foo\" ^(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ^ 1")
//         m should == "2 ^(1)"

//         m = parse("\"foo\" ^ \"bar\"")
//         m should == "\"foo\" ^(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ^ 1 ^ 0")
//         m should == "2 ^(1) ^(0)"

//         m = parse("\"foo\" ^ \"bar\" ^ \"quux\"")
//         m should == "\"foo\" ^(\"bar\") ^(\"quux\")"
//       )
//     )

//     describe("&&",
//       it("should be correctly translated in infix",
//         m = parse("2&&1")
//         m should == "2 &&(1)"

//         m = parse("\"foo\"&&\"bar\"")
//         m should == "\"foo\" &&(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&&(1)")
//         m should == "2 &&(1)"

//         m = parse("2 &&(1)")
//         m should == "2 &&(1)"

//         m = parse("\"foo\"&&(\"bar\")")
//         m should == "\"foo\" &&(\"bar\")"

//         m = parse("\"foo\" &&(\"bar\")")
//         m should == "\"foo\" &&(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 && 1")
//         m should == "2 &&(1)"

//         m = parse("\"foo\" && \"bar\"")
//         m should == "\"foo\" &&(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 && 1 && 0")
//         m should == "2 &&(1) &&(0)"

//         m = parse("\"foo\" && \"bar\" && \"quux\"")
//         m should == "\"foo\" &&(\"bar\") &&(\"quux\")"
//       )
//     )

//     describe("||",
//       it("should be correctly translated in infix",
//         m = parse("2||1")
//         m should == "2 ||(1)"

//         m = parse("\"foo\"||\"bar\"")
//         m should == "\"foo\" ||(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2||(1)")
//         m should == "2 ||(1)"

//         m = parse("2 ||(1)")
//         m should == "2 ||(1)"

//         m = parse("\"foo\"||(\"bar\")")
//         m should == "\"foo\" ||(\"bar\")"

//         m = parse("\"foo\" ||(\"bar\")")
//         m should == "\"foo\" ||(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 || 1")
//         m should == "2 ||(1)"

//         m = parse("\"foo\" || \"bar\"")
//         m should == "\"foo\" ||(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 || 1 || 0")
//         m should == "2 ||(1) ||(0)"

//         m = parse("\"foo\" || \"bar\" || \"quux\"")
//         m should == "\"foo\" ||(\"bar\") ||(\"quux\")"
//       )
//     )

//     describe("?&",
//       it("should be correctly translated in infix",
//         m = parse("2?&1")
//         m should == "2 ?&(1)"

//         m = parse("\"foo\"?&\"bar\"")
//         m should == "\"foo\" ?&(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2?&(1)")
//         m should == "2 ?&(1)"

//         m = parse("2 ?&(1)")
//         m should == "2 ?&(1)"

//         m = parse("\"foo\"?&(\"bar\")")
//         m should == "\"foo\" ?&(\"bar\")"

//         m = parse("\"foo\" ?&(\"bar\")")
//         m should == "\"foo\" ?&(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ?& 1")
//         m should == "2 ?&(1)"

//         m = parse("\"foo\" ?& \"bar\"")
//         m should == "\"foo\" ?&(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ?& 1 ?& 0")
//         m should == "2 ?&(1) ?&(0)"

//         m = parse("\"foo\" ?& \"bar\" ?& \"quux\"")
//         m should == "\"foo\" ?&(\"bar\") ?&(\"quux\")"
//       )
//     )

//     describe("?|",
//       it("should be correctly translated in infix",
//         m = parse("2?|1")
//         m should == "2 ?|(1)"

//         m = parse("\"foo\"?|\"bar\"")
//         m should == "\"foo\" ?|(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2?|(1)")
//         m should == "2 ?|(1)"

//         m = parse("2 ?|(1)")
//         m should == "2 ?|(1)"

//         m = parse("\"foo\"?|(\"bar\")")
//         m should == "\"foo\" ?|(\"bar\")"

//         m = parse("\"foo\" ?|(\"bar\")")
//         m should == "\"foo\" ?|(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ?| 1")
//         m should == "2 ?|(1)"

//         m = parse("\"foo\" ?| \"bar\"")
//         m should == "\"foo\" ?|(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ?| 1 ?| 0")
//         m should == "2 ?|(1) ?|(0)"

//         m = parse("\"foo\" ?| \"bar\" ?| \"quux\"")
//         m should == "\"foo\" ?|(\"bar\") ?|(\"quux\")"
//       )
//     )

//     describe("or",
//       it("should be translated correctly with parenthesis",
//         m = parse("2 or(1)")
//         m should == "2 or(1)"

//         m = parse("\"foo\" or(\"bar\")")
//         m should == "\"foo\" or(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 or 1")
//         m should == "2 or(1)"

//         m = parse("\"foo\" or \"bar\"")
//         m should == "\"foo\" or(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 or 1 or 0")
//         m should == "2 or(1) or(0)"

//         m = parse("\"foo\" or \"bar\" or \"quux\"")
//         m should == "\"foo\" or(\"bar\") or(\"quux\")"
//       )
//     )

//     describe("and",
//       it("should be translated correctly with parenthesis",
//         m = parse("2 and(1)")
//         m should == "2 and(1)"

//         m = parse("\"foo\" and(\"bar\")")
//         m should == "\"foo\" and(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 and 1")
//         m should == "2 and(1)"

//         m = parse("\"foo\" and \"bar\"")
//         m should == "\"foo\" and(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 and 1 and 0")
//         m should == "2 and(1) and(0)"

//         m = parse("\"foo\" and \"bar\" and \"quux\"")
//         m should == "\"foo\" and(\"bar\") and(\"quux\")"
//       )
//     )

//     describe("!",
//       it("should work in a simple unary position",
//         m = parse("!false")
//         m should == "!(false)"
//       )

//       it("should work in a simple unary position with space",
//         m = parse("! false")
//         m should == "!(false)"
//       )

//       it("should work with parenthesis",
//         m = parse("!(false)")
//         m should == "!(false)"
//       )

//       it("should work in an expression",
//         m = parse("true && !false")
//         m should == "true &&(!(false))"
//       )
//     )

//     describe("~",
//       it("should work in a simple unary position",
//         m = parse("~false")
//         m should == "~(false)"
//       )

//       it("should work in a simple unary position with space",
//         m = parse("~ false")
//         m should == "~(false)"
//       )

//       it("should work with parenthesis",
//         m = parse("~(false)")
//         m should == "~(false)"
//       )

//       it("should work in an expression",
//         m = parse("true && ~false")
//         m should == "true &&(~(false))"
//       )

//       it("should work as a binary operator",
//         m = parse("true ~ false")
//         m should == "true ~(false)"
//       )
//     )

//     describe("$",
//       it("should work in a simple unary position",
//         m = parse("$false")
//         m should == "$(false)"
//       )

//       it("should work in a simple unary position with space",
//         m = parse("$ false")
//         m should == "$(false)"
//       )

//       it("should work with parenthesis",
//         m = parse("$(false)")
//         m should == "$(false)"
//       )

//       it("should work in an expression",
//         m = parse("true && $false")
//         m should == "true &&($(false))"
//       )

//       it("should work as a binary operator",
//         m = parse("true $ false")
//         m should == "true $(false)"
//       )
//     )

//     describe("->",
//       it("should be correctly translated in infix",
//         m = parse("2->1")
//         m should == "2 ->(1)"

//         m = parse("\"foo\"->\"bar\"")
//         m should == "\"foo\" ->(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2->(1)")
//         m should == "2 ->(1)"

//         m = parse("2 ->(1)")
//         m should == "2 ->(1)"

//         m = parse("\"foo\"->(\"bar\")")
//         m should == "\"foo\" ->(\"bar\")"

//         m = parse("\"foo\" ->(\"bar\")")
//         m should == "\"foo\" ->(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 -> 1")
//         m should == "2 ->(1)"

//         m = parse("\"foo\" -> \"bar\"")
//         m should == "\"foo\" ->(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 -> 1 -> 0")
//         m should == "2 ->(1) ->(0)"

//         m = parse("\"foo\" -> \"bar\" -> \"quux\"")
//         m should == "\"foo\" ->(\"bar\") ->(\"quux\")"
//       )
//     )

//     describe("+>",
//       it("should be correctly translated in infix",
//         m = parse("2+>1")
//         m should == "2 +>(1)"

//         m = parse("\"foo\"+>\"bar\"")
//         m should == "\"foo\" +>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2+>(1)")
//         m should == "2 +>(1)"

//         m = parse("2 +>(1)")
//         m should == "2 +>(1)"

//         m = parse("\"foo\"+>(\"bar\")")
//         m should == "\"foo\" +>(\"bar\")"

//         m = parse("\"foo\" +>(\"bar\")")
//         m should == "\"foo\" +>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 +> 1")
//         m should == "2 +>(1)"

//         m = parse("\"foo\" +> \"bar\"")
//         m should == "\"foo\" +>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 +> 1 +> 0")
//         m should == "2 +>(1) +>(0)"

//         m = parse("\"foo\" +> \"bar\" +> \"quux\"")
//         m should == "\"foo\" +>(\"bar\") +>(\"quux\")"
//       )
//     )

//     describe("!>",
//       it("should be correctly translated in infix",
//         m = parse("2!>1")
//         m should == "2 !>(1)"

//         m = parse("\"foo\"!>\"bar\"")
//         m should == "\"foo\" !>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2!>(1)")
//         m should == "2 !>(1)"

//         m = parse("2 !>(1)")
//         m should == "2 !>(1)"

//         m = parse("\"foo\"!>(\"bar\")")
//         m should == "\"foo\" !>(\"bar\")"

//         m = parse("\"foo\" !>(\"bar\")")
//         m should == "\"foo\" !>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 !> 1")
//         m should == "2 !>(1)"

//         m = parse("\"foo\" !> \"bar\"")
//         m should == "\"foo\" !>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 !> 1 !> 0")
//         m should == "2 !>(1) !>(0)"

//         m = parse("\"foo\" !> \"bar\" !> \"quux\"")
//         m should == "\"foo\" !>(\"bar\") !>(\"quux\")"
//       )
//     )

//     describe("<>",
//       it("should be correctly translated in infix",
//         m = parse("2<>1")
//         m should == "2 <>(1)"

//         m = parse("\"foo\"<>\"bar\"")
//         m should == "\"foo\" <>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2<>(1)")
//         m should == "2 <>(1)"

//         m = parse("2 <>(1)")
//         m should == "2 <>(1)"

//         m = parse("\"foo\"<>(\"bar\")")
//         m should == "\"foo\" <>(\"bar\")"

//         m = parse("\"foo\" <>(\"bar\")")
//         m should == "\"foo\" <>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 <> 1")
//         m should == "2 <>(1)"

//         m = parse("\"foo\" <> \"bar\"")
//         m should == "\"foo\" <>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 <> 1 <> 0")
//         m should == "2 <>(1) <>(0)"

//         m = parse("\"foo\" <> \"bar\" <> \"quux\"")
//         m should == "\"foo\" <>(\"bar\") <>(\"quux\")"
//       )
//     )

//     describe("&>",
//       it("should be correctly translated in infix",
//         m = parse("2&>1")
//         m should == "2 &>(1)"

//         m = parse("\"foo\"&>\"bar\"")
//         m should == "\"foo\" &>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&>(1)")
//         m should == "2 &>(1)"

//         m = parse("2 &>(1)")
//         m should == "2 &>(1)"

//         m = parse("\"foo\"&>(\"bar\")")
//         m should == "\"foo\" &>(\"bar\")"

//         m = parse("\"foo\" &>(\"bar\")")
//         m should == "\"foo\" &>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 &> 1")
//         m should == "2 &>(1)"

//         m = parse("\"foo\" &> \"bar\"")
//         m should == "\"foo\" &>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 &> 1 &> 0")
//         m should == "2 &>(1) &>(0)"

//         m = parse("\"foo\" &> \"bar\" &> \"quux\"")
//         m should == "\"foo\" &>(\"bar\") &>(\"quux\")"
//       )
//     )


//     describe("%>",
//       it("should be correctly translated in infix",
//         m = parse("2%>1")
//         m should == "2 %>(1)"

//         m = parse("\"foo\"%>\"bar\"")
//         m should == "\"foo\" %>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2%>(1)")
//         m should == "2 %>(1)"

//         m = parse("2 %>(1)")
//         m should == "2 %>(1)"

//         m = parse("\"foo\"%>(\"bar\")")
//         m should == "\"foo\" %>(\"bar\")"

//         m = parse("\"foo\" %>(\"bar\")")
//         m should == "\"foo\" %>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 %> 1")
//         m should == "2 %>(1)"

//         m = parse("\"foo\" %> \"bar\"")
//         m should == "\"foo\" %>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 %> 1 %> 0")
//         m should == "2 %>(1) %>(0)"

//         m = parse("\"foo\" %> \"bar\" %> \"quux\"")
//         m should == "\"foo\" %>(\"bar\") %>(\"quux\")"
//       )
//     )

//     describe("#>",
//       it("should be correctly translated in infix",
//         m = parse("2#>1")
//         m should == "2 #>(1)"

//         m = parse("\"foo\"#>\"bar\"")
//         m should == "\"foo\" #>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2#>(1)")
//         m should == "2 #>(1)"

//         m = parse("2 #>(1)")
//         m should == "2 #>(1)"

//         m = parse("\"foo\"#>(\"bar\")")
//         m should == "\"foo\" #>(\"bar\")"

//         m = parse("\"foo\" #>(\"bar\")")
//         m should == "\"foo\" #>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 #> 1")
//         m should == "2 #>(1)"

//         m = parse("\"foo\" #> \"bar\"")
//         m should == "\"foo\" #>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 #> 1 #> 0")
//         m should == "2 #>(1) #>(0)"

//         m = parse("\"foo\" #> \"bar\" #> \"quux\"")
//         m should == "\"foo\" #>(\"bar\") #>(\"quux\")"
//       )
//     )

//     describe("@>",
//       it("should be correctly translated in infix",
//         m = parse("2@>1")
//         m should == "2 @>(1)"

//         m = parse("\"foo\"@>\"bar\"")
//         m should == "\"foo\" @>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2@>(1)")
//         m should == "2 @>(1)"

//         m = parse("2 @>(1)")
//         m should == "2 @>(1)"

//         m = parse("\"foo\"@>(\"bar\")")
//         m should == "\"foo\" @>(\"bar\")"

//         m = parse("\"foo\" @>(\"bar\")")
//         m should == "\"foo\" @>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 @> 1")
//         m should == "2 @>(1)"

//         m = parse("\"foo\" @> \"bar\"")
//         m should == "\"foo\" @>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 @> 1 @> 0")
//         m should == "2 @>(1) @>(0)"

//         m = parse("\"foo\" @> \"bar\" @> \"quux\"")
//         m should == "\"foo\" @>(\"bar\") @>(\"quux\")"
//       )
//     )

//     describe("/>",
//       it("should be correctly translated in infix",
//         m = parse("2/>1")
//         m should == "2 />(1)"

//         m = parse("\"foo\"/>\"bar\"")
//         m should == "\"foo\" />(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2/>(1)")
//         m should == "2 />(1)"

//         m = parse("2 />(1)")
//         m should == "2 />(1)"

//         m = parse("\"foo\"/>(\"bar\")")
//         m should == "\"foo\" />(\"bar\")"

//         m = parse("\"foo\" />(\"bar\")")
//         m should == "\"foo\" />(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 /> 1")
//         m should == "2 />(1)"

//         m = parse("\"foo\" /> \"bar\"")
//         m should == "\"foo\" />(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 /> 1 /> 0")
//         m should == "2 />(1) />(0)"

//         m = parse("\"foo\" /> \"bar\" /> \"quux\"")
//         m should == "\"foo\" />(\"bar\") />(\"quux\")"
//       )
//     )

//     describe("*>",
//       it("should be correctly translated in infix",
//         m = parse("2*>1")
//         m should == "2 *>(1)"

//         m = parse("\"foo\"*>\"bar\"")
//         m should == "\"foo\" *>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2*>(1)")
//         m should == "2 *>(1)"

//         m = parse("2 *>(1)")
//         m should == "2 *>(1)"

//         m = parse("\"foo\"*>(\"bar\")")
//         m should == "\"foo\" *>(\"bar\")"

//         m = parse("\"foo\" *>(\"bar\")")
//         m should == "\"foo\" *>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 *> 1")
//         m should == "2 *>(1)"

//         m = parse("\"foo\" *> \"bar\"")
//         m should == "\"foo\" *>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 *> 1 *> 0")
//         m should == "2 *>(1) *>(0)"

//         m = parse("\"foo\" *> \"bar\" *> \"quux\"")
//         m should == "\"foo\" *>(\"bar\") *>(\"quux\")"
//       )
//     )


//     describe("?>",
//       it("should be correctly translated in infix",
//         m = parse("2?>1")
//         m should == "2 ?>(1)"

//         m = parse("\"foo\"?>\"bar\"")
//         m should == "\"foo\" ?>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2?>(1)")
//         m should == "2 ?>(1)"

//         m = parse("2 ?>(1)")
//         m should == "2 ?>(1)"

//         m = parse("\"foo\"?>(\"bar\")")
//         m should == "\"foo\" ?>(\"bar\")"

//         m = parse("\"foo\" ?>(\"bar\")")
//         m should == "\"foo\" ?>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ?> 1")
//         m should == "2 ?>(1)"

//         m = parse("\"foo\" ?> \"bar\"")
//         m should == "\"foo\" ?>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ?> 1 ?> 0")
//         m should == "2 ?>(1) ?>(0)"

//         m = parse("\"foo\" ?> \"bar\" ?> \"quux\"")
//         m should == "\"foo\" ?>(\"bar\") ?>(\"quux\")"
//       )
//     )

//     describe("|>",
//       it("should be correctly translated in infix",
//         m = parse("2|>1")
//         m should == "2 |>(1)"

//         m = parse("\"foo\"|>\"bar\"")
//         m should == "\"foo\" |>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2|>(1)")
//         m should == "2 |>(1)"

//         m = parse("2 |>(1)")
//         m should == "2 |>(1)"

//         m = parse("\"foo\"|>(\"bar\")")
//         m should == "\"foo\" |>(\"bar\")"

//         m = parse("\"foo\" |>(\"bar\")")
//         m should == "\"foo\" |>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 |> 1")
//         m should == "2 |>(1)"

//         m = parse("\"foo\" |> \"bar\"")
//         m should == "\"foo\" |>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 |> 1 |> 0")
//         m should == "2 |>(1) |>(0)"

//         m = parse("\"foo\" |> \"bar\" |> \"quux\"")
//         m should == "\"foo\" |>(\"bar\") |>(\"quux\")"
//       )
//     )

//     describe("^>",
//       it("should be correctly translated in infix",
//         m = parse("2^>1")
//         m should == "2 ^>(1)"

//         m = parse("\"foo\"^>\"bar\"")
//         m should == "\"foo\" ^>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2^>(1)")
//         m should == "2 ^>(1)"

//         m = parse("2 ^>(1)")
//         m should == "2 ^>(1)"

//         m = parse("\"foo\"^>(\"bar\")")
//         m should == "\"foo\" ^>(\"bar\")"

//         m = parse("\"foo\" ^>(\"bar\")")
//         m should == "\"foo\" ^>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ^> 1")
//         m should == "2 ^>(1)"

//         m = parse("\"foo\" ^> \"bar\"")
//         m should == "\"foo\" ^>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ^> 1 ^> 0")
//         m should == "2 ^>(1) ^>(0)"

//         m = parse("\"foo\" ^> \"bar\" ^> \"quux\"")
//         m should == "\"foo\" ^>(\"bar\") ^>(\"quux\")"
//       )
//     )

//     describe("~>",
//       it("should be correctly translated in infix",
//         m = parse("2~>1")
//         m should == "2 ~>(1)"

//         m = parse("\"foo\"~>\"bar\"")
//         m should == "\"foo\" ~>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2~>(1)")
//         m should == "2 ~>(1)"

//         m = parse("2 ~>(1)")
//         m should == "2 ~>(1)"

//         m = parse("\"foo\"~>(\"bar\")")
//         m should == "\"foo\" ~>(\"bar\")"

//         m = parse("\"foo\" ~>(\"bar\")")
//         m should == "\"foo\" ~>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ~> 1")
//         m should == "2 ~>(1)"

//         m = parse("\"foo\" ~> \"bar\"")
//         m should == "\"foo\" ~>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ~> 1 ~> 0")
//         m should == "2 ~>(1) ~>(0)"

//         m = parse("\"foo\" ~> \"bar\" ~> \"quux\"")
//         m should == "\"foo\" ~>(\"bar\") ~>(\"quux\")"
//       )
//     )

//     describe("->>",
//       it("should be correctly translated in infix",
//         m = parse("2->>1")
//         m should == "2 ->>(1)"

//         m = parse("\"foo\"->>\"bar\"")
//         m should == "\"foo\" ->>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2->>(1)")
//         m should == "2 ->>(1)"

//         m = parse("2 ->>(1)")
//         m should == "2 ->>(1)"

//         m = parse("\"foo\"->>(\"bar\")")
//         m should == "\"foo\" ->>(\"bar\")"

//         m = parse("\"foo\" ->>(\"bar\")")
//         m should == "\"foo\" ->>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ->> 1")
//         m should == "2 ->>(1)"

//         m = parse("\"foo\" ->> \"bar\"")
//         m should == "\"foo\" ->>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ->> 1 ->> 0")
//         m should == "2 ->>(1) ->>(0)"

//         m = parse("\"foo\" ->> \"bar\" ->> \"quux\"")
//         m should == "\"foo\" ->>(\"bar\") ->>(\"quux\")"
//       )
//     )

//     describe("+>>",
//       it("should be correctly translated in infix",
//         m = parse("2+>>1")
//         m should == "2 +>>(1)"

//         m = parse("\"foo\"+>>\"bar\"")
//         m should == "\"foo\" +>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2+>>(1)")
//         m should == "2 +>>(1)"

//         m = parse("2 +>>(1)")
//         m should == "2 +>>(1)"

//         m = parse("\"foo\"+>>(\"bar\")")
//         m should == "\"foo\" +>>(\"bar\")"

//         m = parse("\"foo\" +>>(\"bar\")")
//         m should == "\"foo\" +>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 +>> 1")
//         m should == "2 +>>(1)"

//         m = parse("\"foo\" +>> \"bar\"")
//         m should == "\"foo\" +>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 +>> 1 +>> 0")
//         m should == "2 +>>(1) +>>(0)"

//         m = parse("\"foo\" +>> \"bar\" +>> \"quux\"")
//         m should == "\"foo\" +>>(\"bar\") +>>(\"quux\")"
//       )
//     )

//     describe("!>>",
//       it("should be correctly translated in infix",
//         m = parse("2!>>1")
//         m should == "2 !>>(1)"

//         m = parse("\"foo\"!>>\"bar\"")
//         m should == "\"foo\" !>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2!>>(1)")
//         m should == "2 !>>(1)"

//         m = parse("2 !>>(1)")
//         m should == "2 !>>(1)"

//         m = parse("\"foo\"!>>(\"bar\")")
//         m should == "\"foo\" !>>(\"bar\")"

//         m = parse("\"foo\" !>>(\"bar\")")
//         m should == "\"foo\" !>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 !>> 1")
//         m should == "2 !>>(1)"

//         m = parse("\"foo\" !>> \"bar\"")
//         m should == "\"foo\" !>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 !>> 1 !>> 0")
//         m should == "2 !>>(1) !>>(0)"

//         m = parse("\"foo\" !>> \"bar\" !>> \"quux\"")
//         m should == "\"foo\" !>>(\"bar\") !>>(\"quux\")"
//       )
//     )

//     describe("<>>",
//       it("should be correctly translated in infix",
//         m = parse("2<>>1")
//         m should == "2 <>>(1)"

//         m = parse("\"foo\"<>>\"bar\"")
//         m should == "\"foo\" <>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2<>>(1)")
//         m should == "2 <>>(1)"

//         m = parse("2 <>>(1)")
//         m should == "2 <>>(1)"

//         m = parse("\"foo\"<>>(\"bar\")")
//         m should == "\"foo\" <>>(\"bar\")"

//         m = parse("\"foo\" <>>(\"bar\")")
//         m should == "\"foo\" <>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 <>> 1")
//         m should == "2 <>>(1)"

//         m = parse("\"foo\" <>> \"bar\"")
//         m should == "\"foo\" <>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 <>> 1 <>> 0")
//         m should == "2 <>>(1) <>>(0)"

//         m = parse("\"foo\" <>> \"bar\" <>> \"quux\"")
//         m should == "\"foo\" <>>(\"bar\") <>>(\"quux\")"
//       )
//     )

//     describe("&>>",
//       it("should be correctly translated in infix",
//         m = parse("2&>>1")
//         m should == "2 &>>(1)"

//         m = parse("\"foo\"&>>\"bar\"")
//         m should == "\"foo\" &>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&>>(1)")
//         m should == "2 &>>(1)"

//         m = parse("2 &>>(1)")
//         m should == "2 &>>(1)"

//         m = parse("\"foo\"&>>(\"bar\")")
//         m should == "\"foo\" &>>(\"bar\")"

//         m = parse("\"foo\" &>>(\"bar\")")
//         m should == "\"foo\" &>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 &>> 1")
//         m should == "2 &>>(1)"

//         m = parse("\"foo\" &>> \"bar\"")
//         m should == "\"foo\" &>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 &>> 1 &>> 0")
//         m should == "2 &>>(1) &>>(0)"

//         m = parse("\"foo\" &>> \"bar\" &>> \"quux\"")
//         m should == "\"foo\" &>>(\"bar\") &>>(\"quux\")"
//       )
//     )


//     describe("%>>",
//       it("should be correctly translated in infix",
//         m = parse("2%>>1")
//         m should == "2 %>>(1)"

//         m = parse("\"foo\"%>>\"bar\"")
//         m should == "\"foo\" %>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2%>>(1)")
//         m should == "2 %>>(1)"

//         m = parse("2 %>>(1)")
//         m should == "2 %>>(1)"

//         m = parse("\"foo\"%>>(\"bar\")")
//         m should == "\"foo\" %>>(\"bar\")"

//         m = parse("\"foo\" %>>(\"bar\")")
//         m should == "\"foo\" %>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 %>> 1")
//         m should == "2 %>>(1)"

//         m = parse("\"foo\" %>> \"bar\"")
//         m should == "\"foo\" %>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 %>> 1 %>> 0")
//         m should == "2 %>>(1) %>>(0)"

//         m = parse("\"foo\" %>> \"bar\" %>> \"quux\"")
//         m should == "\"foo\" %>>(\"bar\") %>>(\"quux\")"
//       )
//     )

//     describe("#>>",
//       it("should be correctly translated in infix",
//         m = parse("2#>>1")
//         m should == "2 #>>(1)"

//         m = parse("\"foo\"#>>\"bar\"")
//         m should == "\"foo\" #>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2#>>(1)")
//         m should == "2 #>>(1)"

//         m = parse("2 #>>(1)")
//         m should == "2 #>>(1)"

//         m = parse("\"foo\"#>>(\"bar\")")
//         m should == "\"foo\" #>>(\"bar\")"

//         m = parse("\"foo\" #>>(\"bar\")")
//         m should == "\"foo\" #>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 #>> 1")
//         m should == "2 #>>(1)"

//         m = parse("\"foo\" #>> \"bar\"")
//         m should == "\"foo\" #>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 #>> 1 #>> 0")
//         m should == "2 #>>(1) #>>(0)"

//         m = parse("\"foo\" #>> \"bar\" #>> \"quux\"")
//         m should == "\"foo\" #>>(\"bar\") #>>(\"quux\")"
//       )
//     )

//     describe("@>>",
//       it("should be correctly translated in infix",
//         m = parse("2@>>1")
//         m should == "2 @>>(1)"

//         m = parse("\"foo\"@>>\"bar\"")
//         m should == "\"foo\" @>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2@>>(1)")
//         m should == "2 @>>(1)"

//         m = parse("2 @>>(1)")
//         m should == "2 @>>(1)"

//         m = parse("\"foo\"@>>(\"bar\")")
//         m should == "\"foo\" @>>(\"bar\")"

//         m = parse("\"foo\" @>>(\"bar\")")
//         m should == "\"foo\" @>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 @>> 1")
//         m should == "2 @>>(1)"

//         m = parse("\"foo\" @>> \"bar\"")
//         m should == "\"foo\" @>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 @>> 1 @>> 0")
//         m should == "2 @>>(1) @>>(0)"

//         m = parse("\"foo\" @>> \"bar\" @>> \"quux\"")
//         m should == "\"foo\" @>>(\"bar\") @>>(\"quux\")"
//       )
//     )

//     describe("/>>",
//       it("should be correctly translated in infix",
//         m = parse("2/>>1")
//         m should == "2 />>(1)"

//         m = parse("\"foo\"/>>\"bar\"")
//         m should == "\"foo\" />>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2/>>(1)")
//         m should == "2 />>(1)"

//         m = parse("2 />>(1)")
//         m should == "2 />>(1)"

//         m = parse("\"foo\"/>>(\"bar\")")
//         m should == "\"foo\" />>(\"bar\")"

//         m = parse("\"foo\" />>(\"bar\")")
//         m should == "\"foo\" />>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 />> 1")
//         m should == "2 />>(1)"

//         m = parse("\"foo\" />> \"bar\"")
//         m should == "\"foo\" />>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 />> 1 />> 0")
//         m should == "2 />>(1) />>(0)"

//         m = parse("\"foo\" />> \"bar\" />> \"quux\"")
//         m should == "\"foo\" />>(\"bar\") />>(\"quux\")"
//       )
//     )

//     describe("*>>",
//       it("should be correctly translated in infix",
//         m = parse("2*>>1")
//         m should == "2 *>>(1)"

//         m = parse("\"foo\"*>>\"bar\"")
//         m should == "\"foo\" *>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2*>>(1)")
//         m should == "2 *>>(1)"

//         m = parse("2 *>>(1)")
//         m should == "2 *>>(1)"

//         m = parse("\"foo\"*>>(\"bar\")")
//         m should == "\"foo\" *>>(\"bar\")"

//         m = parse("\"foo\" *>>(\"bar\")")
//         m should == "\"foo\" *>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 *>> 1")
//         m should == "2 *>>(1)"

//         m = parse("\"foo\" *>> \"bar\"")
//         m should == "\"foo\" *>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 *>> 1 *>> 0")
//         m should == "2 *>>(1) *>>(0)"

//         m = parse("\"foo\" *>> \"bar\" *>> \"quux\"")
//         m should == "\"foo\" *>>(\"bar\") *>>(\"quux\")"
//       )
//     )


//     describe("?>>",
//       it("should be correctly translated in infix",
//         m = parse("2?>>1")
//         m should == "2 ?>>(1)"

//         m = parse("\"foo\"?>>\"bar\"")
//         m should == "\"foo\" ?>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2?>>(1)")
//         m should == "2 ?>>(1)"

//         m = parse("2 ?>>(1)")
//         m should == "2 ?>>(1)"

//         m = parse("\"foo\"?>>(\"bar\")")
//         m should == "\"foo\" ?>>(\"bar\")"

//         m = parse("\"foo\" ?>>(\"bar\")")
//         m should == "\"foo\" ?>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ?>> 1")
//         m should == "2 ?>>(1)"

//         m = parse("\"foo\" ?>> \"bar\"")
//         m should == "\"foo\" ?>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ?>> 1 ?>> 0")
//         m should == "2 ?>>(1) ?>>(0)"

//         m = parse("\"foo\" ?>> \"bar\" ?>> \"quux\"")
//         m should == "\"foo\" ?>>(\"bar\") ?>>(\"quux\")"
//       )
//     )

//     describe("|>>",
//       it("should be correctly translated in infix",
//         m = parse("2|>>1")
//         m should == "2 |>>(1)"

//         m = parse("\"foo\"|>>\"bar\"")
//         m should == "\"foo\" |>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2|>>(1)")
//         m should == "2 |>>(1)"

//         m = parse("2 |>>(1)")
//         m should == "2 |>>(1)"

//         m = parse("\"foo\"|>>(\"bar\")")
//         m should == "\"foo\" |>>(\"bar\")"

//         m = parse("\"foo\" |>>(\"bar\")")
//         m should == "\"foo\" |>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 |>> 1")
//         m should == "2 |>>(1)"

//         m = parse("\"foo\" |>> \"bar\"")
//         m should == "\"foo\" |>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 |>> 1 |>> 0")
//         m should == "2 |>>(1) |>>(0)"

//         m = parse("\"foo\" |>> \"bar\" |>> \"quux\"")
//         m should == "\"foo\" |>>(\"bar\") |>>(\"quux\")"
//       )
//     )

//     describe("^>>",
//       it("should be correctly translated in infix",
//         m = parse("2^>>1")
//         m should == "2 ^>>(1)"

//         m = parse("\"foo\"^>>\"bar\"")
//         m should == "\"foo\" ^>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2^>>(1)")
//         m should == "2 ^>>(1)"

//         m = parse("2 ^>>(1)")
//         m should == "2 ^>>(1)"

//         m = parse("\"foo\"^>>(\"bar\")")
//         m should == "\"foo\" ^>>(\"bar\")"

//         m = parse("\"foo\" ^>>(\"bar\")")
//         m should == "\"foo\" ^>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ^>> 1")
//         m should == "2 ^>>(1)"

//         m = parse("\"foo\" ^>> \"bar\"")
//         m should == "\"foo\" ^>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ^>> 1 ^>> 0")
//         m should == "2 ^>>(1) ^>>(0)"

//         m = parse("\"foo\" ^>> \"bar\" ^>> \"quux\"")
//         m should == "\"foo\" ^>>(\"bar\") ^>>(\"quux\")"
//       )
//     )

//     describe("~>>",
//       it("should be correctly translated in infix",
//         m = parse("2~>>1")
//         m should == "2 ~>>(1)"

//         m = parse("\"foo\"~>>\"bar\"")
//         m should == "\"foo\" ~>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2~>>(1)")
//         m should == "2 ~>>(1)"

//         m = parse("2 ~>>(1)")
//         m should == "2 ~>>(1)"

//         m = parse("\"foo\"~>>(\"bar\")")
//         m should == "\"foo\" ~>>(\"bar\")"

//         m = parse("\"foo\" ~>>(\"bar\")")
//         m should == "\"foo\" ~>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ~>> 1")
//         m should == "2 ~>>(1)"

//         m = parse("\"foo\" ~>> \"bar\"")
//         m should == "\"foo\" ~>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ~>> 1 ~>> 0")
//         m should == "2 ~>>(1) ~>>(0)"

//         m = parse("\"foo\" ~>> \"bar\" ~>> \"quux\"")
//         m should == "\"foo\" ~>>(\"bar\") ~>>(\"quux\")"
//       )
//     )

//     describe("=>>",
//       it("should be correctly translated in infix",
//         m = parse("2=>>1")
//         m should == "2 =>>(1)"

//         m = parse("\"foo\"=>>\"bar\"")
//         m should == "\"foo\" =>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2=>>(1)")
//         m should == "2 =>>(1)"

//         m = parse("2 =>>(1)")
//         m should == "2 =>>(1)"

//         m = parse("\"foo\"=>>(\"bar\")")
//         m should == "\"foo\" =>>(\"bar\")"

//         m = parse("\"foo\" =>>(\"bar\")")
//         m should == "\"foo\" =>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 =>> 1")
//         m should == "2 =>>(1)"

//         m = parse("\"foo\" =>> \"bar\"")
//         m should == "\"foo\" =>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 =>> 1 =>> 0")
//         m should == "2 =>>(1) =>>(0)"

//         m = parse("\"foo\" =>> \"bar\" =>> \"quux\"")
//         m should == "\"foo\" =>>(\"bar\") =>>(\"quux\")"
//       )
//     )

//     describe("**>",
//       it("should be correctly translated in infix",
//         m = parse("2**>1")
//         m should == "2 **>(1)"

//         m = parse("\"foo\"**>\"bar\"")
//         m should == "\"foo\" **>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2**>(1)")
//         m should == "2 **>(1)"

//         m = parse("2 **>(1)")
//         m should == "2 **>(1)"

//         m = parse("\"foo\"**>(\"bar\")")
//         m should == "\"foo\" **>(\"bar\")"

//         m = parse("\"foo\" **>(\"bar\")")
//         m should == "\"foo\" **>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 **> 1")
//         m should == "2 **>(1)"

//         m = parse("\"foo\" **> \"bar\"")
//         m should == "\"foo\" **>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 **> 1 **> 0")
//         m should == "2 **>(1) **>(0)"

//         m = parse("\"foo\" **> \"bar\" **> \"quux\"")
//         m should == "\"foo\" **>(\"bar\") **>(\"quux\")"
//       )
//     )

//     describe("**>>",
//       it("should be correctly translated in infix",
//         m = parse("2**>>1")
//         m should == "2 **>>(1)"

//         m = parse("\"foo\"**>>\"bar\"")
//         m should == "\"foo\" **>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2**>>(1)")
//         m should == "2 **>>(1)"

//         m = parse("2 **>>(1)")
//         m should == "2 **>>(1)"

//         m = parse("\"foo\"**>>(\"bar\")")
//         m should == "\"foo\" **>>(\"bar\")"

//         m = parse("\"foo\" **>>(\"bar\")")
//         m should == "\"foo\" **>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 **>> 1")
//         m should == "2 **>>(1)"

//         m = parse("\"foo\" **>> \"bar\"")
//         m should == "\"foo\" **>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 **>> 1 **>> 0")
//         m should == "2 **>>(1) **>>(0)"

//         m = parse("\"foo\" **>> \"bar\" **>> \"quux\"")
//         m should == "\"foo\" **>>(\"bar\") **>>(\"quux\")"
//       )
//     )

//     describe("&&>",
//       it("should be correctly translated in infix",
//         m = parse("2&&>1")
//         m should == "2 &&>(1)"

//         m = parse("\"foo\"&&>\"bar\"")
//         m should == "\"foo\" &&>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&&>(1)")
//         m should == "2 &&>(1)"

//         m = parse("2 &&>(1)")
//         m should == "2 &&>(1)"

//         m = parse("\"foo\"&&>(\"bar\")")
//         m should == "\"foo\" &&>(\"bar\")"

//         m = parse("\"foo\" &&>(\"bar\")")
//         m should == "\"foo\" &&>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 &&> 1")
//         m should == "2 &&>(1)"

//         m = parse("\"foo\" &&> \"bar\"")
//         m should == "\"foo\" &&>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 &&> 1 &&> 0")
//         m should == "2 &&>(1) &&>(0)"

//         m = parse("\"foo\" &&> \"bar\" &&> \"quux\"")
//         m should == "\"foo\" &&>(\"bar\") &&>(\"quux\")"
//       )
//     )

//     describe("&&>>",
//       it("should be correctly translated in infix",
//         m = parse("2&&>>1")
//         m should == "2 &&>>(1)"

//         m = parse("\"foo\"&&>>\"bar\"")
//         m should == "\"foo\" &&>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2&&>>(1)")
//         m should == "2 &&>>(1)"

//         m = parse("2 &&>>(1)")
//         m should == "2 &&>>(1)"

//         m = parse("\"foo\"&&>>(\"bar\")")
//         m should == "\"foo\" &&>>(\"bar\")"

//         m = parse("\"foo\" &&>>(\"bar\")")
//         m should == "\"foo\" &&>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 &&>> 1")
//         m should == "2 &&>>(1)"

//         m = parse("\"foo\" &&>> \"bar\"")
//         m should == "\"foo\" &&>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 &&>> 1 &&>> 0")
//         m should == "2 &&>>(1) &&>>(0)"

//         m = parse("\"foo\" &&>> \"bar\" &&>> \"quux\"")
//         m should == "\"foo\" &&>>(\"bar\") &&>>(\"quux\")"
//       )
//     )

//     describe("||>",
//       it("should be correctly translated in infix",
//         m = parse("2||>1")
//         m should == "2 ||>(1)"

//         m = parse("\"foo\"||>\"bar\"")
//         m should == "\"foo\" ||>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2||>(1)")
//         m should == "2 ||>(1)"

//         m = parse("2 ||>(1)")
//         m should == "2 ||>(1)"

//         m = parse("\"foo\"||>(\"bar\")")
//         m should == "\"foo\" ||>(\"bar\")"

//         m = parse("\"foo\" ||>(\"bar\")")
//         m should == "\"foo\" ||>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ||> 1")
//         m should == "2 ||>(1)"

//         m = parse("\"foo\" ||> \"bar\"")
//         m should == "\"foo\" ||>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ||> 1 ||> 0")
//         m should == "2 ||>(1) ||>(0)"

//         m = parse("\"foo\" ||> \"bar\" ||> \"quux\"")
//         m should == "\"foo\" ||>(\"bar\") ||>(\"quux\")"
//       )
//     )

//     describe("||>>",
//       it("should be correctly translated in infix",
//         m = parse("2||>>1")
//         m should == "2 ||>>(1)"

//         m = parse("\"foo\"||>>\"bar\"")
//         m should == "\"foo\" ||>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2||>>(1)")
//         m should == "2 ||>>(1)"

//         m = parse("2 ||>>(1)")
//         m should == "2 ||>>(1)"

//         m = parse("\"foo\"||>>(\"bar\")")
//         m should == "\"foo\" ||>>(\"bar\")"

//         m = parse("\"foo\" ||>>(\"bar\")")
//         m should == "\"foo\" ||>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 ||>> 1")
//         m should == "2 ||>>(1)"

//         m = parse("\"foo\" ||>> \"bar\"")
//         m should == "\"foo\" ||>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 ||>> 1 ||>> 0")
//         m should == "2 ||>>(1) ||>>(0)"

//         m = parse("\"foo\" ||>> \"bar\" ||>> \"quux\"")
//         m should == "\"foo\" ||>>(\"bar\") ||>>(\"quux\")"
//       )
//     )

//     describe("$>",
//       it("should be correctly translated in infix",
//         m = parse("2$>1")
//         m should == "2 $>(1)"

//         m = parse("\"foo\"$>\"bar\"")
//         m should == "\"foo\" $>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2$>(1)")
//         m should == "2 $>(1)"

//         m = parse("2 $>(1)")
//         m should == "2 $>(1)"

//         m = parse("\"foo\"$>(\"bar\")")
//         m should == "\"foo\" $>(\"bar\")"

//         m = parse("\"foo\" $>(\"bar\")")
//         m should == "\"foo\" $>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 $> 1")
//         m should == "2 $>(1)"

//         m = parse("\"foo\" $> \"bar\"")
//         m should == "\"foo\" $>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 $> 1 $> 0")
//         m should == "2 $>(1) $>(0)"

//         m = parse("\"foo\" $> \"bar\" $> \"quux\"")
//         m should == "\"foo\" $>(\"bar\") $>(\"quux\")"
//       )
//     )

//     describe("$>>",
//       it("should be correctly translated in infix",
//         m = parse("2$>>1")
//         m should == "2 $>>(1)"

//         m = parse("\"foo\"$>>\"bar\"")
//         m should == "\"foo\" $>>(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2$>>(1)")
//         m should == "2 $>>(1)"

//         m = parse("2 $>>(1)")
//         m should == "2 $>>(1)"

//         m = parse("\"foo\"$>>(\"bar\")")
//         m should == "\"foo\" $>>(\"bar\")"

//         m = parse("\"foo\" $>>(\"bar\")")
//         m should == "\"foo\" $>>(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 $>> 1")
//         m should == "2 $>>(1)"

//         m = parse("\"foo\" $>> \"bar\"")
//         m should == "\"foo\" $>>(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 $>> 1 $>> 0")
//         m should == "2 $>>(1) $>>(0)"

//         m = parse("\"foo\" $>> \"bar\" $>> \"quux\"")
//         m should == "\"foo\" $>>(\"bar\") $>>(\"quux\")"
//       )
//     )

//     describe("<->",
//       it("should be correctly translated in infix",
//         m = parse("2<->1")
//         m should == "2 <->(1)"

//         m = parse("\"foo\"<->\"bar\"")
//         m should == "\"foo\" <->(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2<->(1)")
//         m should == "2 <->(1)"

//         m = parse("2 <->(1)")
//         m should == "2 <->(1)"

//         m = parse("\"foo\"<->(\"bar\")")
//         m should == "\"foo\" <->(\"bar\")"

//         m = parse("\"foo\" <->(\"bar\")")
//         m should == "\"foo\" <->(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 <-> 1")
//         m should == "2 <->(1)"

//         m = parse("\"foo\" <-> \"bar\"")
//         m should == "\"foo\" <->(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 <-> 1 <-> 0")
//         m should == "2 <->(1) <->(0)"

//         m = parse("\"foo\" <-> \"bar\" <-> \"quux\"")
//         m should == "\"foo\" <->(\"bar\") <->(\"quux\")"
//       )
//     )

//     describe("<-",
//       it("should be correctly translated in infix",
//         m = parse("2<-1")
//         m should == "2 <-(1)"

//         m = parse("\"foo\"<-\"bar\"")
//         m should == "\"foo\" <-(\"bar\")"
//       )

//       it("should be translated correctly with parenthesis",
//         m = parse("2<-(1)")
//         m should == "2 <-(1)"

//         m = parse("2 <-(1)")
//         m should == "2 <-(1)"

//         m = parse("\"foo\"<-(\"bar\")")
//         m should == "\"foo\" <-(\"bar\")"

//         m = parse("\"foo\" <-(\"bar\")")
//         m should == "\"foo\" <-(\"bar\")"
//       )

//       it("should be translated correctly with spaces",
//         m = parse("2 <- 1")
//         m should == "2 <-(1)"

//         m = parse("\"foo\" <- \"bar\"")
//         m should == "\"foo\" <-(\"bar\")"
//       )

//       it("should be translated correctly when chained",
//         m = parse("2 <- 1 <- 0")
//         m should == "2 <-(1) <-(0)"

//         m = parse("\"foo\" <- \"bar\" <- \"quux\"")
//         m should == "\"foo\" <-(\"bar\") <-(\"quux\")"
//       )
//     )

//     describe("inverted ::",

//       it("should be correctly translated",
//         m = parse("foo :: bar")
//         m should == "bar ::(foo)"
//       )

//       it("should receive just one argument",
//         o = Origin mimic
//         o cell("::") = macro(call)
//         (foo :: o) arguments length should == 1
//       )
//     )

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
