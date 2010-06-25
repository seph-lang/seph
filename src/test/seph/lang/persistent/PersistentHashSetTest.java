/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.persistent;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class PersistentHashSetTest {
    @Test
    public void disjoin_returns_same_object_for_set_that_doesnt_contain_object() throws Exception {
        PersistentHashSet set = PersistentHashSet.create("foo", "bar");
        assertThat(set.disjoin("fux"), is((Object)set));
    }

    @Test
    public void disjoin_returns_a_new_set_without_the_given_object() throws Exception {
        PersistentHashSet set = PersistentHashSet.create("foo", "bar");
        assertThat(set.disjoin("foo").contains("foo"), is(false));
    }

    @Test
    public void cons_returns_same_object_if_it_already_contains_the_object() throws Exception {
        PersistentHashSet set = PersistentHashSet.create("foo", "bar");
        assertThat(set.cons("foo"), is((Object)set));
    }

    @Test
    public void cons_returns_new_object_with_the_added_object() throws Exception {
        PersistentHashSet set = PersistentHashSet.create("foo", "bar");
        assertThat(set.cons("bax").contains("bax"), is(true));
    }

    @Test
    public void empty_returns_an_empty_set_with_the_same_metadata() throws Exception {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentHashSet base = new PersistentHashSet(meta, new PersistentArrayMap(new Object[]{"foo", null, "bar", null}));
        assertThat(((PersistentHashSet)base.empty()).meta(), is(meta));
    }

    @Test
    public void withMeta_returns_new_object_with_updated_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        IPersistentMap newMeta = new PersistentArrayMap(new Object[]{"baxus", "filur"});
        PersistentHashSet base = new PersistentHashSet(meta, new PersistentArrayMap(new Object[]{"foo", null, "bar", null}));

        PersistentHashSet newObject = base.withMeta(newMeta);
        assertThat(newObject.meta(), is(newMeta));
        assertThat(newObject.contains("foo"), is(true));
    }

    @Test
    public void creation_from_list() {
        PersistentHashSet set = PersistentHashSet.create(Arrays.asList("foo", "bar"));
        assertThat(set.contains("foo"), is(true));
        assertThat(set.contains("bar"), is(true));
    }

    @Test
    public void creation_from_iseq() {
        PersistentHashSet set = PersistentHashSet.create((ISeq)PersistentList.create(Arrays.asList("foo", "bar")));
        assertThat(set.contains("foo"), is(true));
        assertThat(set.contains("bar"), is(true));
    }

    @Test
    public void creation_with_checking() {
        PersistentHashSet set = PersistentHashSet.createWithCheck("foo", "bar");
        assertThat(set.contains("foo"), is(true));
        assertThat(set.contains("bar"), is(true));
    }

    @Test(expected=IllegalArgumentException.class)
    public void creation_with_checking_throws_exception_on_duplicate_key() {
        PersistentHashSet.createWithCheck("foo", "bar", "foo");
    }

    @Test
    public void creation_with_checking_from_list() {
        PersistentHashSet set = PersistentHashSet.createWithCheck(Arrays.asList("foo", "bar"));
        assertThat(set.contains("foo"), is(true));
        assertThat(set.contains("bar"), is(true));
    }

    @Test(expected=IllegalArgumentException.class)
    public void creation_with_checking_from_list_throws_exception_on_duplicate_key() {
        PersistentHashSet.createWithCheck(Arrays.asList("foo", "bar", "foo"));
    }

    @Test
    public void creation_with_checking_from_iseq() {
        PersistentHashSet set = PersistentHashSet.createWithCheck((ISeq)PersistentList.create(Arrays.asList("foo", "bar")));
        assertThat(set.contains("foo"), is(true));
        assertThat(set.contains("bar"), is(true));
    }

    @Test(expected=IllegalArgumentException.class)
    public void creation_with_checking_from_iseq_throws_exception_on_duplicate_key() {
        PersistentHashSet.createWithCheck((ISeq)PersistentList.create(Arrays.asList("foo", "bar", "foo")));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void add_fails() {
        PersistentHashSet.create("foo").add(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void addAll_fails() {
        PersistentHashSet.create("foo").addAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_fails() {
        PersistentHashSet.create("foo").remove(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void clear_fails() {
        PersistentHashSet.create("foo").clear();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void retainAll_fails() {
        PersistentHashSet.create("foo").retainAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void removeAll_fails() {
        PersistentHashSet.create("foo").removeAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_int_fails() {
        PersistentHashSet.create("foo").remove(0);
    }
}// PersistentHashSetTest
