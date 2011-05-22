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
    private final MethodHandle handle_n;
    private final MethodHandle handle_0;
    private final MethodHandle handle_1;
    private final MethodHandle handle_2;
    private final MethodHandle handle_3;
    private final MethodHandle handle_4;
    private final MethodHandle handle_5;
    private final MethodHandle handle_n_k;
    private final MethodHandle handle_0_k;
    private final MethodHandle handle_1_k;
    private final MethodHandle handle_2_k;
    private final MethodHandle handle_3_k;
    private final MethodHandle handle_4_k;
    private final MethodHandle handle_5_k;

    public SephMethodHandleObject(MethodHandle handle_n, MethodHandle handle_0, 
                                  MethodHandle handle_1, MethodHandle handle_2, MethodHandle handle_3, MethodHandle handle_4, MethodHandle handle_5,
                                  MethodHandle handle_n_k, MethodHandle handle_0_k,
                                  MethodHandle handle_1_k, MethodHandle handle_2_k, MethodHandle handle_3_k, MethodHandle handle_4_k, MethodHandle handle_5_k) {
        this.handle_n = handle_n;
        this.handle_0 = handle_0;
        this.handle_1 = handle_1;
        this.handle_2 = handle_2;
        this.handle_3 = handle_3;
        this.handle_4 = handle_4;
        this.handle_5 = handle_5;

        this.handle_n_k = handle_n_k;
        this.handle_0_k = handle_0_k;
        this.handle_1_k = handle_1_k;
        this.handle_2_k = handle_2_k;
        this.handle_3_k = handle_3_k;
        this.handle_4_k = handle_4_k;
        this.handle_5_k = handle_5_k;
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments) {
        try {
            return (SephObject)this.handle_n.invoke(receiver, thread, scope, arguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope) {
        try {
            return (SephObject)this.handle_0.invoke(receiver, thread, scope);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0) {
        try {
            return (SephObject)this.handle_1.invoke(receiver, thread, scope, arg0);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1) {
        try {
            return (SephObject)this.handle_2.invoke(receiver, thread, scope, arg0, arg1);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2) {
        try {
            return (SephObject)this.handle_3.invoke(receiver, thread, scope, arg0, arg1, arg2);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3) {
        try {
            return (SephObject)this.handle_4.invoke(receiver, thread, scope, arg0, arg1, arg2, arg3);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4) {
        try {
            return (SephObject)this.handle_5.invoke(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_n_k.invoke(receiver, thread, scope, arguments, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_0_k.invoke(receiver, thread, scope, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_1_k.invoke(receiver, thread, scope, arg0, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_2_k.invoke(receiver, thread, scope, arg0, arg1, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_3_k.invoke(receiver, thread, scope, arg0, arg1, arg2, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_4_k.invoke(receiver, thread, scope, arg0, arg1, arg2, arg3, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        try {
            return (SephObject)this.handle_5_k.invoke(receiver, thread, scope, arg0, arg1, arg2, arg3, arg4, keywordNames, keywordArguments);
        } catch(Error | RuntimeException e) {
            throw e;
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

}// SephMethodHandleObject

