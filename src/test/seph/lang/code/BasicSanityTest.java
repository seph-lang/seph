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

    @Test
    public void assign_variable_to_value() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("x = 42 + 5\nx"), is(equalTo(new IntNum(47))));
    }

    @Test
    public void assign_several_variables_and_use_them() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString("x = 42 + 5\ny = x + x\ny"), is(equalTo(new IntNum(94))));
    }

    private PrintStream oldout;
    private ByteArrayOutputStream baos;

    private void captureStandardOut() throws IOException {
        oldout = System.out;
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
    }

    private String restoreStandardOut() throws IOException {
        System.setOut(oldout);
        return baos.toString("UTF-8");
    }

    @Test
    public void printing_hello_world() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("\"hello world\" println");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("hello world\n")));
    }

    @Test
    public void printing_another_hello_world() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("\"hello again, world\" println");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("hello again, world\n")));
    }

    @Test
    public void printing_with_custom_asText() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("Ground Something with(asText: \"this is cool\") println");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("this is cool\n")));
    }

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
