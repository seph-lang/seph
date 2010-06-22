/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.persistent;

import org.junit.Test;
import static org.junit.Assert.*;

import seph.lang.Text;
import seph.lang.SephObject;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PersistentListTest {
    @Test
    public void first_will_return_the_given_first_value() {
        SephObject expected = new Text("foo");
        assertSame(expected, new PersistentList(expected).first());
    }

    @Test
    public void cons_returns_a_cons_with_first_as_the_object_given() {
        SephObject expected = new Text("foo");
        assertSame(expected, new PersistentList(null).cons(expected).first());
    }

    @Test
    public void cons_returns_a_new_object() {
        PersistentList pc = new PersistentList(null);
        assertNotSame(pc, pc.cons(null));
    }

    @Test
    public void next_returns_null_for_a_simple_cons() {
        assertNull(new PersistentList(null).next());
    }

    @Test
    public void next_returns_the_next_element() {
        PersistentList pc = new PersistentList(null);
        assertSame(pc, pc.cons(null).next());
    }
}// PersistentListTest
