/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class RuntimeTest {
    @Test
    public void ensure_class_exists() {
        assertEquals("seph.lang.Runtime", seph.lang.Runtime.class.getName());
    }
}// RuntimeTest
