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
public class PersistentArrayMapTest {
    @Test
    public void containsKey_works_for_checking_if_null_is_in_the_map() {
        assertThat(new PersistentArrayMap(new Object[]{}).containsKey(null), is(false));
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).containsKey(null), is(false));
        assertThat(new PersistentArrayMap(new Object[]{"foo", null}).containsKey(null), is(false));
        assertThat(new PersistentArrayMap(new Object[]{(Object)null, "bar"}).containsKey(null), is(true));
    }

    @Test
    public void containsKey_works_for_non_null_objects() {
        assertThat(new PersistentArrayMap(new Object[]{}).containsKey("bar"), is(false));
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).containsKey("foo"), is(true));
        assertThat(new PersistentArrayMap(new Object[]{"fox", "mux", "foo", "bar"}).containsKey("foo"), is(true));
        assertThat(new PersistentArrayMap(new Object[]{"fox", "mux", "foo", "bar"}).containsKey("max"), is(false));
    }

    @Test
    public void entryAt_returns_the_value_for_null() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", (Object)null, "something"}).entryAt(null).key(), is((Object)null));
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", (Object)null, "something"}).entryAt(null).val(), is((Object)"something"));
    }

    @Test
    public void entryAt_returns_null_for_non_existing_key() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).entryAt(null), is((Object)null));
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).entryAt("fox"), is((Object)null));
    }

    @Test
    public void entryAt_returns_correct_entry_for_a_value() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", "quux", "max"}).entryAt("quux").key(), is((Object)"quux"));
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", "quux", "max"}).entryAt("quux").val(), is((Object)"max"));
    }

    @Test
    public void valueAt_returns_the_value_for_null() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", (Object)null, "something"}).valueAt(null), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_null_for_non_existing_key() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).valueAt("fox"), is((Object)null));
    }

    @Test
    public void valueAt_returns_correct_entry_for_a_value() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", "quux", "max"}).valueAt("quux"), is((Object)"max"));
    }

    @Test
    public void valueAt_2_returns_the_value_for_null() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", (Object)null, "something"}).valueAt(null, "fox"), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_default_value_for_non_existent_key() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).valueAt("fox", "something"), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_default_value_for_null_when_not_there() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar"}).valueAt(null, "something"), is((Object)"something"));
    }

    @Test
    public void valueAt_2_returns_correct_entry_for_a_value() {
        assertThat(new PersistentArrayMap(new Object[]{"foo", "bar", "quux", "max"}).valueAt("quux", "muxie"), is((Object)"max"));
    }

    @Test
    public void valueAt_2_returns_null_if_the_root_is_null() {
        assertThat(new PersistentArrayMap(null, new Object[0]).valueAt("quux"), is((Object)null));
    }

    @Test
    public void valueAt_2_returns_notfound_if_the_root_is_null() {
        assertThat(new PersistentArrayMap(null, new Object[0]).valueAt("quux", "muxie"), is((Object)"muxie"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void create_with_check_fails_if_given_a_duplicate_key() {
        PersistentArrayMap.createWithCheck(new Object[]{"foo", "bar", "foo", "mux"});
    }

    @Test
    public void create_with_check_returns_a_working_map() {
        assertThat(PersistentArrayMap.createWithCheck(new Object[]{"foo", "bar", "fox", "mux"}).valueAt("fox"), is((Object)"mux"));;
    }

    @Test
    public void create_with_meta_gives_a_working_map() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentArrayMap m = new PersistentArrayMap(meta, new Object[]{"foo", "bar"});
        assertThat(m.valueAt("foo"), is((Object)"bar"));
        assertThat(m.meta(), is(meta));
    }

    @Test
    public void associate_with_null_key_and_same_value_returns_same_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", null, "mux"});
        assertThat(m.associate(null, "mux"), is((Object)m));
    }

    @Test
    public void associate_with_null_key_and_different_value_returns_new_map_with_that_value() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", null, "max"});
        assertThat(m.associate(null, "mux").valueAt(null), is((Object)"mux"));
    }

    @Test
    public void associate_with_null_key_returns_new_map_with_that_value() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar"});
        assertThat(m.associate(null, "mux").valueAt(null), is((Object)"mux"));
    }


    @Test
    public void associate_with_same_key_and_same_value_returns_same_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", "something", "mux"});
        assertThat(m.associate("something", "mux"), is((Object)m));
    }

    @Test
    public void associate_with_same_key_and_different_value_returns_new_map_with_that_value() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", "something", "max"});
        assertThat(m.associate("something", "mux").valueAt("something"), is((Object)"mux"));
    }

    @Test
    public void associate_returns_new_map_with_that_value() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar"});
        assertThat(m.associate("something", "mux").valueAt("something"), is((Object)"mux"));
    }

    @Test
    public void associateOrFail_returns_new_map_with_that_value() throws Exception {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar"});
        assertThat(m.associateOrFail("something", "mux").valueAt("something"), is((Object)"mux"));
    }

    @Test(expected=Exception.class)
    public void associateOrFail_fails_if_the_same_key_already_exists() throws Exception {
        new PersistentArrayMap(new Object[]{"foo", "bar", "something", "else"}).associateOrFail("something", "mux");
    }

    @Test
    public void without_returns_the_same_map_if_given_null_but_not_having_null() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar"});
        assertThat(m.without(null), is((Object)m));
    }

    @Test
    public void without_null_returns_a_new_map_without_null() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", null, "max"});
        assertThat(m.without(null).containsKey(null), is(false));
    }

    @Test
    public void without_non_existing_value_returns_the_same_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar"});
        assertThat(m.without("blarg"), is((Object)m));
    }

    @Test
    public void without_value_returns_a_new_map_without_null() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", "sox", "max"});
        assertThat(m.without("sox").containsKey("sox"), is(false));
    }

    @Test
    public void without_returns_self_when_no_root_exists() {
        PersistentArrayMap m = new PersistentArrayMap(null, new Object[0]);
        assertThat(m.without("sox"), is((Object)m));
    }

    @Test
    public void count_returns_1_for_a_map_with_only_null() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{null, null, "something"});
        assertThat(m.count(), is(1));
    }

    @Test
    public void count_returns_0_for_an_empty_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{});
        assertThat(m.count(), is(0));
    }

    @Test
    public void count_returns_a_valid_count_for_a_larger_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"foo", "bar", 
                                                       null, "x", 
                                                       "y", "q", 
                                                       "p", "r", 
                                                       "r", "q", 
                                                       "q", "s",
                                                       "s", "t",
                                                       "a", "b", 
                                                       "b", "c", 
                                                       "c", "d", 
                                                       "d", "e", 
                                                       "e", "f",
                                                       "f", "g",
                                                       "g", "h",
                                                       "h", "i",
                                                       "i", "j", 
                                                       "j", "k"});
        assertThat(m.count(), is(17));
    }

    @Test
    public void withMeta_returns_same_if_given_the_same_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentArrayMap base = new PersistentArrayMap(meta, new Object[]{"foo", "bar"});
        assertThat(base.withMeta(meta), is(base));
    }

    @Test
    public void withMeta_returns_new_if_given_the_a_new_meta() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        IPersistentMap newMeta = new PersistentArrayMap(new Object[]{"baxus", "filur"});
        PersistentArrayMap base = new PersistentArrayMap(meta, new Object[]{"foo", "bar"});

        PersistentArrayMap newObject = base.withMeta(newMeta);
        assertThat(newObject.meta(), is(newMeta));
        assertThat(newObject.valueAt("foo"), is((Object)"bar"));
    }

    @Test
    public void seq_returns_null_for_an_empty_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{});
        assertThat(m.seq(), is((Object)null));
    }

    @Test
    public void seq_returns_a_simple_cons_for_a_null_value() {
        PersistentArrayMap m = new PersistentArrayMap(null, new Object[]{null, "bar"});
        assertThat(((MapEntry)m.seq().first()).key(), is((Object)null));
        assertThat(((MapEntry)m.seq().first()).val(), is((Object)"bar"));
        assertThat(m.seq().next(), is((Object)null));
    }

    @Test
    public void seq_returns_a_sequence_of_values_for_a_non_empty_map() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"something", "bar"});
        assertThat(((MapEntry)m.seq().first()).key(), is((Object)"something"));
        assertThat(((MapEntry)m.seq().first()).val(), is((Object)"bar"));
        assertThat(m.seq().next(), is((Object)null));
    }

    @Test
    public void seq_returns_a_sequence_of_values_that_come_in_insertion_order() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"something", "bar", null, "blax"});
        assertThat(((MapEntry)m.seq().first()).key(), is((Object)"something"));
        assertThat(((MapEntry)m.seq().first()).val(), is((Object)"bar"));
        assertThat(((MapEntry)m.seq().next().first()).key(), is((Object)null));
        assertThat(((MapEntry)m.seq().next().first()).val(), is((Object)"blax"));
        assertThat(m.seq().next().next(), is((Object)null));
    }

    @Test
    public void iterator_returns_an_iterator() {
        PersistentArrayMap m = new PersistentArrayMap(new Object[]{"something", "bar"});
        assertThat(((MapEntry)m.iterator().next()).key(), is((Object)"something"));
        assertThat(((MapEntry)m.iterator().next()).val(), is((Object)"bar"));
    }

    @Test
    public void empty_returns_empty_with_same_meta_data() {
        IPersistentMap meta = new PersistentArrayMap(new Object[]{"foo", "bar"});
        PersistentArrayMap base = new PersistentArrayMap(meta, new Object[]{"foo", "bar"});
        assertThat(((PersistentArrayMap)base.empty()).meta(), is(meta));
    }
}// PersistentArrayMapTest
