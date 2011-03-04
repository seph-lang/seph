/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.dyn.MethodHandle;

import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SephMethodHandleObject extends SephMethodObject {
    private final MethodHandle handle;

    public SephMethodHandleObject(MethodHandle handle) {
        this.handle = handle;
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        try {
            return (SephObject)this.handle.invokeExact(thread, scope, receiver, arguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}// SephMethodHandleObject

