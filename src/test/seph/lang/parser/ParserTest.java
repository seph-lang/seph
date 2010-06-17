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
import seph.lang.ControlFlow;
import seph.lang.ast.Message;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class ParserTest {
    private ControlFlow error;

    private Message parse(String input) {
        return parse(input, "<eval>");
    }

    private Message parse(String input, String sourcename) {
        try {
            return new Parser(new Runtime(), new StringReader(input), sourcename).parseFully().get(0);
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        } catch(ControlFlow cf) {
            this.error = cf;
            return null;
        }
    }

    private List<Message> parseAll(String input) {
        try {
            return new Parser(new Runtime(), new StringReader(input), "<eval>").parseFully();
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        } catch(ControlFlow cf) {
            this.error = cf;
            return null;
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
    public void parsing_a_range_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n        ...", "aae.sp");
        assertEquals("aae.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(8, result.position());
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

    @Test
    public void parses_a_separator() {
        Message result = parse("foo \n.\n bar");

        assertEquals(".", result.next().name());
    }

    @Test
    public void parses_two_dots() {
        Message result = parse("foo..bar");
        assertEquals("..", result.next().name());
    }

    @Test
    public void parses_three_dots() {
        Message result = parse("foo...bar");
        assertEquals("...", result.next().name());
    }

    @Test
    public void parses_four_dots() {
        Message result = parse("foo....bar");
        assertEquals("....", result.next().name());
    }

    @Test
    public void parses_five_dots() {
        Message result = parse("foo.....bar");
        assertEquals(".....", result.next().name());
    }

    @Test
    public void parses_six_dots() {
        Message result = parse("foo......bar");
        assertEquals("......", result.next().name());
    }

    @Test
    public void parses_seven_dots() {
        Message result = parse("foo.......bar");
        assertEquals(".......", result.next().name());
    }

    @Test
    public void parses_eight_dots() {
        Message result = parse("foo........bar");
        assertEquals("........", result.next().name());
    }

    @Test
    public void parses_nine_dots() {
        Message result = parse("foo.........bar");
        assertEquals(".........", result.next().name());
    }

    @Test
    public void parses_ten_dots() {
        Message result = parse("foo..........bar");
        assertEquals("..........", result.next().name());
    }

    @Test
    public void parses_eleven_dots() {
        Message result = parse("foo...........bar");
        assertEquals("...........", result.next().name());
    }

    @Test
    public void parses_twelve_dots() {
        Message result = parse("foo............bar");
        assertEquals("............", result.next().name());
    }

    @Test
    public void parses_thirteen_dots() {
        Message result = parse("foo.............bar");
        assertEquals(".............", result.next().name());
    }

    @Test
    public void parses_a_symbol_with_only_numbers() {
        Message result = parse(" :123666");
        assertEquals(":123666", result.name());
    }

    @Test
    public void handles_text_interpolation_in_double_quotes() {
        Message result = parse("\"foo #{foo bar(\"blarg#{bux}\")} bar\"");

        assertEquals("internal:concatenateText", result.name());
        assertEquals("foo ", ((Text)result.arguments().get(0).literal()).text());
        assertEquals("foo", result.arguments().get(1).name());
        assertEquals("bar", result.arguments().get(1).next().name());
        assertEquals("internal:concatenateText", result.arguments().get(1).next().arguments().get(0).name());
        assertEquals("blarg", ((Text)result.arguments().get(1).next().arguments().get(0).arguments().get(0).literal()).text());
        assertEquals("bux", result.arguments().get(1).next().arguments().get(0).arguments().get(1).name());
        assertEquals(" bar", ((Text)result.arguments().get(2).literal()).text());
    }

    @Test
    public void handles_text_interpolation_in_alt_text_format() {
        Message result = parse("#[foo #{foo bar(\"blarg#{bux}\")} bar]");

        assertEquals("internal:concatenateText", result.name());
        assertEquals("foo ", ((Text)result.arguments().get(0).literal()).text());
        assertEquals("foo", result.arguments().get(1).name());
        assertEquals("bar", result.arguments().get(1).next().name());
        assertEquals("internal:concatenateText", result.arguments().get(1).next().arguments().get(0).name());
        assertEquals("blarg", ((Text)result.arguments().get(1).next().arguments().get(0).arguments().get(0).literal()).text());
        assertEquals("bux", result.arguments().get(1).next().arguments().get(0).arguments().get(1).name());
        assertEquals(" bar", ((Text)result.arguments().get(2).literal()).text());
    }

    @Test
    public void handles_unicode_escape_in_text() {
        Message result = parse("\"foo \\uABCD\"");
        assertEquals("foo \uABCD", ((Text)result.literal()).text());
    }

    @Test
    public void handles_unicode_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\uABCD]");
        assertEquals("foo \uABCD", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octal_escape_in_text() {
        Message result = parse("\"foo \\037\"");
        assertEquals("foo \037", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octal_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\037]");
        assertEquals("foo \037", ((Text)result.literal()).text());
    }

    @Test
    public void handles_tab_escape_in_text() {
        Message result = parse("\"foo \\t\"");
        assertEquals("foo \t", ((Text)result.literal()).text());
    }

    @Test
    public void handles_tab_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\t]");
        assertEquals("foo \t", ((Text)result.literal()).text());
    }

    @Test
    public void handles_newline_escape_in_text() {
        Message result = parse("\"foo \\t\"");
        assertEquals("foo \t", ((Text)result.literal()).text());
    }

    @Test
    public void handles_newline_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\n]");
        assertEquals("foo \n", ((Text)result.literal()).text());
    }

    @Test
    public void handles_form_escape_in_text() {
        Message result = parse("\"foo \\f\"");
        assertEquals("foo \f", ((Text)result.literal()).text());
    }

    @Test
    public void handles_form_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\f]");
        assertEquals("foo \f", ((Text)result.literal()).text());
    }

    @Test
    public void handles_carriage_return_escape_in_text() {
        Message result = parse("\"foo \\r\"");
        assertEquals("foo \r", ((Text)result.literal()).text());
    }

    @Test
    public void handles_carriage_return_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\r]");
        assertEquals("foo \r", ((Text)result.literal()).text());
    }

    @Test
    public void handles_quote_escape_in_text() {
        Message result = parse("\"foo \\\"\"");
        assertEquals("foo \"", ((Text)result.literal()).text());
    }

    @Test
    public void handles_quote_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\\"]");
        assertEquals("foo \"", ((Text)result.literal()).text());
    }

    @Test
    public void handles_square_bracket_escape_in_text() {
        Message result = parse("\"foo \\]\"");
        assertEquals("foo ]", ((Text)result.literal()).text());
    }

    @Test
    public void handles_square_bracket_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\]]");
        assertEquals("foo ]", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octothorpe_escape_in_text() {
        Message result = parse("\"foo \\#\"");
        assertEquals("foo #", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octothorpe_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\#]");
        assertEquals("foo #", ((Text)result.literal()).text());
    }

    @Test
    public void handles_e_escape_in_text() {
        Message result = parse("\"foo \\e\"");
        assertEquals("foo " + (char)27, ((Text)result.literal()).text());
    }

    @Test
    public void handles_e_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\e]");
        assertEquals("foo " + (char)27, ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_escape_in_text() {
        Message result = parse("\"foo \\\r\"");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\\r]");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_lf_escape_in_text() {
        Message result = parse("\"foo \\\r\n\"");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_lf_escape_in_text_with_alt_syntax() {
        Message result = parse("#[foo \\\r\n]");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void generates_an_error_when_mismatched_curly_brackets() {
        parse("foo({1, 2)}", "blargus.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(8, f.character);
        assertEquals("}", f.expected);
        assertEquals("')'", f.got);
        assertEquals("blargus.sp", f.source);
    }

    @Test
    public void generates_an_error_when_finding_end_in_curly_brackets() {
        parse("{1, 2", "blargus2.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(5, f.character);
        assertEquals("}", f.expected);
        assertEquals("EOF", f.got);
        assertEquals("blargus2.sp", f.source);
    }

    @Test
    public void generates_an_error_when_mismatched_square_brackets() {
        parse("foo([1, 2)]", "blargus.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(8, f.character);
        assertEquals("]", f.expected);
        assertEquals("')'", f.got);
        assertEquals("blargus.sp", f.source);
    }

    @Test
    public void generates_an_error_when_finding_end_in_square_brackets() {
        parse("[1, 2", "blargus2.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(5, f.character);
        assertEquals("]", f.expected);
        assertEquals("EOF", f.got);
        assertEquals("blargus2.sp", f.source);
    }

    @Test
    public void generates_an_error_when_a_comma_isnt_followed_by_anything() {
        parse("\nfoo(abc,)", "blargus3.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(2, f.line);
        assertEquals(8, f.character);
        assertEquals("Expected expression following comma", f.message);
        assertEquals("blargus3.sp", f.source);
    }

    @Test
    public void generates_an_error_when_a_freefloating_escape_is_found() {
        parse("\n foo \\ blarg", "blargus4.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(2, f.line);
        assertEquals(6, f.character);
        assertEquals("Expected newline after free-floating escape character", f.message);
        assertEquals("blargus4.sp", f.source);
    }

    @Test
    public void generates_an_error_when_a_text_isnt_closed() {
        parse("bla \"foo", "blargus5.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(7, f.character);
        assertEquals("Expected end of text, found EOF", f.message);
        assertEquals("blargus5.sp", f.source);
    }

    @Test
    public void generates_an_error_when_an_alt_text_isnt_closed() {
        parse("bla #[foo", "blargus5.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(8, f.character);
        assertEquals("Expected end of text, found EOF", f.message);
        assertEquals("blargus5.sp", f.source);
    }

    @Test
    public void generates_an_error_when_a_hexadecimal_text_escape_isnt_done() {
        parse("\n\n\"foo\\uABC\t\"", "blargus6.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(3, f.line);
        assertEquals(9, f.character);
        assertEquals("Expected four hexadecimal characters in unicode escape - got: TAB", f.message);
        assertEquals("blargus6.sp", f.source);
    }

    @Test
    public void generates_an_error_when_an_unknown_escape_is_used() {
        parse("\n\n\"foo\\y\"", "blargus7.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(3, f.line);
        assertEquals(5, f.character);
        assertEquals("Undefined text escape character: 'y'", f.message);
        assertEquals("blargus7.sp", f.source);
    }


    // TODO:
    // - regular expressions (#/, #r)
    // - numbers
}// ParserTest
