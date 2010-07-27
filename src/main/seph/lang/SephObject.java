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
    SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments);
    boolean isTrue();
}// SephObject
