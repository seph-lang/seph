/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface SephObject {
    SephObject get(String cellName);
    boolean isActivatable();
    MethodHandle activationFor(int arity, boolean keywords);
    boolean isTrue();
    Object identity();
}// SephObject
