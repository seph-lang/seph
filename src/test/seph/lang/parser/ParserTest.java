/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.io.StringReader;

import org.junit.Test;
import static org.junit.Assert.*;

import seph.lang.Runtime;
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
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_spaces_should_become_a_terminator_message() {
        Message result = parse("  ");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_unicode_spaces_should_become_a_terminator_message() {
        Message result = parse("\u0009\u0009\u000b\u000c");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_a_message_should_become_that_message() {
        Message result = parse("foo");
        assertEquals("foo", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_two_messages_should_become_a_message_chain() {
        Message result = parse("foo bar");
        assertEquals("foo", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertEquals("bar", result.next().name());
        assertArrayEquals(new Message[0], result.next().arguments());
        assertNull(result.next().next());
    }

    @Test
    public void octothorp_followed_by_bang_will_be_interpreted_as_a_comment() {
        Message result = parse("#!/foo/bar 123\nfoo");
        assertEquals("foo", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_as_a_terminator() {
        Message result = parse("\n");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_two_newlines_as_one_terminator() {
        Message result = parse("\n\n");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_as_one_terminator() {
        Message result = parse(".");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_and_a_newline_as_one_terminator() {
        Message result = parse(".\n");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_and_a_period_as_one_terminator() {
        Message result = parse("\n.");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_period_between_newlines_as_one_terminator() {
        Message result = parse("\n.\n");
        assertEquals(".", result.name());
        assertArrayEquals(new Message[0], result.arguments());
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


    /*
  describe("strings",
    it("should parse a string containing newlines",
      m = parse("\"foo\nbar\"")
      m should == "\"foo\nbar\""
    )

    describe("escapes",
      it("should parse a newline as nothing if preceeded with a slash",
        "foo\
bar" should == "foobar"
      )
    )
  )

  describe("parens without preceeding message",
    it("should be translated into identity message",
      m = parse("(1)")
      m should == "(1)"
    )
  )

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

  describe("identifiers",
    it("should be allowed to begin with colon",
      m = parse(":foo")
      m should == ":foo"
    )

    it("should use two colons",
      m = parse("::")
      m should == "::"
    )

    it("should separate two colons",
      m = parse("::foo")
      m should == "::(foo)"
    )

    it("should separate three colons",
      m = parse(":::foo")
      m should == ":::(foo)"
    )

    it("should be allowed to only be a colon",
      m = parse(":")
      m should == ":"
    )

    it("should be allowed to end with colon",
      m = parse("foo:")
      m should == "foo:"
    )

    it("should be allowed to have a colon in the middle",
      m = parse("foo:bar")
      m should == "foo:bar"
    )

    it("should be allowed to have more than one colon in the middle",
      m = parse("foo::bar")
      m should == "foo::bar"

      m = parse("f:o:o:b:a:r")
      m should == "f:o:o:b:a:r"
    )

    it("should be possible to follow a question mark with a colon",
      m = parse("foo?:")
      m should == "foo?:"
    )
  )
    */    
}// ParserTest
