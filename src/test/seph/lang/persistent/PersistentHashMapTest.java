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
public class PersistentHashMapTest {
    @Test
    public void containsKey_works_for_checking_if_null_is_in_the_map() {
        assertThat(PersistentHashMap.create().containsKey(null), is(false));
        assertThat(PersistentHashMap.create("foo", "bar").containsKey(null), is(false));
        assertThat(PersistentHashMap.create("foo", null).containsKey(null), is(false));
        assertThat(PersistentHashMap.create((Object)null, "bar").containsKey(null), is(true));
    }

    @Test
    public void containsKey_works_for_non_null_objects() {
        assertThat(PersistentHashMap.create().containsKey("bar"), is(false));
        assertThat(PersistentHashMap.create("foo", "bar").containsKey("foo"), is(true));
        assertThat(PersistentHashMap.create("fox", "mux", "foo", "bar").containsKey("foo"), is(true));
        assertThat(PersistentHashMap.create("fox", "mux", "foo", "bar").containsKey("max"), is(false));
    }

    @Test
    public void entryAt_returns_the_value_for_null() {
        assertThat(PersistentHashMap.create("foo", "bar", (Object)null, "something").entryAt(null).key(), is((Object)null));
        assertThat(PersistentHashMap.create("foo", "bar", (Object)null, "something").entryAt(null).val(), is((Object)"something"));
    }

    @Test
    public void entryAt_returns_null_for_non_existing_key() {
        assertThat(PersistentHashMap.create("foo", "bar").entryAt(null), is((Object)null));
        assertThat(PersistentHashMap.create("foo", "bar").entryAt("fox"), is((Object)null));
    }

    @Test
    public void entryAt_returns_correct_entry_for_a_value() {
        assertThat(PersistentHashMap.create("foo", "bar", "quux", "max").entryAt("quux").key(), is((Object)"quux"));
        assertThat(PersistentHashMap.create("foo", "bar", "quux", "max").entryAt("quux").val(), is((Object)"max"));
    }

    @Test
    public void entryAt_returns_null_if_root_is_null() {
        assertThat(new PersistentHashMap(null, 0, null, false, null).entryAt("quux"), is((Object)null));
    }

    @Test
    public void valueAt_returns_the_value_for_null() {
        assertThat(PersistentHashMap.create("foo", "bar", (Object)null, "something").valueAt(null), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_null_for_non_existing_key() {
        assertThat(PersistentHashMap.create("foo", "bar").valueAt("fox"), is((Object)null));
    }

    @Test
    public void valueAt_returns_correct_entry_for_a_value() {
        assertThat(PersistentHashMap.create("foo", "bar", "quux", "max").valueAt("quux"), is((Object)"max"));
    }

    @Test
    public void valueAt_2_returns_the_value_for_null() {
        assertThat(PersistentHashMap.create("foo", "bar", (Object)null, "something").valueAt(null, "fox"), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_default_value_for_non_existent_key() {
        assertThat(PersistentHashMap.create("foo", "bar").valueAt("fox", "something"), is((Object)"something"));
    }

    @Test
    public void valueAt_returns_default_value_for_null_when_not_there() {
        assertThat(PersistentHashMap.create("foo", "bar").valueAt(null, "something"), is((Object)"something"));
    }

    @Test
    public void valueAt_2_returns_correct_entry_for_a_value() {
        assertThat(PersistentHashMap.create("foo", "bar", "quux", "max").valueAt("quux", "muxie"), is((Object)"max"));
    }

    @Test
    public void valueAt_2_returns_null_if_the_root_is_null() {
        assertThat(new PersistentHashMap(0, null, false, null).valueAt("quux"), is((Object)null));
    }

    @Test
    public void valueAt_2_returns_notfound_if_the_root_is_null() {
        assertThat(new PersistentHashMap(0, null, false, null).valueAt("quux", "muxie"), is((Object)"muxie"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void create_with_check_fails_if_given_a_duplicate_key() {
        PersistentHashMap.createWithCheck("foo", "bar", "foo", "mux");
    }

    @Test
    public void create_with_check_returns_a_working_map() {
        assertThat(PersistentHashMap.createWithCheck("foo", "bar", "fox", "mux").valueAt("fox"), is((Object)"mux"));;
    }
}// PersistentHashMapTest
