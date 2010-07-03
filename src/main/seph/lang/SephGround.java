/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents={"Base", "DefaultBehavior"})
public class SephGround implements SephObject {
    public final static SephGround instance = new SephGround();

    public final SephObject parent1 = Base.instance;
    public final SephObject parent2 = DefaultBehavior.instance;


    public SephObject get(String cellName) {
        return seph.lang.bim.SephGroundBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// SephGround
