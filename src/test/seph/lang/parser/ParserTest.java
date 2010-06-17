/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.io.StringReader;

import java.util.Arrays;
import java.util.List;

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
        return parse(input, "<eval>");
    }

    private static Message parse(String input, String sourcename) {
        try {
            return new Parser(new Runtime(), new StringReader(input), sourcename).parseFully().get(0);
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Message> parseAll(String input) {
        try {
            return new Parser(new Runtime(), new StringReader(input), "<eval>").parseFully();
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

    @Test
    public void simple_square_bracket_application_with_arguments_should_work() {
        Message result = parse("[](foo)");
        assertEquals("[]", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void parse_square_brackets_correctly_as_part_of_longer_message_chain() {
        Message result = parse("foo bar(q) [](r)");
        assertEquals("[]", result.next().next().name());
        assertEquals("r", result.next().next().arguments().get(0).name());
        assertNull(result.next().next().next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_should_be_parsed_correctly() {
        Message result = parse("[]");
        assertEquals("[]", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_spaces_inbetween_should_be_parsed_correctly() {
        Message result = parse("[   ]");
        assertEquals("[]", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_argument_should_be_parsed_correctly() {
        Message result = parse("[foo]");
        assertEquals("[]", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_argument_and_spaces_should_be_parsed_correctly() {
        Message result = parse("[   foo   ]");
        assertEquals("[]", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }


    @Test
    public void simple_square_bracket_without_parenthesis_with_arguments_should_be_parsed_correctly() {
        Message result = parse("[foo, bar]");
        assertEquals("[]", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertEquals("bar", result.arguments().get(1).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_can_be_parsed_with_terminators_inside() {
        Message result = parse("[bar, \nfoo(quux)]");
        assertEquals("[]", result.name());
        assertEquals("bar", result.arguments().get(0).name());
        assertEquals("foo", result.arguments().get(1).name());
        assertEquals("quux", result.arguments().get(1).arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void parses_square_brackets_correctly_after_an_identifier() {
        Message result = parse("foo[bar, quux]");
        assertEquals("foo", result.name());
        assertEquals("[]", result.next().name());
        assertEquals("bar", result.next().arguments().get(0).name());
        assertEquals("quux", result.next().arguments().get(1).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_square_brackets_correctly_after_an_identifier_and_a_space() {
        Message result = parse("foo [bar, quux]");
        assertEquals("foo", result.name());
        assertEquals("[]", result.next().name());
        assertEquals("bar", result.next().arguments().get(0).name());
        assertEquals("quux", result.next().arguments().get(1).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_square_brackets_correctly_inside_a_function_application() {
        Message result = parse("foo([bar, quux])");
        assertEquals("foo", result.name());
        assertEquals("[]", result.arguments().get(0).name());
        assertEquals("bar", result.arguments().get(0).arguments().get(0).name());
        assertEquals("quux", result.arguments().get(0).arguments().get(1).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_application_should_be_parsed_correctly() {
        Message result = parse("{}()");
        assertEquals("{}", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_application_with_arguments_should_work() {
        Message result = parse("{}(foo)");
        assertEquals("{}", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void parse_curly_brackets_correctly_as_part_of_longer_message_chain() {
        Message result = parse("foo bar(q) {}(r)");
        assertEquals("{}", result.next().next().name());
        assertEquals("r", result.next().next().arguments().get(0).name());
        assertNull(result.next().next().next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_should_be_parsed_correctly() {
        Message result = parse("{}");
        assertEquals("{}", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_spaces_inbetween_should_be_parsed_correctly() {
        Message result = parse("{   }");
        assertEquals("{}", result.name());
        assertEquals(Arrays.<Message>asList(), result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_argument_should_be_parsed_correctly() {
        Message result = parse("{foo}");
        assertEquals("{}", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_argument_and_spaces_should_be_parsed_correctly() {
        Message result = parse("{   foo   }");
        assertEquals("{}", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertNull(result.next());
    }


    @Test
    public void simple_curly_bracket_without_parenthesis_with_arguments_should_be_parsed_correctly() {
        Message result = parse("{foo, bar}");
        assertEquals("{}", result.name());
        assertEquals("foo", result.arguments().get(0).name());
        assertEquals("bar", result.arguments().get(1).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_can_be_parsed_with_terminators_inside() {
        Message result = parse("{bar, \nfoo(quux)}");
        assertEquals("{}", result.name());
        assertEquals("bar", result.arguments().get(0).name());
        assertEquals("foo", result.arguments().get(1).name());
        assertEquals("quux", result.arguments().get(1).arguments().get(0).name());
        assertNull(result.next());
    }

    @Test
    public void parses_curly_brackets_correctly_after_an_identifier() {
        Message result = parse("foo{bar, quux}");
        assertEquals("foo", result.name());
        assertEquals("{}", result.next().name());
        assertEquals("bar", result.next().arguments().get(0).name());
        assertEquals("quux", result.next().arguments().get(1).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_curly_brackets_correctly_after_an_identifier_and_a_space() {
        Message result = parse("foo {bar, quux}");
        assertEquals("foo", result.name());
        assertEquals("{}", result.next().name());
        assertEquals("bar", result.next().arguments().get(0).name());
        assertEquals("quux", result.next().arguments().get(1).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_curly_brackets_correctly_inside_a_function_application() {
        Message result = parse("foo({bar, quux})");
        assertEquals("foo", result.name());
        assertEquals("{}", result.arguments().get(0).name());
        assertEquals("bar", result.arguments().get(0).arguments().get(0).name());
        assertEquals("quux", result.arguments().get(0).arguments().get(1).name());
        assertNull(result.next());
    }

    @Test
    public void parses_the_toplevel_with_commas() {
        List<Message> result = parseAll("foo,\nbar: method");
        assertEquals("foo", result.get(0).name());
        assertNull(result.get(0).next());
        assertEquals("bar:", result.get(1).name());
        assertEquals("method", result.get(1).next().name());
    }

    @Test
    public void parses_set_literal_as_a_set_literal() {
        Message result = parse("#{bar, quux}");
        assertEquals("set", result.name());
        assertEquals("bar", result.arguments().get(0).name());
        assertEquals("quux", result.arguments().get(1).name());
        assertNull(result.next());
    }

    @Test
    public void parsing_an_empty_thing_generates_a_correct_filename_line_and_position() {
        Message result = parse("", "foo.sp");
        assertEquals("foo.sp", result.filename());
        assertEquals(0, result.line());
        assertEquals(0, result.position());
    }

    @Test
    public void parsing_a_message_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n blarg", "fox.sp");
        assertEquals("fox.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(1, result.position());
    }

    @Test
    public void parsing_a_terminator_generates_a_correct_filename_line_and_position() {
        Message result = parse("\nblarg\n\nbra", "fox.sp").next();
        assertEquals("fox.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(-1, result.position());
    }

    @Test
    public void parsing_a_text_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n\n   \"blargus\"", "box.sp");
        assertEquals("box.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(3, result.position());
    }

    @Test
    public void parsing_an_operator_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n\n   ::", "bax.sp");
        assertEquals("bax.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(3, result.position());

        result = parse("\n\n\n ::(bar)", "bax.sp");
        assertEquals("bax.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(1, result.position());
    }

    @Test
    public void parsing_an_empty_message_send_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n\n          (blarg)", "aaa.sp");
        assertEquals("aaa.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(10, result.position());
    }

    @Test
    public void parsing_a_square_message_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n    [foo]", "aab.sp");
        assertEquals("aab.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(4, result.position());
    }

    @Test
    public void parsing_a_curly_message_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n   {foo}", "aac.sp");
        assertEquals("aac.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(3, result.position());
    }

    @Test
    public void parsing_a_set_message_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n  #{foo}", "aad.sp");
        assertEquals("aad.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(2, result.position());
    }

    @Test
    public void parses_a_string_using_alternative_syntax_correctly() {
        Message result = parse(" #[blargus hello \"something else\" - he]");

        assertTrue("Result should be a literal message", result.isLiteral());
        SephObject literal = result.literal();
        assertEquals(Text.class, literal.getClass());
        assertEquals("blargus hello \"something else\" - he", ((Text)literal).text());
        assertNull(result.next());
    }

    @Test
    public void parses_plus_operator() {
        Message result = parse(" +-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("+-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_plus_operator_with_args() {
        Message result = parse(" +-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("+-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_minus_operator() {
        Message result = parse(" --+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("--+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_minus_operator_with_args() {
        Message result = parse(" --+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("--+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_asterisk_operator() {
        Message result = parse(" *-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("*-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_asterisk_operator_with_args() {
        Message result = parse(" *-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("*-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_percent_operator() {
        Message result = parse(" %-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("%-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_percent_operator_with_args() {
        Message result = parse(" %-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("%-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_lt_operator() {
        Message result = parse(" <-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("<-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_lt_operator_with_args() {
        Message result = parse(" <-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("<-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_gt_operator() {
        Message result = parse(" >-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals(">-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_gt_operator_with_args() {
        Message result = parse(" >-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals(">-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_bang_operator() {
        Message result = parse(" !-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("!-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_bang_operator_with_args() {
        Message result = parse(" !-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("!-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_question_mark_operator() {
        Message result = parse(" ?-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("?-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_question_mark_operator_with_args() {
        Message result = parse(" ?-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("?-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_tilde_operator() {
        Message result = parse(" ~-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("~-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_tilde_operator_with_args() {
        Message result = parse(" ~-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("~-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_ampersand_operator() {
        Message result = parse(" &-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("&-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_ampersand_operator_with_args() {
        Message result = parse(" &-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("&-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_pipe_operator() {
        Message result = parse(" |-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("|-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_pipe_operator_with_args() {
        Message result = parse(" |-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("|-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_caret_operator() {
        Message result = parse(" ^-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("^-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_caret_operator_with_args() {
        Message result = parse(" ^-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("^-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_dollar_operator() {
        Message result = parse(" $-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("$-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_dollar_operator_with_args() {
        Message result = parse(" $-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("$-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_eq_operator() {
        Message result = parse(" =-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("=-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_eq_operator_with_args() {
        Message result = parse(" =-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("=-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_at_operator() {
        Message result = parse(" @-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("@-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_at_operator_with_args() {
        Message result = parse(" @-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("@-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_tick_operator() {
        Message result = parse(" '-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("'-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_tick_operator_with_args() {
        Message result = parse(" '-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("'-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_backtick_operator() {
        Message result = parse(" `-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("`-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_backtick_operator_with_args() {
        Message result = parse(" `-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("`-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parses_slash_operator() {
        Message result = parse(" /-+*%<>!!?~&|^$$=@'`//:# foo");

        assertEquals("/-+*%<>!!?~&|^$$=@'`//:#", result.name());
    }

    @Test
    public void parses_slash_operator_with_args() {
        Message result = parse(" /-+*%<>!!?~&|^$$=@'`//:#(foo)");

        assertEquals("/-+*%<>!!?~&|^$$=@'`//:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void parse_simple_comment() {
        Message result = parse("foo bar ; blarg foo \nsomething else");

        assertEquals("foo", result.name());
        assertEquals("bar", result.next().name());
        assertEquals("something", result.next().next().next().name());
        assertEquals("else", result.next().next().next().next().name());
    }

    @Test
    public void parses_octothorpe_operator() {
        Message result = parse(" #-+*%<>!!?~&|^$$=@'`:# foo");

        assertEquals("#-+*%<>!!?~&|^$$=@'`:#", result.name());
    }

    @Test
    public void parses_octothorpe_operator_with_args() {
        Message result = parse(" #-+*%<>!!?~&|^$$=@'`:#(foo)");

        assertEquals("#-+*%<>!!?~&|^$$=@'`:#", result.name());
        assertEquals("foo", result.arguments().get(0).name());
    }

    @Test
    public void separates_a_slash_from_octothorpe_operator() {
        Message result = parse(" #-+*%<>!!?~&|^$$=@'`:#/ foo");

        assertEquals("#-+*%<>!!?~&|^$$=@'`:#", result.name());
    }


    // TODO:
    // - parse: 
    //   #/
    //   #r
    // - numbers
    // - parse terminator
    // - parse range
    // - parse : followed by number
    // - handle string interpolation
    // - handle all string escapes
    // - errors

    /*
  describe("curly brackets",
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
  describe("square brackets",
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
    */    
}// ParserTest
