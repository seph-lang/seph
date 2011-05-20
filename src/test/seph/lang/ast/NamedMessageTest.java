/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import seph.lang.SephObject;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;

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
        assertTrue("A NamedMessage should be a SephObject", NamedMessage.create(null, null, null, null, 0, 0, null) instanceof SephObject);
    }

    @Test
    public void is_a_message() {
        assertTrue("A NamedMessage should be a Message", NamedMessage.create(null, null, null, null, 0, 0, null) instanceof Message);
    }

    @Test
    public void has_a_next_pointer() {
        Message one = NamedMessage.create(null, null, null, null, 0, 0, null);
        NamedMessage two = NamedMessage.create(null, null, one, null, 0, 0, null);
        assertSame(one, two.next());
    }

    @Test
    public void has_a_name() {
        NamedMessage msg = NamedMessage.create("foo", null, null, null, 0, 0, null);
        assertEquals("foo", msg.name());
    }

    @Test
    public void has_a_list_of_arguments() {
        IPersistentList args = PersistentList.create(Arrays.<Message>asList(NamedMessage.create(null, null, null, null, 0, 0, null), NamedMessage.create(null, null, null, null, 0, 0, null)));
        NamedMessage msg = NamedMessage.create(null, args, null, null, 0, 0, null);
        assertSame(args, msg.arguments());
    }

    @Test
    public void arguments_return_an_empty_array_if_given_null() {
        NamedMessage msg = NamedMessage.create(null, null, null, null, 0, 0, null);
        assertEquals(PersistentList.EMPTY, msg.arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_name() {
        NamedMessage msg = NamedMessage.create("fox", null, null, null, 0, 0, null);
        assertEquals("fox", msg.withNext(null).name());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_arguments() {
        IPersistentList expected = new PersistentList(null);
        NamedMessage msg = NamedMessage.create(null, expected, null, null, 0, 0, null);
        assertSame(expected, msg.withNext(null).arguments());
    }

    @Test
    public void with_next_returns_a_new_object_with_a_new_next() {
        Message expected = NamedMessage.create(null, null, null, null, 0, 0, null);
        NamedMessage msg = NamedMessage.create(null, null, null, null, 0, 0, null);
        assertSame(expected, msg.withNext(expected).next());
    }

    @Test
    public void has_a_filename() {
        NamedMessage msg = NamedMessage.create(null, null, null, "foobar.sp", 0, 0, null);
        assertEquals("foobar.sp", msg.filename());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_filename() {
        NamedMessage msg = NamedMessage.create(null, null, null, "blarg.sp", 0, 0, null);
        assertEquals("blarg.sp", msg.withNext(null).filename());
    }

    @Test
    public void has_a_line() {
        NamedMessage msg = NamedMessage.create(null, null, null, null, 42, 0, null);
        assertEquals(42, msg.line());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_line() {
        NamedMessage msg = NamedMessage.create(null, null, null, null, 23, 0, null);
        assertEquals(23, msg.withNext(null).line());
    }

    @Test
    public void has_a_position() {
        NamedMessage msg = NamedMessage.create(null, null, null, null, 0, 13, null);
        assertEquals(13, msg.position());
    }

    @Test
    public void with_next_returns_a_new_object_with_the_same_position() {
        NamedMessage msg = NamedMessage.create(null, null, null, null, 0, 55, null);
        assertEquals(55, msg.withNext(null).position());
    }
}// NamedMessageTest
