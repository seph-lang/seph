/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.NamedMessage;
import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton
public class IODefaultBehavior implements SephObject {
    public final static IODefaultBehavior instance = new IODefaultBehavior();

    @SephMethod
    public final static SephObject println(SThread thread, SephObject receiver) {
        System.out.println(((Text)NamedMessage.create("asText", null, null, null, -1, -1).go(thread, LexicalScope.ROOT, receiver)).text());
        return Runtime.NIL;
    }

    public SephObject get(String cellName) {
        return seph.lang.bim.IODefaultBehaviorBase.get(cellName);
    }

    public boolean isActivatable() {
        return false;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject activateWith(SThread thread, LexicalScope scope, SephObject receiver, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// IODefaultBehavior
