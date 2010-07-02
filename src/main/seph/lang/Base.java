/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class Base implements SephObject {
    public final static Base instance = new Base();


    public SephObject get(String cellName) {
        return seph.lang.bim.BaseBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public SephObject activateWith(SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// Base
