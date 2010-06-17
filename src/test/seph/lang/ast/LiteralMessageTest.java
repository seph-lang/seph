/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import seph.lang.SephObject;
import seph.lang.Text;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class LiteralMessageTest {
    @Test
    public void ensure_class_exists() {
        assertEquals("seph.lang.ast.LiteralMessage", seph.lang.ast.LiteralMessage.class.getName());
    }

    @Test
    public void is_a_seph_object() {
        assertTrue("A LiteralMessage should be a SephObject", new LiteralMessage(null, null, null, 0, 0) instanceof SephObject);
    }

    @Test
    public void is_a_message() {
        assertTrue("A LiteralMessage should be a Message", new LiteralMessage(null, null, null, 0, 0) instanceof Message);
    }

    @Test
    public void has_a_next_pointer() {
        Message one = new LiteralMessage(null, null, null, 0, 0);
        LiteralMessage two = new LiteralMessage(null, one, null, 0, 0);
        assertSame(one, two.next());
    }

    @Test
    public void has_a_name_based_one_the_literal_class() {
        LiteralMessage msg = new LiteralMessage(new Text("foo"), null, null, 0, 0);
        assertEquals("<literal:seph.lang.Text>", msg.name());
    }

    @Test
    public void has_an_empty_list_of_arguments() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 0, 0);
        assertEquals(Arrays.<Message>asList(), msg.arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_literal() {
        SephObject lit = new Text("bar");
        LiteralMessage msg = new LiteralMessage(lit, null, null, 0, 0);
        assertSame(lit, msg.withNext(null).literal());
    }

    @Test
    public void with_next_returns_a_new_object_with_a_new_next() {
        Message expected = new LiteralMessage(null, null, null, 0, 0);
        LiteralMessage msg = new LiteralMessage(null, null, null, 0, 0);
        assertSame(expected, msg.withNext(expected).next());
    }

    @Test
    public void literal_returns_the_literal_given() {
        SephObject lit = new Text("bar");
        LiteralMessage msg = new LiteralMessage(lit, null, null, 0, 0);
        assertSame(lit, msg.literal());
    }

    @Test
    public void is_literal_returns_true() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 0, 0);
        assertTrue(msg.isLiteral());
    }

    @Test
    public void has_a_filename() {
        LiteralMessage msg = new LiteralMessage(null, null, "foobar.sp", 0, 0);
        assertEquals("foobar.sp", msg.filename());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_filename() {
        LiteralMessage msg = new LiteralMessage(null, null, "blarg.sp", 0, 0);
        assertEquals("blarg.sp", msg.withNext(null).filename());
    }

    @Test
    public void has_a_line() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 42, 0);
        assertEquals(42, msg.line());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_line() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 23, 0);
        assertEquals(23, msg.withNext(null).line());
    }

    @Test
    public void has_a_position() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 0, 13);
        assertEquals(13, msg.position());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_position() {
        LiteralMessage msg = new LiteralMessage(null, null, null, 0, 55);
        assertEquals(55, msg.withNext(null).position());
    }
}// LiteralMessageTest
