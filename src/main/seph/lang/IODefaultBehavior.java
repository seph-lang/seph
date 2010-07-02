/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class IODefaultBehavior implements SephObject {
    public final static IODefaultBehavior instance = new IODefaultBehavior();

    @SephMethod
    public final static SephObject println() {
        System.err.println("Calling println, yay!");
        return null;
    }

    public SephObject get(String cellName) {
        return seph.lang.bim.IODefaultBehaviorBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public SephObject activateWith(SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// IODefaultBehavior
