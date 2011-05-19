/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.lang.invoke.MethodHandle;

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
    private final MethodHandle handle_k;
    private final MethodHandle handle0_k;
    private final MethodHandle handle1s_k;
    private final MethodHandle handle2s_k;
    private final MethodHandle handle3s_k;
    private final MethodHandle handle4s_k;
    private final MethodHandle handle5s_k;
    private final MethodHandle handle1m_k;
    private final MethodHandle handle2m_k;
    private final MethodHandle handle3m_k;
    private final MethodHandle handle4m_k;
    private final MethodHandle handle5m_k;

    public SephMethodHandleObject(MethodHandle handle, MethodHandle handle0, 
                                  MethodHandle handle1s, MethodHandle handle2s, MethodHandle handle3s, MethodHandle handle4s, MethodHandle handle5s, 
                                  MethodHandle handle1m, MethodHandle handle2m, MethodHandle handle3m, MethodHandle handle4m, MethodHandle handle5m,
                                  MethodHandle handle_k, MethodHandle handle0_k, 
                                  MethodHandle handle1s_k, MethodHandle handle2s_k, MethodHandle handle3s_k, MethodHandle handle4s_k, MethodHandle handle5s_k, 
                                  MethodHandle handle1m_k, MethodHandle handle2m_k, MethodHandle handle3m_k, MethodHandle handle4m_k, MethodHandle handle5m_k) {
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
        this.handle_k = handle_k;
        this.handle0_k = handle0_k;
        this.handle1s_k = handle1s_k;
        this.handle2s_k = handle2s_k;
        this.handle3s_k = handle3s_k;
        this.handle4s_k = handle4s_k;
        this.handle5s_k = handle5s_k;
        this.handle1m_k = handle1m_k;
        this.handle2m_k = handle2m_k;
        this.handle3m_k = handle3m_k;
        this.handle4m_k = handle4m_k;
        this.handle5m_k = handle5m_k;
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        try {
            return (SephObject)this.handle.invoke(thread, scope, receiver, arguments);
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
            return (SephObject)this.handle0.invoke(thread, scope, receiver);
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
            return (SephObject)this.handle1s.invoke(thread, scope, receiver, arg0);
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
            return (SephObject)this.handle2s.invoke(thread, scope, receiver, arg0, arg1);
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
            return (SephObject)this.handle3s.invoke(thread, scope, receiver, arg0, arg1, arg2);
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
            return (SephObject)this.handle4s.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3);
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
            return (SephObject)this.handle5s.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4);
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
            return (SephObject)this.handle1m.invoke(thread, scope, receiver, arg0);
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
            return (SephObject)this.handle2m.invoke(thread, scope, receiver, arg0, arg1);
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
            return (SephObject)this.handle3m.invoke(thread, scope, receiver, arg0, arg1, arg2);
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
            return (SephObject)this.handle4m.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3);
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
            return (SephObject)this.handle5m.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }




    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_k.invoke(thread, scope, receiver, arguments, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle0_k.invoke(thread, scope, receiver, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle1s_k.invoke(thread, scope, receiver, arg0, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle2s_k.invoke(thread, scope, receiver, arg0, arg1, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle3s_k.invoke(thread, scope, receiver, arg0, arg1, arg2, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle4s_k.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, SephObject arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle5s_k.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle1m_k.invoke(thread, scope, receiver, arg0, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle2m_k.invoke(thread, scope, receiver, arg0, arg1, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle3m_k.invoke(thread, scope, receiver, arg0, arg1, arg2, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle4m_k.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle5m_k.invoke(thread, scope, receiver, arg0, arg1, arg2, arg3, arg4, keywordNames, keywordArguments);
        } catch(Error e) {
            throw e;
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

}// SephMethodHandleObject

