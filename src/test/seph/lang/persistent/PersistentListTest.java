/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.persistent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.Text;
import seph.lang.SephObject;
import seph.lang.SimpleSephObject;

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

    @Test
    public void peek_returns_first_element() {
        Object obj = new Object();
        assertThat(new PersistentList(obj).peek(), is(obj));
    }

    @Test
    public void pop_returns_new_with_same_meta_data_if_empty() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentList base = new PersistentList(meta, null, null, 1);
        assertThat(((SimpleSephObject)base.pop()).meta(), is(meta));
    }

    @Test
    public void pop_returns_rest_if_not_null() {
        PersistentList rest = new PersistentList(null);;
        PersistentList base = new PersistentList(null, null, rest, 2);
        assertThat(base.pop(), is((IPersistentList)rest));
    }

    @Test
    public void empty_returns_empty_with_same_meta_data() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentList rest = new PersistentList(null);;
        PersistentList base = new PersistentList(meta, null, rest, 2);
        assertThat(((SimpleSephObject)base.empty()).meta(), is(meta));
    }

    @Test
    public void withMeta_returns_same_if_given_the_same_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentList base = new PersistentList(meta, null, null, 1);
        assertThat(base.withMeta(meta), is(base));
    }

    @Test
    public void withMeta_returns_new_if_given_the_a_new_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        IPersistentMap newMeta = new PersistentArrayMap(new Object[]{"baxus", "filur"});
        PersistentList rest = new PersistentList(meta, null, null, 1);
        Object some = new Object();
        PersistentList base = new PersistentList(meta, some, rest, 42);

        PersistentList newObject = base.withMeta(newMeta);
        assertThat(newObject.meta(), is(newMeta));
        assertThat((PersistentList)newObject.next(), is(rest));
        assertThat(newObject.count(), is(42));
        assertThat(newObject.first(), is(some));
    }


    // EmptyList.hashcode()
    // EmptyList.equals()
    // EmptyList.equiv()
    // EmptyList.first()
    // EmptyList.next()
    // EmptyList.more()
    // EmptyList.empty()
    // EmptyList.withMeta()
    // EmptyList.peek()
    // EmptyList.pop()
    // EmptyList.size()
    // EmptyList.isEmpty()
    // EmptyList.contains()
    // EmptyList.iterator()
    // EmptyList.add()
    // EmptyList.remove()
    // EmptyList.addAll()
    // EmptyList.clear()
    // EmptyList.retainAll()
    // EmptyList.removeAll()
    // EmptyList.containsAll()
    // EmptyList.toArray()
    // EmptyList.sublist()
    // EmptyList.set()
    // EmptyList.remove()
    // EmptyList.indexOf()
    // EmptyList.lastIndexOf()
    // EmptyList.listIterator()
    // EmptyList.get()
    // EmptyList.add()
    // EmptyList.addAll()
}// PersistentListTest
