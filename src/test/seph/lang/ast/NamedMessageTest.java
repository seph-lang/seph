/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import seph.lang.SephObject;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class NamedMessageTest {
    @Test
    public void ensure_class_exists() {
        assertEquals("seph.lang.ast.NamedMessage", seph.lang.ast.NamedMessage.class.getName());
    }

    @Test
    public void is_a_seph_object() {
        assertTrue("A NamedMessage should be a SephObject", new NamedMessage(null, null, null) instanceof SephObject);
    }

    @Test
    public void is_a_message() {
        assertTrue("A NamedMessage should be a Message", new NamedMessage(null, null, null) instanceof Message);
    }

    @Test
    public void has_a_next_pointer() {
        Message one = new NamedMessage(null, null, null);
        NamedMessage two = new NamedMessage(null, null, one);
        assertSame(one, two.next());
    }

    @Test
    public void has_a_name() {
        NamedMessage msg = new NamedMessage("foo", null, null);
        assertEquals("foo", msg.name());
    }

    @Test
    public void has_a_list_of_arguments() {
        List<Message> args = Arrays.<Message>asList(new NamedMessage(null, null, null), new NamedMessage(null, null, null));
        NamedMessage msg = new NamedMessage(null, args, null);
        assertSame(args, msg.arguments());
    }

    @Test
    public void arguments_return_an_empty_array_if_given_null() {
        NamedMessage msg = new NamedMessage(null, null, null);
        assertEquals(Arrays.<Message>asList(), msg.arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_name() {
        NamedMessage msg = new NamedMessage("fox", null, null);
        assertEquals("fox", msg.withNext(null).name());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_arguments() {
        List<Message> expected = new ArrayList<Message>();
        NamedMessage msg = new NamedMessage(null, expected, null);
        assertSame(expected, msg.withNext(null).arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_a_new_next() {
        Message expected = new NamedMessage(null, null, null);
        NamedMessage msg = new NamedMessage(null, null, null);
        assertSame(expected, msg.withNext(expected).next());
    }
}// NamedMessageTest
