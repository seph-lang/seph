/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.io.StringReader;

import java.util.Arrays;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.Text;
import seph.lang.ast.Message;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ParserTest {
    private static Message parse(String input) {
        try {
            return new Parser(new Runtime(), new StringReader(input)).parseFully();
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void ensure_class_exists() {
        assertEquals("seph.lang.parser.Parser", Parser.class.getName());
    }

    @Test
    public void an_empty_string_should_become_a_terminator_message() {
        Message result = parse("");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_spaces_should_become_a_terminator_message() {
        Message result = parse("  ");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_unicode_spaces_should_become_a_terminator_message() {
        Message result = parse("\u0009\u0009\u000b\u000c");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_a_message_should_become_that_message() {
        Message result = parse("foo");
        assertEquals("foo", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_two_messages_should_become_a_message_chain() {
        Message result = parse("foo bar");
        assertEquals("foo", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertEquals("bar", result.next().name());
        assertEquals(Arrays.<Message>asList(), result.next().arguments());
        assertNull(result.next().next());
    }

    @Test
    public void octothorp_followed_by_bang_will_be_interpreted_as_a_comment() {
        Message result = parse("#!/foo/bar 123\nfoo");
        assertEquals("foo", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_as_a_terminator() {
        Message result = parse("\n");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_two_newlines_as_one_terminator() {
        Message result = parse("\n\n");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_as_one_terminator() {
        Message result = parse(".");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_and_a_newline_as_one_terminator() {
        Message result = parse(".\n");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_and_a_period_as_one_terminator() {
        Message result = parse("\n.");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_period_between_newlines_as_one_terminator() {
        Message result = parse("\n.\n");
        assertEquals(".", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void doesnt_parse_a_line_ending_with_a_preceding_slash_as_a_terminator() {
        Message result = parse("foo\\\nbar");
        assertEquals("foo", result.name());
        assertEquals("bar", result.next().name());
        assertNull(result.next().next());
    }

    @Test
    public void doesnt_parse_a_line_ending_with_a_preceding_slash_surrounded_by_spaces_as_a_terminator() {
        Message result = parse("foo    \\\n    bar");
        assertEquals("foo", result.name());
        assertEquals("bar", result.next().name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_a_string_with_a_newline_in_it_correctly() {
        Message result = parse("\"foo\nbar\"");
        assertTrue("Result should be a literal message", result.isLiteral());
        SephObject literal = result.literal();
        assertEquals(Text.class, literal.getClass());
        assertEquals("foo\nbar", ((Text)literal).text());
        assertNull(result.next());
    }

    @Test
    public void parses_a_string_with_an_escaped_newline_correctly() {
        Message result = parse("\"foo\\\nbar\"");
        SephObject literal = result.literal();
        assertEquals("foobar", ((Text)literal).text());
    }

    @Test
    public void should_allow_identifier_starting_with_colon() {
        Message result = parse(":foo");
        assertEquals(":foo", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void uses_two_semi_colons() {
        Message result = parse("::");
        assertEquals("::", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test @Ignore("This test should really be in tests for operator shuffling, not in the parser test")
    public void separates_two_colons_into_a_message_send() {
        Message result = parse("::foo");
        assertEquals("::", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test @Ignore("This test should really be in tests for operator shuffling, not in the parser test")
    public void separates_three_colons_into_a_message_send() {
        Message result = parse(":::foo");
        assertEquals(":::", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void allows_a_single_colon_as_identifier() {
        Message result = parse(":");
        assertEquals(":", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_ended_with_a_colon() {
        Message result = parse("foo:");
        assertEquals("foo:", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_split_with_a_colon() {
        Message result = parse("foo:bar");
        assertEquals("foo:bar", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_split_with_more_than_one_colon() {
        Message result = parse("foo::bar");
        assertEquals("foo::bar", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_interspersed_with_colons() {
        Message result = parse("f:o:o:b:a:r");
        assertEquals("f:o:o:b:a:r", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_a_question_mark_followed_by_a_colon() {
        Message result = parse("foo?:");
        assertEquals("foo?:", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parens_without_preceeding_message_becomes_the_identity_message() {
        Message result = parse("(foo)");
        assertEquals("", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_application_should_be_parsed_correctly() {
        Message result = parse("[]()");
        assertEquals("[]", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }
    


    /*
  describe("square brackets",
    it("should be parsed correctly in regular message passing syntax",
      m = parse("[]()")
      m should == "[]"
    )

    it("should be parsed correctly in regular message passing syntax with arguments",
      m = parse("[](123)")
      m should == "[](123)"
    )

    it("should be parsed correctly in regular message passing syntax with arguments and receiver",
      m = parse("foo bar(1) [](123)")
      m should == "foo bar(1) [](123)"
    )

    it("should be parsed correctly when empty",
      m = parse("[]")
      m should == "[]"
    )

    it("should be parsed correctly when empty with spaces",
      m = parse("[   ]")
      m should == "[]"
    )

    it("should be parsed correctly with argument",
      m = parse("[1]")
      m should == "[](1)"
    )

    it("should be parsed correctly with argument and spaces",
      m = parse("[   1   ]")
      m should == "[](1)"
    )

    it("should be parsed correctly with arguments",
      m = parse("[1, 2]")
      m should == "[](1, 2)"
    )

    it("should be parsed correctly with terminators inside",
      m = parse("[1, \nfoo(24)]")
      m should == "[](1, foo(24))"
    )

    it("should be parsed correctly directly after an identifier",
      m = parse("foo[1, 2]")
      m should == "foo [](1, 2)"
    )

    it("should be parsed correctly with a space directly after an identifier",
      m = parse("foo [1, 2]")
      m should == "foo [](1, 2)"
    )

    it("should be parsed correctly inside a function application",
      m = parse("foo([1, 2])")
      m should == "foo([](1, 2))"
    )

    it("should not parse correctly when mismatched",
      fn(parse("foo([1, 2)]")) should signal(Condition Error Parser Syntax)
      fn(parse("foo([1, 2)]")) should signalArgument(line: 1)
      fn(parse("foo([1, 2)]")) should signalArgument(character: 8)
      fn(parse("foo([1, 2)]")) should signalArgument(expected: "]")
      fn(parse("foo([1, 2)]")) should signalArgument(got: "')'")
    )

    it("should not parse correctly when missing end",
      fn(parse("[1, 2")) should signal(Condition Error Parser Syntax)
      fn(parse("[1, 2")) should signalArgument(line: 1)
      fn(parse("[1, 2")) should signalArgument(character: 5)
      fn(parse("[1, 2")) should signalArgument(expected: "]")
      fn(parse("[1, 2")) should signalArgument(got: "EOF")
    )
  )

  describe("curly brackets",
    it("should be parsed correctly in regular message passing syntax",
      m = parse("{}()")
      m should == "{}"
    )

    it("should be parsed correctly in regular message passing syntax with arguments",
      m = parse("{}(123)")
      m should == "{}(123)"
    )

    it("should be parsed correctly in regular message passing syntax with arguments and receiver",
      m = parse("foo bar(1) {}(123)")
      m should == "foo bar(1) {}(123)"
    )

    it("should be parsed correctly when empty",
      m = parse("{}")
      m should == "{}"
    )

    it("should be parsed correctly when empty with spaces",
      m = parse("{     }")
      m should == "{}"
    )

    it("should be parsed correctly with argument",
      m = parse("{1}")
      m should == "{}(1)"
    )

    it("should be parsed correctly with argument and spaces",
      m = parse("{ 1     }")
      m should == "{}(1)"
    )

    it("should be parsed correctly with arguments",
      m = parse("{1, 2}")
      m should == "{}(1, 2)"
    )

    it("should be parsed correctly with terminators inside",
      m = parse("{1, \nfoo(24)}")
      m should == "{}(1, foo(24))"
    )

    it("should be parsed correctly directly after an identifier",
      m = parse("foo{1, 2}")
      m should == "foo {}(1, 2)"
    )

    it("should be parsed correctly with a space directly after an identifier",
      m = parse("foo {1, 2}")
      m should == "foo {}(1, 2)"
    )

    it("should be parsed correctly inside a function application",
      m = parse("foo({1, 2})")
      m should == "foo({}(1, 2))"
    )

    it("should not parse correctly when mismatched",
      fn(parse("foo({1, 2)}")) should signal(Condition Error Parser Syntax)
      fn(parse("foo({1, 2)}")) should signalArgument(line: 1)
      fn(parse("foo({1, 2)}")) should signalArgument(character: 8)
      fn(parse("foo({1, 2)}")) should signalArgument(expected: "}")
      fn(parse("foo({1, 2)}")) should signalArgument(got: "')'")
    )

    it("should not parse correctly when missing end",
      fn(parse("{1, 2")) should signal(Condition Error Parser Syntax)
      fn(parse("{1, 2")) should signalArgument(line: 1)
      fn(parse("{1, 2")) should signalArgument(character: 5)
      fn(parse("{1, 2")) should signalArgument(expected: "}")
      fn(parse("{1, 2")) should signalArgument(got: "EOF")
    )
  )
    */    
}// ParserTest
