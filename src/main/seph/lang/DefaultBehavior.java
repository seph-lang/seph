/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents="IODefaultBehavior")
public class DefaultBehavior implements SephObject {
    public final static DefaultBehavior instance = new DefaultBehavior();

    public final SephObject parent1 = IODefaultBehavior.instance;


    public SephObject get(String cellName) {
        return seph.lang.bim.DefaultBehaviorBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public SephObject activateWith(SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// DefaultBehavior
