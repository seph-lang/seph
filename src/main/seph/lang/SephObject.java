/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface SephObject {
    SephObject get(String cellName);
    boolean isActivatable();
    SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments);
    boolean isTrue();
}// SephObject
