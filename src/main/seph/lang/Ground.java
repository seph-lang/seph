/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents="SephGround")
public class Ground implements SephObject {
    public final static Ground instance = new Ground();

    @SephCell
    public final static SephObject Something = null;

    @SephCell
    public final static SephObject Ground = null;

    @SephCell
    public final static SephObject SephGround = null;

    @SephCell
    public final static SephObject Base = null;

    @SephCell
    public final static SephObject DefaultBehavior = null;

    @SephCell
    public final static SephObject IODefaultBehavior = null;

    @SephCell
    public final static SephObject ControlDefaultBehavior = null;

    public SephObject get(String cellName) {
        return seph.lang.bim.GroundBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// Ground
