/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public @interface SephSingleton {
    String parent() default "";
    String parent2() default "";
}// SephSingleton
