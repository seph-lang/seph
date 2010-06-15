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
        assertTrue("A LiteralMessage should be a SephObject", new LiteralMessage(null, null) instanceof SephObject);
    }

    @Test
    public void is_a_message() {
        assertTrue("A LiteralMessage should be a Message", new LiteralMessage(null, null) instanceof Message);
    }

    @Test
    public void has_a_next_pointer() {
        Message one = new LiteralMessage(null, null);
        LiteralMessage two = new LiteralMessage(null, one);
        assertSame(one, two.next());
    }

    @Test
    public void has_a_name_based_one_the_literal_class() {
        LiteralMessage msg = new LiteralMessage(new Text("foo"), null);
        assertEquals("<literal:seph.lang.Text>", msg.name());
    }

    @Test
    public void has_an_empty_list_of_arguments() {
        LiteralMessage msg = new LiteralMessage(null, null);
        assertEquals(Arrays.<Message>asList(), msg.arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_literal() {
        SephObject lit = new Text("bar");
        LiteralMessage msg = new LiteralMessage(lit, null);
        assertSame(lit, msg.withNext(null).literal());
    }

    @Test
    public void with_next_returns_a_new_object_with_a_new_next() {
        Message expected = new LiteralMessage(null, null);
        LiteralMessage msg = new LiteralMessage(null, null);
        assertSame(expected, msg.withNext(expected).next());
    }

    @Test
    public void literal_returns_the_literal_given() {
        SephObject lit = new Text("bar");
        LiteralMessage msg = new LiteralMessage(lit, null);
        assertSame(lit, msg.literal());
    }

    @Test
    public void is_literal_returns_true() {
        LiteralMessage msg = new LiteralMessage(null, null);
        assertTrue(msg.isLiteral());
    }
}// LiteralMessageTest
