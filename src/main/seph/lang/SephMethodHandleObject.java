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
    private final MethodHandle handle0;
    private final MethodHandle handle1s;
    private final MethodHandle handle2s;
    private final MethodHandle handle3s;
    private final MethodHandle handle4s;
    private final MethodHandle handle5s;
    private final MethodHandle handle1m;
    private final MethodHandle handle2m;
    private final MethodHandle handle3m;
    private final MethodHandle handle4m;
    private final MethodHandle handle5m;

    public SephMethodHandleObject(MethodHandle handle, MethodHandle handle0, 
                                  MethodHandle handle1s, MethodHandle handle2s, MethodHandle handle3s, MethodHandle handle4s, MethodHandle handle5s, 
                                  MethodHandle handle1m, MethodHandle handle2m, MethodHandle handle3m, MethodHandle handle4m, MethodHandle handle5m) {
        this.handle = handle;
        this.handle0 = handle0;
        this.handle1s = handle1s;
        this.handle2s = handle2s;
        this.handle3s = handle3s;
        this.handle4s = handle4s;
        this.handle5s = handle5s;
        this.handle1m = handle1m;
        this.handle2m = handle2m;
        this.handle3m = handle3m;
        this.handle4m = handle4m;
        this.handle5m = handle5m;
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

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope) {
        try {
            return (SephObject)this.handle0.invokeExact(thread, scope, receiver);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0) {
        try {
            return (SephObject)this.handle1s.invokeExact(thread, scope, receiver, arg0);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1) {
        try {
            return (SephObject)this.handle2s.invokeExact(thread, scope, receiver, arg0, arg1);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2) {
        try {
            return (SephObject)this.handle3s.invokeExact(thread, scope, receiver, arg0, arg1, arg2);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3) {
        try {
            return (SephObject)this.handle4s.invokeExact(thread, scope, receiver, arg0, arg1, arg2, arg3);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, SephObject arg4) {
        try {
            return (SephObject)this.handle5s.invokeExact(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        try {
            return (SephObject)this.handle1m.invokeExact(thread, scope, receiver, arg0);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        try {
            return (SephObject)this.handle2m.invokeExact(thread, scope, receiver, arg0, arg1);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        try {
            return (SephObject)this.handle3m.invokeExact(thread, scope, receiver, arg0, arg1, arg2);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        try {
            return (SephObject)this.handle4m.invokeExact(thread, scope, receiver, arg0, arg1, arg2, arg3);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        try {
            return (SephObject)this.handle5m.invokeExact(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}// SephMethodHandleObject

