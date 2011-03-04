/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.structure.SephObjectFactory;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;

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

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    @SephMethod
    public final static SephObject with(SephObject receiver, IPersistentMap restKeywords) {
        if(restKeywords.count() == 0) {
            return receiver;
        } else {
            return SephObjectFactory.spreadAndCreate(null, receiver, restKeywords);
        }
    }

    @SephMethod(name="==")
    public final static SephObject eq(SephObject receiver, SephObject other) {
        if(receiver.equals(other)) {
            return Runtime.TRUE;
        } else {
            return Runtime.FALSE;
        }
    }
}// Base
