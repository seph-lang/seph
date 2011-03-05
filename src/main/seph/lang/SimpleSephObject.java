/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;

import java.dyn.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SimpleSephObject implements SephObject, TailCallable {
    final IPersistentMap meta;
    final IPersistentMap cells;

    public SimpleSephObject(IPersistentMap meta, IPersistentMap cells) {
        this.meta = meta;
        this.cells = cells;
    }

    public SimpleSephObject(IPersistentMap meta) {
        this(meta, PersistentArrayMap.EMPTY);
    }
    
    public SimpleSephObject() {
        this(PersistentArrayMap.EMPTY, PersistentArrayMap.EMPTY);
    }

    final public IPersistentMap meta(){
        return meta;
    }

    public SephObject get(String cellName) {
        return (SephObject)cells.valueAt(cellName);
    }

    public final static Symbol activatable = new Symbol();
    
    public boolean isActivatable() {
        return meta.valueAt(activatable) == Runtime.TRUE;
    }

    public boolean isTrue() {
        return true;
    }

    public SephObject goTail(SThread thread) {
        SephObject receiver = thread.nextReceiver;
        LexicalScope scope = thread.nextScope;

        if(thread.nextArity > -1) {
            int arity = thread.nextArity;
            MethodHandle arg0 = thread.arg0;
            MethodHandle arg1 = thread.arg1;
            MethodHandle arg2 = thread.arg2;
            MethodHandle arg3 = thread.arg3;
            MethodHandle arg4 = thread.arg4;
            thread.clean();
            switch(arity) {
            case 0:
                return activateWith(receiver, thread, scope);
            case 1: 
                return activateWith(receiver, thread, scope, arg0);
            case 2:
                return activateWith(receiver, thread, scope, arg0, arg1);
            case 3:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2);
            case 4:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3);
            case 5:
                return activateWith(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
            default:
                // Can't happen
                throw new RuntimeException("Should never happen");
            }
        } else {
            IPersistentList arguments = thread.arguments;
            thread.clean();
            return activateWith(receiver, thread, scope, arguments);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, SephObject arg4) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }
}// SimpleSephObject
