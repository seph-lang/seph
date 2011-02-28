/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class CompilationAborted extends RuntimeException {
    public CompilationAborted(String reason) {
        super(reason);
    }
}// CompilationAborted

