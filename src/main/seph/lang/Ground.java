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

    @SephCell(name="true")
    public final static SephObject Runtime_true = null;

    @SephCell(name="false")
    public final static SephObject Runtime_false = null;

    @SephCell(name="nil")
    public final static SephObject Runtime_nil = null;

    @SephMethod
    public final static SephObject asText(SephObject receiver) {
        return new Text(receiver.toString());
    }

    public SephObject get(String cellName) {
        return seph.lang.bim.GroundBase.get(cellName);
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
}// Ground
