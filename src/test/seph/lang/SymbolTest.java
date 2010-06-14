/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SymbolTest {
    @Test
    public void symbol_class_should_be_a_seph_object() {
        assertTrue("A Symbol should be a SephObject", new Symbol() instanceof SephObject);
    }
}// SymbolTest
