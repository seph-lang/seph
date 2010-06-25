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
public class PersistentVectorTest {
    @Test(expected=UnsupportedOperationException.class)
    public void add_fails() {
        PersistentVector.create("foo").add(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void addAll_fails() {
        PersistentVector.create("foo").addAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_fails() {
        PersistentVector.create("foo").remove(new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void clear_fails() {
        PersistentVector.create("foo").clear();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void retainAll_fails() {
        PersistentVector.create("foo").retainAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void removeAll_fails() {
        PersistentVector.create("foo").removeAll(new ArrayList());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void set_fails() {
        PersistentVector.create("foo").set(0, new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void remove_int_fails() {
        PersistentVector.create("foo").remove(0);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void add_2_fails() {
        PersistentVector.create("foo").add(0, new Object());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void addAll_2_fails() {
        PersistentVector.create("foo").addAll(0, new ArrayList());
    }
}// PersistentVectorTest
