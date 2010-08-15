/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.code;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.*;
import gnu.math.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class BasicSanityTest {
    @Test
    public void evaluate_text() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("\"hello world\""), is(equalTo(new Text("hello world"))));
    }

    @Test
    public void evaluate_number() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("42"), is(equalTo(new IntNum(42))));
    }

    @Test
    public void evaluate_number_to_text() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("43 asText"), is(equalTo(new Text("43"))));
    }

    @Test
    public void evaluate_object_creation_and_calls() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\") with(bar: 55) foo"), is(equalTo(new Text("Hello world"))));
    }

    @Test
    public void evaluate_object_creation_and_calls2() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\", bar: 126) foo"), is(equalTo(new Text("Hello world"))));
    }

    @Test
    public void evaluate_object_creation_and_calls3() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\", bar: 126) bar"), is(equalTo(new IntNum(126))));
    }

    @Test
    public void evaluate_object_creation_and_calls4() throws Exception, ControlFlow {
        assertThat((Text)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\", bar: 126) with(bar: 55) foo"), is(equalTo(new Text("Hello world"))));
    }

    @Test
    public void evaluate_object_creation_and_calls5() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\", bar: 126) with(bar: 55) bar"), is(equalTo(new IntNum(55))));
    }

    @Test
    public void evaluate_object_creation_and_calls6() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("Something with(foo: \"Hello world\") with(bar: 55) bar"), is(equalTo(new IntNum(55))));
    }

    @Test
    public void evaluate_simple_arithmetic() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("42 +(5)"), is(equalTo(new IntNum(47))));
    }

    @Test
    public void evaluate_simple_arithmetic_with_large_numbers() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("53476375647456756546754675467246645 +(53476375647456756546754675467246645)"), is(equalTo(IntNum.valueOf("106952751294913513093509350934493290"))));
    }

    @Test
    public void evaluate_simple_arithmetic_in_parenthesis() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("(42 +(555))"), is(equalTo(new IntNum(597))));
    }

    @Test
    public void evaluate_simple_arithmetic_with_shuffled_syntax() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("(42 + 5)"), is(equalTo(new IntNum(47))));
    }

// x = 42 + 5
// x println

// y = x + x
// y println

// foo = #("Hello World" println)
// foo

// barius = Something with(
//   bax: y + y + y, 
//   mux: #(bax println))

// barius mux
// barius mux
// barius mux
// barius mux

// barius2 = barius with(bax: 60)
// barius mux
// barius2 mux



// fux = #(
//   inside = "my very special value"
//   #(inside println))

// faxo = fux
// faxo
// faxo
// faxo

// accumulator = #(
//   n = 0
//   #(
//     y = n
//     n = n + 1
//     y
//   )
// )

// f1 = accumulator
// f2 = accumulator

// f1 println
// f1 println
// f1 println

// f2 println
// f2 println

// argus = #(x, x println)
// argus(42)
// argus(55)

// acc = #(n, #(x, n += x))
// f3 = acc(10)
// f4 = acc(25)

// f3(2) println
// f3(1) println
// f3(1) println
// f4(1) println
// f3(1) println

// f4(1) println


}// RuntimeTest
