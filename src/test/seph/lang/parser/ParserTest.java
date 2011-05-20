/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Arrays;
import java.io.StringReader;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.Text;
import seph.lang.Regexp;
import seph.lang.ControlFlow;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

import gnu.math.IntNum;
import gnu.math.DFloNum;

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
            return ((Message)new Parser(new Runtime(), new StringReader(input), sourcename).parseFully().seq().first());
        } catch(java.io.IOException e) {
            throw new RuntimeException(e);
        } catch(ControlFlow cf) {
            this.error = cf;
            return null;
        }
    }

    private IPersistentList parseAll(String input) {
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
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_spaces_should_become_a_terminator_message() {
        Message result = parse("  ");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_only_unicode_spaces_should_become_a_terminator_message() {
        Message result = parse("\u0009\u0009\u000b\u000c");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_a_message_should_become_that_message() {
        Message result = parse("foo");
        assertEquals("foo", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void a_string_with_two_messages_should_become_a_message_chain() {
        Message result = parse("foo bar");
        assertEquals("foo", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertEquals("bar", result.next().name());
        assertEquals(PersistentList.EMPTY, result.next().arguments());
        assertNull(result.next().next());
    }

    @Test
    public void octothorp_followed_by_bang_will_be_interpreted_as_a_comment() {
        Message result = parse("#!/foo/bar 123\nfoo");
        assertEquals("foo", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_as_a_terminator() {
        Message result = parse("\n");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_two_newlines_as_one_terminator() {
        Message result = parse("\n\n");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_as_one_terminator() {
        Message result = parse(".");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_period_and_a_newline_as_one_terminator() {
        Message result = parse(".\n");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_a_newline_and_a_period_as_one_terminator() {
        Message result = parse("\n.");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parses_period_between_newlines_as_one_terminator() {
        Message result = parse("\n.\n");
        assertEquals(".", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
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
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void uses_two_semi_colons() {
        Message result = parse("::");
        assertEquals("::", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test @Ignore("This test should really be in tests for operator shuffling, not in the parser test")
    public void separates_two_colons_into_a_message_send() {
        Message result = parse("::foo");
        assertEquals("::", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test @Ignore("This test should really be in tests for operator shuffling, not in the parser test")
    public void separates_three_colons_into_a_message_send() {
        Message result = parse(":::foo");
        assertEquals(":::", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void allows_a_single_colon_as_identifier() {
        Message result = parse(":");
        assertEquals(":", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_ended_with_a_colon() {
        Message result = parse("foo:");
        assertEquals("foo:", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_split_with_a_colon() {
        Message result = parse("foo:bar");
        assertEquals("foo:bar", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_to_be_split_with_more_than_one_colon() {
        Message result = parse("foo::bar");
        assertEquals("foo::bar", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_an_identifier_interspersed_with_colons() {
        Message result = parse("f:o:o:b:a:r");
        assertEquals("f:o:o:b:a:r", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void allows_a_question_mark_followed_by_a_colon() {
        Message result = parse("foo?:");
        assertEquals("foo?:", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void parens_without_preceeding_message_becomes_the_identity_message() {
        Message result = parse("(foo)");
        assertEquals("", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_application_should_be_parsed_correctly() {
        Message result = parse("[]()");
        assertEquals("[]", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_application_with_arguments_should_work() {
        Message result = parse("[](foo)");
        assertEquals("[]", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parse_square_brackets_correctly_as_part_of_longer_message_chain() {
        Message result = parse("foo bar(q) [](r)");
        assertEquals("[]", result.next().next().name());
        assertEquals("r", ((Message)result.next().next().arguments().seq().first()).name());
        assertNull(result.next().next().next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_should_be_parsed_correctly() {
        Message result = parse("[]");
        assertEquals("[]", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_spaces_inbetween_should_be_parsed_correctly() {
        Message result = parse("[   ]");
        assertEquals("[]", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_argument_should_be_parsed_correctly() {
        Message result = parse("[foo]");
        assertEquals("[]", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_without_parenthesis_with_argument_and_spaces_should_be_parsed_correctly() {
        Message result = parse("[   foo   ]");
        assertEquals("[]", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }


    @Test
    public void simple_square_bracket_without_parenthesis_with_arguments_should_be_parsed_correctly() {
        Message result = parse("[foo, bar]");
        assertEquals("[]", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_square_bracket_can_be_parsed_with_terminators_inside() {
        Message result = parse("[bar, \nfoo(quux)]");
        assertEquals("[]", result.name());
        assertEquals("bar", ((Message)result.arguments().seq().first()).name());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("quux", ((Message)((Message)result.arguments().seq().more().first()).arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parses_square_brackets_correctly_after_an_identifier() {
        Message result = parse("foo[bar, quux]");
        assertEquals("foo", result.name());
        assertEquals("[]", result.next().name());
        assertEquals("bar", ((Message)result.next().arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.next().arguments().seq().more().first()).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_square_brackets_correctly_after_an_identifier_and_a_space() {
        Message result = parse("foo [bar, quux]");
        assertEquals("foo", result.name());
        assertEquals("[]", result.next().name());
        assertEquals("bar", ((Message)result.next().arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.next().arguments().seq().more().first()).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_square_brackets_correctly_inside_a_function_application() {
        Message result = parse("foo([bar, quux])");
        assertEquals("foo", result.name());
        assertEquals("[]", ((Message)result.arguments().seq().first()).name());
        assertEquals("bar", ((Message)((Message)result.arguments().seq().first()).arguments().seq().first()).name());
        assertEquals("quux", ((Message)((Message)result.arguments().seq().first()).arguments().seq().more().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_application_should_be_parsed_correctly() {
        Message result = parse("{}()");
        assertEquals("{}", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_application_with_arguments_should_work() {
        Message result = parse("{}(foo)");
        assertEquals("{}", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parse_curly_brackets_correctly_as_part_of_longer_message_chain() {
        Message result = parse("foo bar(q) {}(r)");
        assertEquals("{}", result.next().next().name());
        assertEquals("r", ((Message)result.next().next().arguments().seq().first()).name());
        assertNull(result.next().next().next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_should_be_parsed_correctly() {
        Message result = parse("{}");
        assertEquals("{}", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_spaces_inbetween_should_be_parsed_correctly() {
        Message result = parse("{   }");
        assertEquals("{}", result.name());
        assertEquals(PersistentList.EMPTY, result.arguments());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_argument_should_be_parsed_correctly() {
        Message result = parse("{foo}");
        assertEquals("{}", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_without_parenthesis_with_argument_and_spaces_should_be_parsed_correctly() {
        Message result = parse("{   foo   }");
        assertEquals("{}", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertNull(result.next());
    }


    @Test
    public void simple_curly_bracket_without_parenthesis_with_arguments_should_be_parsed_correctly() {
        Message result = parse("{foo, bar}");
        assertEquals("{}", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).name());
        assertNull(result.next());
    }

    @Test
    public void simple_curly_bracket_can_be_parsed_with_terminators_inside() {
        Message result = parse("{bar, \nfoo(quux)}");
        assertEquals("{}", result.name());
        assertEquals("bar", ((Message)result.arguments().seq().first()).name());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("quux", ((Message)((Message)result.arguments().seq().more().first()).arguments().seq().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parses_curly_brackets_correctly_after_an_identifier() {
        Message result = parse("foo{bar, quux}");
        assertEquals("foo", result.name());
        assertEquals("{}", result.next().name());
        assertEquals("bar", ((Message)result.next().arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.next().arguments().seq().more().first()).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_curly_brackets_correctly_after_an_identifier_and_a_space() {
        Message result = parse("foo {bar, quux}");
        assertEquals("foo", result.name());
        assertEquals("{}", result.next().name());
        assertEquals("bar", ((Message)result.next().arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.next().arguments().seq().more().first()).name());
        assertNull(result.next().next());
    }

    @Test
    public void parses_curly_brackets_correctly_inside_a_function_application() {
        Message result = parse("foo({bar, quux})");
        assertEquals("foo", result.name());
        assertEquals("{}", ((Message)result.arguments().seq().first()).name());
        assertEquals("bar", ((Message)((Message)result.arguments().seq().first()).arguments().seq().first()).name());
        assertEquals("quux", ((Message)((Message)result.arguments().seq().first()).arguments().seq().more().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parses_the_toplevel_with_commas() {
        IPersistentList result = parseAll("foo,\nbar: method");
        assertEquals("foo", ((Message)result.seq().first()).name());
        assertNull(((Message)result.seq().first()).next());
        assertEquals("bar:", ((Message)result.seq().more().first()).name());
        assertEquals("method", ((Message)result.seq().more().first()).next().name());
    }

    @Test
    public void parses_set_literal_as_a_set_literal() {
        Message result = parse("#{bar, quux}");
        assertEquals("set", result.name());
        assertEquals("bar", ((Message)result.arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.arguments().seq().more().first()).name());
        assertNull(result.next());
    }

    @Test
    public void parses_vector_literal_as_a_vector_literal() {
        Message result = parse("#[bar, quux]");
        assertEquals("vector", result.name());
        assertEquals("bar", ((Message)result.arguments().seq().first()).name());
        assertEquals("quux", ((Message)result.arguments().seq().more().first()).name());
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
    public void parsing_a_vector_message_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n  #[foo]", "aad.sp");
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
        Message result = parse(" %[blargus hello \"something else\" - he]");

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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
    }

    @Test
    public void parses_percent_operator() {
        Message result = parse(" %-+*%<>!!?~&|^$$=@'`:# foo");

        assertEquals("%-+*%<>!!?~&|^$$=@'`:#", result.name());
    }

    @Test
    public void parses_percent_operator_with_args() {
        Message result = parse(" %-+*%<>!!?~&|^$$=@'`:#(foo)");

        assertEquals("%-+*%<>!!?~&|^$$=@'`:#", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
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
        Message result = parse(" #/-+*%<>!!?~&|^$$=@'`:# foo");

        assertEquals("#/-+*%<>!!?~&|^$$=@'`:#", result.name());
    }

    @Test
    public void parses_octothorpe_operator_with_args() {
        Message result = parse(" #/-+*%<>!!?~&|^$$=@'`:#(foo)");

        assertEquals("#/-+*%<>!!?~&|^$$=@'`:#", result.name());
        assertEquals("foo", ((Message)result.arguments().seq().first()).name());
    }

    @Test
    public void separates_a_slash_from_percent_operator() {
        Message result = parse(" %-+*#<>!!?~&|^$$=@'`:#/ foo");

        assertEquals("%-+*#<>!!?~&|^$$=@'`:#", result.name());
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
        Message result = parse("\"foo %{foo bar(\"blarg%{bux}\")} bar\"");

        assertEquals("internal:concatenateText", result.name());
        assertEquals("foo ", ((Text)((Message)result.arguments().seq().first()).literal()).text());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).next().name());
        assertEquals("internal:concatenateText", ((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).name());
        assertEquals("blarg", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().first()).literal()).text());
        assertEquals("bux", ((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().first()).name());
        assertEquals(" bar", ((Text)((Message)result.arguments().seq().more().more().first()).literal()).text());
    }

    @Test
    public void handles_text_interpolation_in_alt_text_format() {
        Message result = parse("%[foo %{foo bar(\"blarg%{bux}\")} bar]");

        assertEquals("internal:concatenateText", result.name());
        assertEquals("foo ", ((Text)((Message)result.arguments().seq().first()).literal()).text());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).next().name());
        assertEquals("internal:concatenateText", ((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).name());
        assertEquals("blarg", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().first()).literal()).text());
        assertEquals("bux", ((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().first()).name());
        assertEquals(" bar", ((Text)((Message)result.arguments().seq().more().more().first()).literal()).text());
    }

    @Test
    public void handles_unicode_escape_in_text() {
        Message result = parse("\"foo \\uABCD\"");
        assertEquals("foo \uABCD", ((Text)result.literal()).text());
    }

    @Test
    public void handles_unicode_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\uABCD]");
        assertEquals("foo \uABCD", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octal_escape_in_text() {
        Message result = parse("\"foo \\037\"");
        assertEquals("foo \037", ((Text)result.literal()).text());
    }

    @Test
    public void handles_octal_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\037]");
        assertEquals("foo \037", ((Text)result.literal()).text());
    }

    @Test
    public void handles_tab_escape_in_text() {
        Message result = parse("\"foo \\t\"");
        assertEquals("foo \t", ((Text)result.literal()).text());
    }

    @Test
    public void handles_tab_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\t]");
        assertEquals("foo \t", ((Text)result.literal()).text());
    }

    @Test
    public void handles_newline_escape_in_text() {
        Message result = parse("\"foo \\n\"");
        assertEquals("foo \n", ((Text)result.literal()).text());
    }

    @Test
    public void handles_newline_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\n]");
        assertEquals("foo \n", ((Text)result.literal()).text());
    }

    @Test
    public void handles_form_escape_in_text() {
        Message result = parse("\"foo \\f\"");
        assertEquals("foo \f", ((Text)result.literal()).text());
    }

    @Test
    public void handles_form_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\f]");
        assertEquals("foo \f", ((Text)result.literal()).text());
    }

    @Test
    public void handles_carriage_return_escape_in_text() {
        Message result = parse("\"foo \\r\"");
        assertEquals("foo \r", ((Text)result.literal()).text());
    }

    @Test
    public void handles_carriage_return_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\r]");
        assertEquals("foo \r", ((Text)result.literal()).text());
    }

    @Test
    public void handles_quote_escape_in_text() {
        Message result = parse("\"foo \\\"\"");
        assertEquals("foo \"", ((Text)result.literal()).text());
    }

    @Test
    public void handles_quote_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\\"]");
        assertEquals("foo \"", ((Text)result.literal()).text());
    }

    @Test
    public void handles_square_bracket_escape_in_text() {
        Message result = parse("\"foo \\]\"");
        assertEquals("foo ]", ((Text)result.literal()).text());
    }

    @Test
    public void handles_square_bracket_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\]]");
        assertEquals("foo ]", ((Text)result.literal()).text());
    }

    @Test
    public void handles_percent_escape_in_text() {
        Message result = parse("\"foo \\%\"");
        assertEquals("foo %", ((Text)result.literal()).text());
    }

    @Test
    public void handles_percent_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\%]");
        assertEquals("foo %", ((Text)result.literal()).text());
    }

    @Test
    public void handles_e_escape_in_text() {
        Message result = parse("\"foo \\e\"");
        assertEquals("foo " + (char)27, ((Text)result.literal()).text());
    }

    @Test
    public void handles_e_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\e]");
        assertEquals("foo " + (char)27, ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_escape_in_text() {
        Message result = parse("\"foo \\\r\"");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\\r]");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_lf_escape_in_text() {
        Message result = parse("\"foo \\\r\n\"");
        assertEquals("foo ", ((Text)result.literal()).text());
    }

    @Test
    public void handles_cr_lf_escape_in_text_with_alt_syntax() {
        Message result = parse("%[foo \\\r\n]");
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
        assertEquals("Expected message chain following comma", f.message);
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
        parse("bla %[foo", "blargus5.sp");
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

    @Test
    public void parses_a_regular_expression() {
        Message result = parse(" %/foo/");
        assertTrue("Result should be a literal message", result.isLiteral());
        SephObject literal = result.literal();
        assertEquals(Regexp.class, literal.getClass());
        assertEquals("foo", ((Regexp)literal).pattern());
        assertNull(result.next());
    }

    @Test
    public void parses_a_regular_expression_with_flags() {
        Message result = parse(" %/bar/ix");
        SephObject literal = result.literal();
        assertEquals("bar", ((Regexp)literal).pattern());
        assertEquals("ix", ((Regexp)literal).flags());
        assertNull(result.next());
    }

    @Test
    public void parses_a_regular_expression_and_adds_correct_positioning_information() {
        Message result = parse("\n\n\n %/foxy/", "boxy.sp");
        assertEquals("boxy.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(1, result.position());
    }

    @Test
    public void parses_a_regular_expression_with_alternative_syntax() {
        Message result = parse(" %r[foo]");
        assertTrue("Result should be a literal message", result.isLiteral());
        SephObject literal = result.literal();
        assertEquals(Regexp.class, literal.getClass());
        assertEquals("foo", ((Regexp)literal).pattern());
        assertNull(result.next());
    }

    @Test
    public void parses_a_regular_expression_with_flags_with_alternative_syntax() {
        Message result = parse(" %r[bar]mux");
        SephObject literal = result.literal();
        assertEquals("bar", ((Regexp)literal).pattern());
        assertEquals("mux", ((Regexp)literal).flags());
        assertNull(result.next());
    }

    @Test
    public void parses_a_regular_expression_and_adds_correct_positioning_information_with_alternative_syntax() {
        Message result = parse("\n\n\n %r[foxy]", "boxy2.sp");
        assertEquals("boxy2.sp", result.filename());
        assertEquals(4, result.line());
        assertEquals(1, result.position());
    }

    @Test
    public void signals_a_failure_when_eof_is_found_in_the_middle_of_a_regexp() {
        parse("bla %/foo", "blargus6.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(8, f.character);
        assertEquals("Expected end of regular expression, found EOF", f.message);
        assertEquals("blargus6.sp", f.source);
    }

    @Test
    public void signals_a_failure_when_eof_is_found_in_the_middle_of_an_alternative_regexp() {
        parse("bla %r[foo", "blargus7.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(1, f.line);
        assertEquals(9, f.character);
        assertEquals("Expected end of regular expression, found EOF", f.message);
        assertEquals("blargus7.sp", f.source);
    }

    @Test
    public void parses_a_unicode_escape_in_a_regexp() {
        Message result = parse("%/foo\\uABCD/");
        assertEquals("foo\uABCD", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_unicode_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\uABCD]");
        assertEquals("foo\uABCD", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_octal_escape_in_a_regexp() {
        Message result = parse("%/foo\037/");
        assertEquals("foo\037", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_octal_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\037]");
        assertEquals("foo\037", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_tab_escape_in_a_regexp() {
        Message result = parse("%/foo\\t/");
        assertEquals("foo\t", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_tab_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\t]");
        assertEquals("foo\t", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_newline_escape_in_a_regexp() {
        Message result = parse("%/foo\\n/");
        assertEquals("foo\n", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_newline_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\n]");
        assertEquals("foo\n", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_form_escape_in_a_regexp() {
        Message result = parse("%/foo\\f/");
        assertEquals("foo\f", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_form_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\f]");
        assertEquals("foo\f", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_carriage_return_escape_in_a_regexp() {
        Message result = parse("%/foo\\r/");
        assertEquals("foo\r", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_carriage_return_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\r]");
        assertEquals("foo\r", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_slash_escape_in_a_regexp() {
        Message result = parse("%/foo\\//");
        assertEquals("foo\\/", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_slash_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\/]");
        assertEquals("foo\\/", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_backslash_escape_in_a_regexp() {
        Message result = parse("%/foo\\\\/");
        assertEquals("foo\\\\", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_backslash_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\\\]");
        assertEquals("foo\\\\", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_real_newline_escape_in_a_regexp() {
        Message result = parse("%/foo\\\n/");
        assertEquals("foo", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_real_newline_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\\n]");
        assertEquals("foo", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_percent_escape_in_a_regexp() {
        Message result = parse("%/foo\\%/");
        assertEquals("foo%", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_percent_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\%]");
        assertEquals("foo%", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_A_escape_in_a_regexp() {
        Message result = parse("%/foo\\A/");
        assertEquals("foo\\A", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_A_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\A]");
        assertEquals("foo\\A", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_d_escape_in_a_regexp() {
        Message result = parse("%/foo\\d/");
        assertEquals("foo\\d", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_d_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\d]");
        assertEquals("foo\\d", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_D_escape_in_a_regexp() {
        Message result = parse("%/foo\\D/");
        assertEquals("foo\\D", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_D_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\D]");
        assertEquals("foo\\D", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_s_escape_in_a_regexp() {
        Message result = parse("%/foo\\s/");
        assertEquals("foo\\s", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_s_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\s]");
        assertEquals("foo\\s", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_S_escape_in_a_regexp() {
        Message result = parse("%/foo\\S/");
        assertEquals("foo\\S", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_S_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\S]");
        assertEquals("foo\\S", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_w_escape_in_a_regexp() {
        Message result = parse("%/foo\\w/");
        assertEquals("foo\\w", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_w_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\w]");
        assertEquals("foo\\w", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_W_escape_in_a_regexp() {
        Message result = parse("%/foo\\W/");
        assertEquals("foo\\W", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_W_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\W]");
        assertEquals("foo\\W", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_b_escape_in_a_regexp() {
        Message result = parse("%/foo\\b/");
        assertEquals("foo\\b", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_b_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\b]");
        assertEquals("foo\\b", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_B_escape_in_a_regexp() {
        Message result = parse("%/foo\\B/");
        assertEquals("foo\\B", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_B_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\B]");
        assertEquals("foo\\B", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_z_escape_in_a_regexp() {
        Message result = parse("%/foo\\z/");
        assertEquals("foo\\z", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_z_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\z]");
        assertEquals("foo\\z", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_Z_escape_in_a_regexp() {
        Message result = parse("%/foo\\Z/");
        assertEquals("foo\\Z", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_Z_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\Z]");
        assertEquals("foo\\Z", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_lt_escape_in_a_regexp() {
        Message result = parse("%/foo\\</");
        assertEquals("foo\\<", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_lt_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\<]");
        assertEquals("foo\\<", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_gt_escape_in_a_regexp() {
        Message result = parse("%/foo\\>/");
        assertEquals("foo\\>", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_gt_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\>]");
        assertEquals("foo\\>", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_G_escape_in_a_regexp() {
        Message result = parse("%/foo\\G/");
        assertEquals("foo\\G", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_G_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\G]");
        assertEquals("foo\\G", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_p_escape_in_a_regexp() {
        Message result = parse("%/foo\\p{Space}/");
        assertEquals("foo\\p{Space}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_p_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\p{Space}]");
        assertEquals("foo\\p{Space}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_P_escape_in_a_regexp() {
        Message result = parse("%/foo\\P{Space}/");
        assertEquals("foo\\P{Space}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_P_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\P{Space}]");
        assertEquals("foo\\P{Space}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_curly_open_escape_in_a_regexp() {
        Message result = parse("%/foo\\{/");
        assertEquals("foo\\{", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_curly_open_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\{]");
        assertEquals("foo\\{", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_curly_close_escape_in_a_regexp() {
        Message result = parse("%/foo\\}/");
        assertEquals("foo\\}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_curly_close_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\}]");
        assertEquals("foo\\}", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_square_open_escape_in_a_regexp() {
        Message result = parse("%/foo\\[/");
        assertEquals("foo\\[", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_square_open_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\[]");
        assertEquals("foo\\[", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_square_close_escape_in_a_regexp() {
        Message result = parse("%/foo\\]/");
        assertEquals("foo]", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_square_close_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\]]");
        assertEquals("foo]", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_asterisk_escape_in_a_regexp() {
        Message result = parse("%/foo\\*/");
        assertEquals("foo\\*", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_asterisk_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\*]");
        assertEquals("foo\\*", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_open_paren_escape_in_a_regexp() {
        Message result = parse("%/foo\\(/");
        assertEquals("foo\\(", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_open_paren_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\(]");
        assertEquals("foo\\(", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_close_paren_escape_in_a_regexp() {
        Message result = parse("%/foo\\)/");
        assertEquals("foo\\)", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_close_paren_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\)]");
        assertEquals("foo\\)", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_dollar_escape_in_a_regexp() {
        Message result = parse("%/foo\\$/");
        assertEquals("foo\\$", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_dollar_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\$]");
        assertEquals("foo\\$", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_caret_escape_in_a_regexp() {
        Message result = parse("%/foo\\^/");
        assertEquals("foo\\^", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_caret_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\^]");
        assertEquals("foo\\^", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_plus_escape_in_a_regexp() {
        Message result = parse("%/foo\\+/");
        assertEquals("foo\\+", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_plus_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\+]");
        assertEquals("foo\\+", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_question_mark_escape_in_a_regexp() {
        Message result = parse("%/foo\\?/");
        assertEquals("foo\\?", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_question_mark_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\?]");
        assertEquals("foo\\?", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_period_escape_in_a_regexp() {
        Message result = parse("%/foo\\./");
        assertEquals("foo\\.", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_period_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\.]");
        assertEquals("foo\\.", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_pipe_escape_in_a_regexp() {
        Message result = parse("%/foo\\|/");
        assertEquals("foo\\|", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void parses_a_regexp_pipe_escape_in_a_regexp_with_alternative_syntax() {
        Message result = parse("%r[foo\\|]");
        assertEquals("foo\\|", ((Regexp)result.literal()).pattern());
    }


    @Test
    public void handles_cr_escape_in_regexp() {
        Message result = parse("%/foo \\\r/");
        assertEquals("foo ", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void handles_cr_escape_in_regexp_with_alt_syntax() {
        Message result = parse("%r[foo \\\r]");
        assertEquals("foo ", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void handles_cr_lf_escape_in_regexp() {
        Message result = parse("%/foo \\\r\n/");
        assertEquals("foo ", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void handles_cr_lf_escape_in_regexp_with_alt_syntax() {
        Message result = parse("%r[foo \\\r\n]");
        assertEquals("foo ", ((Regexp)result.literal()).pattern());
    }

    @Test
    public void generates_an_error_when_an_unknown_escape_is_used_in_regexp() {
        parse("\n\n%/foo\\y/", "blargus8.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(3, f.line);
        assertEquals(6, f.character);
        assertEquals("Undefined regular expression escape character: 'y'", f.message);
        assertEquals("blargus8.sp", f.source);
    }

    @Test
    public void generates_an_error_when_a_hexadecimal_regexp_escape_isnt_done() {
        parse("\n\n%/foo\\uABC\t/", "blargus42.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals(3, f.line);
        assertEquals(10, f.character);
        assertEquals("Expected four hexadecimal characters in unicode escape - got: TAB", f.message);
        assertEquals("blargus42.sp", f.source);
    }

    @Test
    public void handles_regexp_interpolation() {
        Message result = parse("%/foo %{foo bar(%/blarg%{bux}/xi)} bar/");

        assertEquals("internal:compositeRegexp", result.name());
        assertEquals("foo ", ((Text)((Message)result.arguments().seq().first()).literal()).text());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).next().name());
        assertEquals("internal:compositeRegexp", ((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).name());
        assertEquals("blarg", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().first()).literal()).text());
        assertEquals("bux", ((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().first()).name());
        assertEquals("xi", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().more().first()).literal()).text());
        assertEquals(" bar", ((Text)((Message)result.arguments().seq().more().more().first()).literal()).text());
    }

    @Test
    public void handles_regexp_interpolation_in_alt_syntax() {
        Message result = parse("%r[foo %{foo bar(%/blarg%{bux}/xi)} bar]");

        assertEquals("internal:compositeRegexp", result.name());
        assertEquals("foo ", ((Text)((Message)result.arguments().seq().first()).literal()).text());
        assertEquals("foo", ((Message)result.arguments().seq().more().first()).name());
        assertEquals("bar", ((Message)result.arguments().seq().more().first()).next().name());
        assertEquals("internal:compositeRegexp", ((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).name());
        assertEquals("blarg", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().first()).literal()).text());
        assertEquals("bux", ((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().first()).name());
        assertEquals("xi", ((Text)((Message)((Message)((Message)result.arguments().seq().more().first()).next().arguments().seq().first()).arguments().seq().more().more().first()).literal()).text());
        assertEquals(" bar", ((Text)((Message)result.arguments().seq().more().more().first()).literal()).text());
    }

    @Test
    public void parses_a_simple_number() {
        for(int i = 0; i < 21; i++) {
            assertEquals(IntNum.make(i), parse("" + i).literal());
        }
        assertEquals(IntNum.make(12345678901L), parse("12345678901").literal());
    }

    @Test
    public void parses_a_simple_number_preceded_by_a_minus() {
        assertEquals(IntNum.make(-43), parse("-43").literal());
    }

    @Test
    public void parses_a_really_big_number() {
        assertEquals(IntNum.valueOf("13423536513485673467456745687234523787568658476842513423534532452345"), parse("13423536513485673467456745687234523787568658476842513423534532452345").literal());
    }

    @Test
    public void parses_a_decimal_number() {
        assertEquals(DFloNum.make(0.123456789111), parse("0.123456789111").literal());
        assertEquals(DFloNum.make(42555.123456789111), parse("42555.123456789111").literal());
    }

    @Test
    public void parses_a_decimal_number_with_a_minus_in_front_of_it() {
        assertEquals(DFloNum.make(-0.123456789111), parse("-0.123456789111").literal());
    }

    @Test
    public void parses_a_decimal_number_followed_by_an_exponent() {
        assertEquals(DFloNum.make(0.123456789111E3), parse("0.123456789111E3").literal());
        assertEquals(DFloNum.make(42555.123456789111E4), parse("42555.123456789111E4").literal());

        assertEquals(DFloNum.make(0.123456789111E+3), parse("0.123456789111E+3").literal());
        assertEquals(DFloNum.make(42555.123456789111E+4), parse("42555.123456789111E+4").literal());

        assertEquals(DFloNum.make(0.123456789111E-3), parse("0.123456789111E-3").literal());
        assertEquals(DFloNum.make(42555.123456789111E-4), parse("42555.123456789111E-4").literal());

        assertEquals(DFloNum.make(0.123456789111e3), parse("0.123456789111e3").literal());
        assertEquals(DFloNum.make(42555.123456789111e4), parse("42555.123456789111e4").literal());

        assertEquals(DFloNum.make(0.123456789111e+3), parse("0.123456789111e+3").literal());
        assertEquals(DFloNum.make(42555.123456789111e+4), parse("42555.123456789111e+4").literal());

        assertEquals(DFloNum.make(0.123456789111e-3), parse("0.123456789111e-3").literal());
        assertEquals(DFloNum.make(42555.123456789111e-4), parse("42555.123456789111e-4").literal());

        assertEquals(DFloNum.make(42e4), parse("42e4").literal());
        assertEquals(DFloNum.make(42E4), parse("42E4").literal());
        assertEquals(DFloNum.make(42e-4), parse("42e-4").literal());
        assertEquals(DFloNum.make(42E-4), parse("42E-4").literal());
        assertEquals(DFloNum.make(42e+4), parse("42e+4").literal());
        assertEquals(DFloNum.make(42E+4), parse("42E+4").literal());
    }

    @Test
    public void parses_a_number_with_underscores_in_it() {
        assertEquals(IntNum.make(14400044), parse("1_44___00_0_44").literal());

        assertEquals("_1_44___00_0_44", parse("_1_44___00_0_44").name());
    }

    @Test
    public void parses_a_decimal_number_with_underscores_in_it() {
        assertEquals(DFloNum.make(42555.123456789111e-12), parse("4_25_5_5.1_2_3456789_111e-1_2").literal());

        assertEquals("_4_25_5_5", parse("_4_25_5_5.1_2_3456789_111e-1_2").name());
    }

    @Test
    public void parses_a_hexadecimal_number_correctly() {
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("0xABC234234DEFabcdef0314256789").literal());
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("0XABC234234DEFabcdef0314256789").literal());
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("16#ABC234234DEFabcdef0314256789").literal());
    }

    @Test
    public void parses_a_hexadecimal_number_correctly_with_underscores_in_it() {
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("0x_A__BC234234DEFabcdef0___3142567______89").literal());
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("0XABC2342_34DEFabcdef03142___56789").literal());
        assertEquals(IntNum.valueOf("ABC234234DEFabcdef0314256789", 16), parse("16#ABC_234234DEFabcdef031425____6789").literal());
    }

    @Test
    public void parses_numbers_in_other_bases_correctly() {
        assertEquals(IntNum.valueOf("000101001010101010001111", 2), parse("2#000101001010101010001111").literal());
        assertEquals(IntNum.valueOf("hellomyFRIENDFROMNOwhereThiszzzIsWeird", 36), parse("36#hellomyFRIENDFROMNOwhereThiszzzIsWeird").literal());
    }

    @Test
    public void parses_numbers_in_other_bases_correctly_with_underscores_in_it() {
        assertEquals(IntNum.valueOf("000101001010101010001111", 2), parse("2#00010100___10101010_1000111_1").literal());
        assertEquals(IntNum.valueOf("hellomyFRIENDFROMNOwhereThiszzzIsWeird", 36), parse("36#h_ellomy_FRIEND_FROMNO_wher____eThiszzzIsWeird").literal());
    }

    @Test
    public void generates_a_good_error_when_no_digits_are_found_for_something_with_radix() {
        parse("3#blarg", "bloho.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals("Expected at least one base 3 character in base 3 number literal - got: 'b'", f.message);
        assertEquals(1, f.line);
        assertEquals(1, f.character);
        assertEquals("bloho.sp", f.source);

        parse("16#ylarg", "bloho2.sp");
        f = (Parser.Failure)error.getValue();
        assertEquals("Expected at least one hexadecimal character in hexadecimal number literal - got: 'y'", f.message);
        assertEquals(1, f.line);
        assertEquals(2, f.character);
        assertEquals("bloho2.sp", f.source);

        parse("2#2", "bloho3.sp");
        f = (Parser.Failure)error.getValue();
        assertEquals("Expected at least one binary character in binary number literal - got: '2'", f.message);
        assertEquals(1, f.line);
        assertEquals(1, f.character);
        assertEquals("bloho3.sp", f.source);

        parse("8#9", "bloho4.sp");
        f = (Parser.Failure)error.getValue();
        assertEquals("Expected at least one octal character in octal number literal - got: '9'", f.message);
        assertEquals(1, f.line);
        assertEquals(1, f.character);
        assertEquals("bloho4.sp", f.source);
    }

    @Test
    public void generates_an_appropriate_error_when_no_digits_are_found_after_exponent_specified() {
        parse("0.15EBlarg", "bloho10.sp");
        Parser.Failure f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(4, f.character);
        assertEquals("bloho10.sp", f.source);

        parse("0.15E+Blarg", "bloho11.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(5, f.character);
        assertEquals("bloho11.sp", f.source);

        parse("0.15E-Blarg", "bloho12.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(5, f.character);
        assertEquals("bloho12.sp", f.source);

        parse("42.15EBlarg", "bloho13.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(5, f.character);
        assertEquals("bloho13.sp", f.source);

        parse("42.15E+Blarg", "bloho14.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(6, f.character);
        assertEquals("bloho14.sp", f.source);

        parse("42.15E-Blarg", "bloho15.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(6, f.character);
        assertEquals("bloho15.sp", f.source);

        parse("42EBlarg", "bloho16.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(2, f.character);
        assertEquals("bloho16.sp", f.source);

        parse("42E+Blarg", "bloho17.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(3, f.character);
        assertEquals("bloho17.sp", f.source);

        parse("42E-Blarg", "bloho18.sp");
        f = (Parser.Failure)error.getValue(); error = null;
        assertEquals("Expected at least one decimal character following exponent specifier in number literal - got: 'B'", f.message);
        assertEquals(1, f.line);
        assertEquals(3, f.character);
        assertEquals("bloho18.sp", f.source);
    }

    @Test
    public void generates_an_appropriate_error_when_a_too_large_radix_is_specified() {
        parse("37#15", "bloho42.sp");
        Parser.Failure f = (Parser.Failure)error.getValue();
        assertEquals("Expected radix between 0 and 36 - got: 37", f.message);
        assertEquals(1, f.line);
        assertEquals(2, f.character);
        assertEquals("bloho42.sp", f.source);
    }

    @Test
    public void parsing_a_number_generates_a_correct_filename_line_and_position() {
        Message result = parse("\n\n 32", "numx.sp");
        assertEquals("numx.sp", result.filename());
        assertEquals(3, result.line());
        assertEquals(1, result.position());
    }

    private static Message msg(String name) {
        return NamedMessage.create(name, null, null, null, -1, -1, null);
    }

    private static Message msg(String name, Message next) {
        return NamedMessage.create(name, null, next, null, -1, -1, null);
    }

    private static Message msg(String name, IPersistentList args, Message next) {
        return NamedMessage.create(name, args, next, null, -1, -1, null);
    }

    private static Message msg(String name, IPersistentList args) {
        return NamedMessage.create(name, args, null, null, -1, -1, null);
    }

    @Test
    public void parsing_a_simple_expression_generates_correct_associativity() {
        Message result = parse("foo1 + foo2 * foo3");
        // same as:     foo1 +(foo2 *(foo3))
        Message expected = msg("foo1", msg("+", PersistentList.create(Arrays.asList(msg("foo2", msg("*", PersistentList.create(Arrays.asList(msg("foo3")))))))));
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void parsing_another_simple_expression_generates_correct_associativity() {
        Message result = parse("foo1 * foo2 + foo3");
        // same as:     foo1 *(foo2) +(foo3)
        Message expected = msg("foo1", msg("*", PersistentList.create(Arrays.asList(msg("foo2"))), msg("+", PersistentList.create(Arrays.asList(msg("foo3"))))));
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void parsing_an_arithmetic_expression_generates_the_correct_associativity() {
        Message result = parse("foo1 + foo2 * foo3 / ( foo4 - foo5 ) ** foo6 ** foo7");
        // same as:     foo1 +(foo2 *(foo3) /((foo4 -(foo5)) **(foo6) **(foo7)))"
        Message expected = msg("foo1", msg("+", PersistentList.create(Arrays.asList(msg("foo2", msg("*", PersistentList.create(Arrays.asList(msg("foo3"))), msg("/", PersistentList.create(Arrays.asList(msg("", PersistentList.create(Arrays.asList(msg("foo4", msg("-", PersistentList.create(Arrays.asList(msg("foo5"))))))), msg("**", PersistentList.create(Arrays.asList(msg("foo6"))), msg("**", PersistentList.create(Arrays.asList(msg("foo7")))))))))))))));
                                                                                    
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void parse_a_plus_message() {
        Message m = parse("foo + bar");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_a_plus_message_with_preceding_messages() {
        Message m = parse("bux foo + bar");
        Message expected = msg("bux", msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_a_plus_message_with_following_messages() {
        Message m = parse("foo + bar something");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("something"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_a_plus_message_inside_of_an_argument_list() {
        Message m = parse("foo(bar + bux foo, blargus) bar");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("bar", msg("+", PersistentList.create(Arrays.asList(msg("bux", msg("foo")))))), msg("blargus"))), msg("bar"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_minus_correctly() {
        Message m = parse("-foo");
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_minus_correctly_with_following_messages() {
        Message m = parse("-foo println");
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_minus_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = parse("-(foo) println");
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("foo"))), msg("println"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_minus_several_times_over() {
        Message m = parse("- -(foo)");
        Message expected = msg("-", PersistentList.create(Arrays.asList(msg("-", PersistentList.create(Arrays.asList(msg("foo")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_binary_operator_in_simple_expression() {
        Message m = parse("map(*foo)");
        Message expected = msg("map", PersistentList.create(Arrays.asList(msg("*", PersistentList.create(Arrays.asList(msg("foo")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_binary_operator_in_complex_expression() {
        Message m = parse("map(* foo + five - thirteen / three)");
        Message expected = msg("map", PersistentList.create(Arrays.asList(msg("*", PersistentList.create(Arrays.asList(msg("foo"))), msg("+", PersistentList.create(Arrays.asList(msg("five"))), msg("-", PersistentList.create(Arrays.asList(msg("thirteen", msg("/", PersistentList.create(Arrays.asList(msg("three")))))))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_bang_correctly() {
        Message m = parse("!false");
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_bang_correctly_with_following_messages() {
        Message m = parse("!false println");
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_bang_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = parse("!(false)");
        Message expected = msg("!", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_bang_in_expression() {
        Message m = parse("true && !false");
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("!", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_tilde_correctly() {
        Message m = parse("~false");
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_tilde_correctly_with_following_messages() {
        Message m = parse("~false println");
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_tilde_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = parse("~(false)");
        Message expected = msg("~", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_tilde_in_expression() {
        Message m = parse("true && ~false");
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("~", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_dollar_correctly() {
        Message m = parse("$false");
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_dollar_correctly_with_following_messages() {
        Message m = parse("$false println");
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))), msg("println"));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_dollar_correctly_with_following_messages_when_already_in_prefix_form() {
        Message m = parse("$(false)");
        Message expected = msg("$", PersistentList.create(Arrays.asList(msg("false"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void parses_unary_dollar_in_expression() {
        Message m = parse("true && $false");
        Message expected = msg("true", msg("&&", PersistentList.create(Arrays.asList(msg("$", PersistentList.create(Arrays.asList(msg("false"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_plus_and_mult() {
        Message m = parse("foo+bar*quux");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("*", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_mult_and_plus() {
        Message m = parse("foo*bar+quux");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("bar"))), msg("+", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_plus_and_mult_with_spaces() {
        Message m = parse("foo + bar * quux");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("*", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_mult_and_plus_with_spaces() {
        Message m = parse("foo * bar + quux");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("bar"))), msg("+", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_plus_and_div() {
        Message m = parse("foo+bar/quux");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("/", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_div_and_plus() {
        Message m = parse("foo/bar+quux");
        Message expected = msg("foo", msg("/", PersistentList.create(Arrays.asList(msg("bar"))), msg("+", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_plus_and_div_with_spaces() {
        Message m = parse("foo + bar / quux");
        Message expected = msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar", msg("/", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_div_and_plus_with_spaces() {
        Message m = parse("foo / bar + quux");
        Message expected = msg("foo", msg("/", PersistentList.create(Arrays.asList(msg("bar"))), msg("+", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }


    @Test
    public void handles_precedence_for_minus_and_mult() {
        Message m = parse("foo-bar*quux");
        Message expected = msg("foo", msg("-", PersistentList.create(Arrays.asList(msg("bar", msg("*", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_mult_and_minus() {
        Message m = parse("foo*bar-quux");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("bar"))), msg("-", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_minus_and_mult_with_spaces() {
        Message m = parse("foo - bar * quux");
        Message expected = msg("foo", msg("-", PersistentList.create(Arrays.asList(msg("bar", msg("*", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_mult_and_minus_with_spaces() {
        Message m = parse("foo * bar - quux");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("bar"))), msg("-", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_minus_and_div() {
        Message m = parse("foo-bar/quux");
        Message expected = msg("foo", msg("-", PersistentList.create(Arrays.asList(msg("bar", msg("/", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_div_and_minus() {
        Message m = parse("foo/bar-quux");
        Message expected = msg("foo", msg("/", PersistentList.create(Arrays.asList(msg("bar"))), msg("-", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_minus_and_div_with_spaces() {
        Message m = parse("foo - bar / quux");
        Message expected = msg("foo", msg("-", PersistentList.create(Arrays.asList(msg("bar", msg("/", PersistentList.create(Arrays.asList(msg("quux")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_div_and_minus_with_spaces() {
        Message m = parse("foo / bar - quux");
        Message expected = msg("foo", msg("/", PersistentList.create(Arrays.asList(msg("bar"))), msg("-", PersistentList.create(Arrays.asList(msg("quux"))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_unary_minus() {
        Message m = parse("foo * -bar");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("-", PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_precedence_for_unary_plus() {
        Message m = parse("foo * +bar");
        Message expected = msg("foo", msg("*", PersistentList.create(Arrays.asList(msg("+", PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_simple_assignment() {
        Message m = parse("foo = bar");
        Message expected = msg("=", PersistentList.create(Arrays.asList(msg("foo"), msg("bar"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_assignment_of_expression() {
        Message m = parse("x = foo + bar");
        Message expected = msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_assignment_of_parenthesised_expression() {
        Message m = parse("x = (foo + bar)");
        Message expected = msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("", PersistentList.create(Arrays.asList(msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar")))))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_simple_assignment_with_receiver() {
        Message m = parse("Fox foo = bar");
        Message expected = msg("Fox", msg("=", PersistentList.create(Arrays.asList(msg("foo"), msg("bar")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_assignment_of_expression_with_receiver() {
        Message m = parse("Fox x = foo + bar");
        Message expected = msg("Fox", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_assignment_of_parenthesised_expression_with_receiver() {
        Message m = parse("Fox x = (foo + bar)");
        Message expected = msg("Fox", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("", PersistentList.create(Arrays.asList(msg("foo", msg("+", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void handles_simple_assignment_followed_by_newline() {
        Message m = parse("count = count + bar\ncount println");
        Message expected = msg("=", PersistentList.create(Arrays.asList(msg("count"), msg("count", msg("+", PersistentList.create(Arrays.asList(msg("bar"))))))), msg(".", msg("count", msg("println"))));
        assertThat(m, is(equalTo(expected)));
    }




    @Test
    public void plus_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x += y");
        Message expected = msg("+=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) += y");
        Message expected = msg("+=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x += y + z * bar");
        Message expected = msg("+=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void minus_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x -= y");
        Message expected = msg("-=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) -= y");
        Message expected = msg("-=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x -= y + z * bar");
        Message expected = msg("-=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }




    @Test
    public void div_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x /= y");
        Message expected = msg("/=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void div_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) /= y");
        Message expected = msg("/=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void div_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x /= y + z * bar");
        Message expected = msg("/=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void mult_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x *= y");
        Message expected = msg("*=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void mult_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) *= y");
        Message expected = msg("*=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void mult_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x *= y + z * bar");
        Message expected = msg("*=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void exp_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x **= y");
        Message expected = msg("**=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void exp_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) **= y");
        Message expected = msg("**=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void exp_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x **= y + z * bar");
        Message expected = msg("**=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void mod_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x %= y");
        Message expected = msg("%=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void mod_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) %= y");
        Message expected = msg("%=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void mod_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x %= y + z * bar");
        Message expected = msg("%=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void and_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x &= y");
        Message expected = msg("&=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void and_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) &= y");
        Message expected = msg("&=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void and_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x &= y + z * bar");
        Message expected = msg("&=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void andand_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x &&= y");
        Message expected = msg("&&=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void andand_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) &&= y");
        Message expected = msg("&&=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void andand_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x &&= y + z * bar");
        Message expected = msg("&&=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void or_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x |= y");
        Message expected = msg("|=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void or_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) |= y");
        Message expected = msg("|=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void or_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x |= y + z * bar");
        Message expected = msg("|=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void oror_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x ||= y");
        Message expected = msg("||=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void oror_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) ||= y");
        Message expected = msg("||=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void oror_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x ||= y + z * bar");
        Message expected = msg("||=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }



    @Test
    public void xor_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x ^= y");
        Message expected = msg("^=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void xor_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) ^= y");
        Message expected = msg("^=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void xor_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x ^= y + z * bar");
        Message expected = msg("^=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }


    @Test
    public void rsh_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x >>= y");
        Message expected = msg(">>=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void rsh_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) >>= y");
        Message expected = msg(">>=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void rsh_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x >>= y + z * bar");
        Message expected = msg(">>=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }


    @Test
    public void lsh_equals_should_parse_correctly_without_receiver_with_arguments() {
        Message m = parse("x <<= y");
        Message expected = msg("<<=", PersistentList.create(Arrays.asList(msg("x"), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void lsh_equals_should_parse_correctly_with_receiver_without_spaces_with_arguments() {
        Message m = parse("foo(x) <<= y");
        Message expected = msg("<<=", PersistentList.create(Arrays.asList(msg("foo", PersistentList.create(Arrays.asList(msg("x")))), msg("y"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void lsh_equals_should_parse_correctly_with_expression_on_left_hand_side() {
        Message m = parse("x <<= y + z * bar");
        Message expected = msg("<<=", PersistentList.create(Arrays.asList(msg("x"), msg("y", msg("+", PersistentList.create(Arrays.asList(msg("z", msg("*", PersistentList.create(Arrays.asList(msg("bar"))))))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_in_simple_expression() {
        Message m = parse("x++");
        Message expected = msg("++", PersistentList.create(Arrays.asList(msg("x"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_in_simple_expression_after_other_part() {
        Message m = parse("foo x++");
        Message expected = msg("foo", msg("++", PersistentList.create(Arrays.asList(msg("x")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_wrapped_as_argument() {
        Message m = parse("foo(x++)");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("++", PersistentList.create(Arrays.asList(msg("x")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_in_simple_expression_with_space() {
        Message m = parse("x ++");
        Message expected = msg("++", PersistentList.create(Arrays.asList(msg("x"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_in_simple_expression_after_other_part_with_space() {
        Message m = parse("foo x ++");
        Message expected = msg("foo", msg("++", PersistentList.create(Arrays.asList(msg("x")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_wrapped_as_argument_with_space() {
        Message m = parse("foo(x ++)");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("++", PersistentList.create(Arrays.asList(msg("x")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_as_message_send() {
        Message m = parse("++(a)");
        Message expected = msg("++", PersistentList.create(Arrays.asList(msg("a"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_as_message_send_with_receiver() {
        Message m = parse("foo ++(a)");
        Message expected = msg("foo", msg("++", PersistentList.create(Arrays.asList(msg("a")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_in_method_call_as_message_send_with_receiver() {
        Message m = parse("foo(++(a))");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("++", PersistentList.create(Arrays.asList(msg("a")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_combined_with_assignment() {
        Message m = parse("foo x = a++");
        Message expected = msg("foo", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("++", PersistentList.create(Arrays.asList(msg("a"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void plus_plus_parses_correctly_combined_with_assignment_and_receiver() {
        Message m = parse("foo x = fox a++");
        Message expected = msg("foo", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("fox", msg("++", PersistentList.create(Arrays.asList(msg("a")))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_in_simple_expression() {
        Message m = parse("x--");
        Message expected = msg("--", PersistentList.create(Arrays.asList(msg("x"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_in_simple_expression_after_other_part() {
        Message m = parse("foo x--");
        Message expected = msg("foo", msg("--", PersistentList.create(Arrays.asList(msg("x")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_wrapped_as_argument() {
        Message m = parse("foo(x--)");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("--", PersistentList.create(Arrays.asList(msg("x")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_in_simple_expression_with_space() {
        Message m = parse("x --");
        Message expected = msg("--", PersistentList.create(Arrays.asList(msg("x"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_in_simple_expression_after_other_part_with_space() {
        Message m = parse("foo x --");
        Message expected = msg("foo", msg("--", PersistentList.create(Arrays.asList(msg("x")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_wrapped_as_argument_with_space() {
        Message m = parse("foo(x --)");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("--", PersistentList.create(Arrays.asList(msg("x")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_as_message_send() {
        Message m = parse("--(a)");
        Message expected = msg("--", PersistentList.create(Arrays.asList(msg("a"))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_as_message_send_with_receiver() {
        Message m = parse("foo --(a)");
        Message expected = msg("foo", msg("--", PersistentList.create(Arrays.asList(msg("a")))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_in_method_call_as_message_send_with_receiver() {
        Message m = parse("foo(--(a))");
        Message expected = msg("foo", PersistentList.create(Arrays.asList(msg("--", PersistentList.create(Arrays.asList(msg("a")))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_combined_with_assignment() {
        Message m = parse("foo x = a--");
        Message expected = msg("foo", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("--", PersistentList.create(Arrays.asList(msg("a"))))))));
        assertThat(m, is(equalTo(expected)));
    }

    @Test
    public void minus_minus_parses_correctly_combined_with_assignment_and_receiver() {
        Message m = parse("foo x = fox a--");
        Message expected = msg("foo", msg("=", PersistentList.create(Arrays.asList(msg("x"), msg("fox", msg("--", PersistentList.create(Arrays.asList(msg("a")))))))));
        assertThat(m, is(equalTo(expected)));
    }
}// ParserTest
