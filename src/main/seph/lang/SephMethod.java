/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public @interface SephMethod {
    String[] name() default {};
    boolean evaluateArguments() default true;
}// SephMethod
