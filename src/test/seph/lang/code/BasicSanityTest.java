/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.code;

import java.io.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import seph.lang.*;
import gnu.math.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class BasicSanityTest {
    @After
    public void resetCompilerSettings() {
        seph.lang.compiler.AbstractionCompiler.DO_COMPILE = true;
        seph.lang.compiler.AbstractionCompiler.PRINT_COMPILE = false;
    }

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

    @Test
    public void printing_from_abstraction() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("foo = #(\"Hello World\" println)\nfoo");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("Hello World\n")));
    }

    @Test
    public void printing_more_complex_things() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("x = 42 + 5\ny = x + x\nbarius = Something with(\n  bax: y + y + y, \n  mux: #(bax println))\nbarius mux\nbarius mux\nbarius mux\nbarius mux\nbarius2 = barius with(bax: 60)\nbarius mux\nbarius2 mux");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("282\n282\n282\n282\n282\n60\n")));
    }

    @Test
    public void printing_a_closed_over_value_in_closure() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("fux = #(\n  inside = \"my very special value\"\n  #(inside println))\nfaxo = fux\nfaxo\nfaxo\nfaxo\n");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("my very special value\nmy very special value\nmy very special value\n")));
    }

    @Test
    public void printing_accumulator_values() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("accumulator = #(\n  n = 0\n  #(\n    y = n\n    n = n + 1\n    y\n  )\n)\nf1 = accumulator\nf2 = accumulator\nf1 println\nf1 println\nf1 println\nf2 println\nf2 println");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("0\n1\n2\n0\n1\n")));
    }

    @Test
    public void simple_argument_printing() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("argus = #(x, x println)\nargus(42)\nargus(55)");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("42\n55\n")));
    }

    @Test
    public void accumulator_with_arg_printing() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("acc = #(n, #(x, n += x))\nf3 = acc(10)\nf4 = acc(25)\nf3(2) println\nf3(1) println\nf3(1) println\nf4(1) println\nf3(1) println\nf4(1) println");
        String stdout = restoreStandardOut();
        assertThat(result, is(equalTo((Object)seph.lang.Runtime.NIL)));
        assertThat(stdout, is(equalTo("12\n13\n14\n26\n15\n27\n")));
    }

    @Test
    public void big_accumulator() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(n,\n" +
                                                                  "  facc = #(acc, n,\n" +
                                                                  "    if(n == 0,\n" +
                                                                  "      acc,\n" +
                                                                  "      facc(n * acc, n - 1)))\n" +
                                                                  "  facc(1, n))\n" +
                                                                  "\n" +
                                                                  "f(421)\n"), is(equalTo(IntNum.valueOf("496709438418428047101555454724916745085465336888090097405467818180922261398842295425806822589714833795213495222240007671066490851554749892134937936656282994637909051947398340526149934808106330096692953599944767716258342891428727319048480424750506254845534889774433360425470710774683290442555783815659955875264427528929575675931563437214692758482302503124210528001023359804355899661904496934663897310150700949162282909151225143677669059060838583122439585977518160297232827264016830876225940821782638126030221442411154489855932639983840488927885300335833913419474049335611983897922046477193331977853760794988593293683296748209719217136728920867862297531494704044484830900817732903201945049906982980655481012268737891556187874734833673235166827039350174863501581347262465477415340389373595437779322143694446268466299687403520000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"))));
    }

    @Test
    public void recursive_odd_and_even_that_should_blow_the_stack() throws Exception, ControlFlow {
        captureStandardOut();
        Object result = new seph.lang.Runtime().evaluateString("odd? = #(n,\n" +
                                                               "  if(n == 0,\n" +
                                                               "    false,\n" +
                                                               "    even?(n - 1)))\n" +
                                                               "\n" +
                                                               "even? = #(n,\n" +
                                                               "  if(n == 0,\n" +
                                                               "    true,\n" +
                                                               "    odd?(n - 1)))\n" +
                                                               "\n" +
                                                               "odd?(10) println\n" +
                                                               "even?(10) println\n" +
                                                               "\n" +
                                                                   "odd?(1010) println\n" +
                                                               "even?(1010) println\n" +
                                                                   "\n" +
                                                               "odd?(1011) println\n" +
                                                               "even?(1011) println\n" +
                                                               "\n" +
                                                               "odd?(101010) println\n" +
                                                               "even?(101010) println\n" +
                                                               "\n" +
                                                               "odd?(101011) println\n" +
                                                               "even?(101011) println\n");
        String stdout = restoreStandardOut();
        assertThat(stdout, is(equalTo("false\ntrue\nfalse\ntrue\ntrue\nfalse\nfalse\ntrue\ntrue\nfalse\n")));
    }

    @Test
    public void small_fib() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "fib = #(n,\n" +
                                                                  "  if(n < 2,\n" +
                                                                  "    n,\n" +
                                                                  "    fib(n - 1) + fib(n - 2)))\n" +
                                                                  "\n" +
                                                                  "fib(10)\n"), is(equalTo(IntNum.valueOf("55"))));
    }

    @Test(expected=RuntimeException.class)
    public void should_not_lookup_on_local_scope_when_explicit_receiver_is_used_in_interpreter() throws Exception, ControlFlow {
        seph.lang.compiler.AbstractionCompiler.DO_COMPILE = false;
        new seph.lang.Runtime().evaluateString(
                                               "f = #(\n" +
                                               "  x = 42\n" +
                                               "  blargiman = 15\n" +
                                               "  #(x blargiman))\n" +
                                               "fp = f\n" +
                                               "fp\n");
    }

    @Test(expected=RuntimeException.class)
    public void should_not_lookup_on_local_scope_when_explicit_receiver_is_used_in_compiler() throws Exception, ControlFlow {
        new seph.lang.Runtime().evaluateString(
                                               "f = #(\n" +
                                               "  x = 42\n" +
                                               "  blargiman = 15\n" +
                                               "  #(x blargiman))\n" +
                                               "fp = f\n" +
                                               "fp\n");
    }

    @Test
    public void evaluate_simple_if_statement_in_interpreter() throws Exception, ControlFlow {
        seph.lang.compiler.AbstractionCompiler.DO_COMPILE = false;
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(false, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("55"))));

        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(true, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("42"))));
    }

    @Test
    public void evaluate_simple_if_statement_in_compiler() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(false, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("55"))));

        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(true, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("42"))));
    }


    @Test
    public void evaluate_if_statement_with_expression_in_conditional_in_interpreter() throws Exception, ControlFlow {
        seph.lang.compiler.AbstractionCompiler.DO_COMPILE = false;
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(1 == 0, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("55"))));

        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(1 == 1, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("42"))));
    }

    @Test
    public void evaluate_if_statement_with_expression_in_conditional_in_compiler() throws Exception, ControlFlow {
        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(1 == 0, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("55"))));

        assertThat((IntNum)new seph.lang.Runtime().evaluateString(
                                                                  "f = #(if(1 == 1, 42, 55))\n" +
                                                                  "f\n"), is(equalTo(IntNum.valueOf("42"))));
    }

    @Test
    public void arguments_to_other_methods_in_interpreter() throws Exception, ControlFlow {
        seph.lang.compiler.AbstractionCompiler.DO_COMPILE = false;
        captureStandardOut();
        new seph.lang.Runtime().evaluateString("f = #(x, y, x asText println\ny asText println)\nf(42, 55)\nff = #(f(42, 55))\nff\n");
        String stdout = restoreStandardOut();
        assertThat(stdout, is(equalTo("42\n55\n42\n55\n")));
    }

    @Test
    public void arguments_to_other_methods_in_compiler() throws Exception, ControlFlow {
        captureStandardOut();
        new seph.lang.Runtime().evaluateString("f = #(x, y, x asText println\ny asText println)\nf(42, 55)\nff = #(f(42, 55))\nff\n");
        String stdout = restoreStandardOut();
        assertThat(stdout, is(equalTo("42\n55\n42\n55\n")));
    }
}// RuntimeTest

