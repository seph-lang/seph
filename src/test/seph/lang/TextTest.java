/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class TextTest {
    @Test
    public void is_a_seph_object() {
        assertTrue("A Text should be a SephObject", new Text("foo") instanceof SephObject);
    }
}// TextTest
