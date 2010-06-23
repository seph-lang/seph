/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.persistent;

import java.util.*;

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

    // EmptyList

    @Test
    public void hashcode_returns_zero() {
        assertThat(new PersistentList.EmptyList(null).hashCode(), is(1));
    }

    @Test
    public void is_equals_to_another_empty_list() {
        assertThat(new PersistentList.EmptyList(null), is(equalTo(new PersistentList.EmptyList(null))));
    }

    @Test
    public void is_equals_to_another_empty_java_util_list() {
        assertThat(new PersistentList.EmptyList(null), is(equalTo((Object)new ArrayList())));
    }

    @Test
    public void is_not_equal_to_something_totally_different() {
        assertThat(new PersistentList.EmptyList(null), is(not(equalTo(new Object()))));
    }

    @Test
    public void is_not_equal_to_something_that_is_not_empty() {
        assertThat(new PersistentList.EmptyList(null), is(not(equalTo((Object)Arrays.asList("foo")))));
    }

    @Test
    public void is_equiv_to_another_empty_list() {
        assertThat(new PersistentList.EmptyList(null).equiv(new PersistentList.EmptyList(null)), is(true));
    }

    @Test
    public void is_equiv_to_another_empty_java_util_list() {
        assertThat(new PersistentList.EmptyList(null).equiv((Object)new ArrayList()), is(true));
    }

    @Test
    public void is_not_equiv_to_something_totally_different() {
        assertThat(new PersistentList.EmptyList(null).equiv(new Object()), is(false));
    }

    @Test
    public void is_not_equiv_to_something_that_is_not_empty() {
        assertThat(new PersistentList.EmptyList(null).equiv(Arrays.asList("foo")), is(false));
    }

    @Test
    public void first_returns_null() {
        assertThat(new PersistentList.EmptyList(null).first(), is((Object)null));
    }

    @Test
    public void next_returns_null() {
        assertThat(new PersistentList.EmptyList(null).next(), is((ISeq)null));
    }

    @Test
    public void more_returns_same() {
        ISeq l = new PersistentList.EmptyList(null);
        assertThat(l.more(), is((ISeq)l));
    }

    @Test
    public void empty_returns_same() {
        IPersistentList l = new PersistentList.EmptyList(null);
        assertThat(l.empty(), is((IPersistentCollection)l));
    }


    @Test
    public void withMeta_on_empty_returns_same_if_given_the_same_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentList.EmptyList base = new PersistentList.EmptyList(meta);
        assertThat(base.withMeta(meta), is(base));
    }

    @Test
    public void withMeta_on_empty_returns_new_if_given_the_a_new_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        IPersistentMap newMeta = new PersistentArrayMap(new Object[]{"baxus", "filur"});
        PersistentList rest = new PersistentList(meta, null, null, 1);
        PersistentList.EmptyList base = new PersistentList.EmptyList(meta);

        PersistentList.EmptyList newObject = base.withMeta(newMeta);
        assertThat(newObject.meta(), is(newMeta));
        assertThat(newObject == base, is(false));
    }

    @Test
    public void peek_returns_null() {
        assertThat(new PersistentList.EmptyList(null).peek(), is((Object)null));
    }

    @Test(expected=IllegalStateException.class)
    public void pop_fails() {
        new PersistentList.EmptyList(null).pop();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void add_fails() {
        new PersistentList.EmptyList(null).add(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void addAll_fails() {
        new PersistentList.EmptyList(null).addAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_fails() {
        new PersistentList.EmptyList(null).remove(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void clear_fails() {
        new PersistentList.EmptyList(null).clear();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void retainAll_fails() {
        new PersistentList.EmptyList(null).retainAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void removeAll_fails() {
        new PersistentList.EmptyList(null).removeAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void set_fails() {
        new PersistentList.EmptyList(null).set(0, new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_int_fails() {
        new PersistentList.EmptyList(null).remove(0);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void add_2_fails() {
        new PersistentList.EmptyList(null).add(0, new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void addAll_2_fails() {
        new PersistentList.EmptyList(null).addAll(0, new ArrayList());
    }

    @Test
    public void size_is_zero() {
        assertThat(new PersistentList.EmptyList(null).size(), is(0));
    }

    @Test
    public void isEmpty_always_true() {
        assertThat(new PersistentList.EmptyList(null).isEmpty(), is(true));
    }

    @Test
    public void contains_always_false() {
        assertThat(new PersistentList.EmptyList(null).contains(new Object()), is(false));
        assertThat(new PersistentList.EmptyList(null).contains("foo"), is(false));
    }

    @Test
    public void containsAll_returns_true_for_an_empty_collection() {
        assertThat(new PersistentList.EmptyList(null).containsAll(new ArrayList()), is(true));
    }

    @Test
    public void containsAll_returns_false_for_a_nonempty_collection() {
        assertThat(new PersistentList.EmptyList(null).containsAll(Arrays.asList("String")), is(false));
    }

    @Test
    public void toArray_returns_given_array() {
        Object[] arr = new Object[0];
        assertThat(new PersistentList.EmptyList(null).toArray(arr) == arr, is(true));
    }

    @Test
    public void toArray_sets_first_element_to_null_if_array_larger_than_zero() {
        Object[] arr = new Object[] {new Object()};
        new PersistentList.EmptyList(null).toArray(arr);
        assertThat(arr[0], is((Object)null));
    }

    @Test
    public void sublist_returns_an_empty_list_for_zero_indices() {
        assertThat(new PersistentList.EmptyList(null).subList(0,0).isEmpty(), is(true));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void subList_throws_an_error_if_given_a_window_over_zero() {
        new PersistentList.EmptyList(null).subList(0,1);
    }

    @Test
    public void indexOf_returns_minus_one() {
        assertThat(new PersistentList.EmptyList(null).indexOf("foo"), is(-1));
    }

    @Test
    public void lastIndexOf_returns_minus_one() {
        assertThat(new PersistentList.EmptyList(null).lastIndexOf("foo"), is(-1));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void get_returns_null() {
        new PersistentList.EmptyList(null).get(0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void listIterator_from_other_index_than_zero() {
        new PersistentList.EmptyList(null).listIterator(1);
    }

    @Test
    public void listIterator_from_zero_returns_iterator_that_doesnt_have_next() {
        assertThat(new PersistentList.EmptyList(null).listIterator(0).hasNext(), is(false));
    }

    @Test
    public void listIterator_returns_iterator_that_doesnt_have_next() {
        assertThat(new PersistentList.EmptyList(null).listIterator().hasNext(), is(false));
    }

    @Test
    public void iterator_returns_iterator_that_doesnt_have_next() {
        assertThat(new PersistentList.EmptyList(null).iterator().hasNext(), is(false));
    }

    @Test(expected=NoSuchElementException.class)
    public void iterator_returns_iterator_that_doesnt_have_any_next_value() {
        new PersistentList.EmptyList(null).iterator().next();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void iterator_returns_iterator_that_doesnt_support_remove() {
        new PersistentList.EmptyList(null).iterator().remove();
    }
}// PersistentListTest
