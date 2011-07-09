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

import java.lang.invoke.SwitchPoint;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Buster {
    @After
    public void resetCompilerSettings() {
        seph.lang.compiler.AbstractionCompiler.PRINT_COMPILE = false;
    }

    @Test
    public void buster() throws Throwable {
        seph.lang.compiler.AbstractionCompiler.PRINT_COMPILE = true;
        seph.lang.Runtime runtime = new seph.lang.Runtime();
        SephObject result = (SephObject)runtime.evaluateString("#(1 + 1)");
        java.lang.invoke.MethodHandle mh = result.activationFor(0, false);
        SThread thread = new SThread(runtime);
        for(int i = 0; i<20000; i++) {
            mh.invokeExact((SephObject)Ground.instance, thread, (LexicalScope)null);
        }
    }
}
