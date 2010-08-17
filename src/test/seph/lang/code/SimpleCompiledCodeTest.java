/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.code;

import java.io.*;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.*;
import gnu.math.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SimpleCompiledCodeTest {
    @Test
    public void evaluate_number_in_abstraction() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("x = #(42). x"), is(equalTo(new IntNum(42))));
    }

    @Test
    public void evaluate_simple_math_in_abstraction() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("x = #(42 + 2). x"), is(equalTo(new IntNum(44))));
    }

    @Test
    public void evaluate_message_chain_in_abstraction() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("x = #(42 asText). x"), is(equalTo(new Text("42"))));
    }

    @Test
    public void evaluate_terminator_in_abstraction() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("x = #(42. 43). x"), is(equalTo(new IntNum(43))));
    }

    // abstraction in abstraction
    // referring to names in the lexical closure
    // arguments
}// SimpleCompiledCodeTest

