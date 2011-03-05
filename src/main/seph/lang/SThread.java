/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.ast.Message;
import seph.lang.persistent.IPersistentList;

import java.dyn.MethodHandle;

/**
 * Represents a running thread of Seph computation. This
 * might not correspond to any real threads.
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SThread {
    public final static SephObject TAIL_MARKER = new SimpleSephObject() {
            public String toString() {
                return "TAIL_MARKER (should never escape, it's a bug if you see this)";
            }
        };

    public final Runtime runtime;

    public Message next;
    public boolean first;

    public TailCallable nextTail;
    public LexicalScope nextScope;
    public SephObject nextReceiver;

    public int nextArity = -1;
    public IPersistentList arguments;
    public MethodHandle arg0;
    public MethodHandle arg1;
    public MethodHandle arg2;
    public MethodHandle arg3;
    public MethodHandle arg4;

    public void clean() {
        this.nextArity = -1;
        this.arguments = null;
        this.arg0 = null;
        this.arg1 = null;
        this.arg2 = null;
        this.arg3 = null;
        this.arg4 = null;
        this.nextReceiver = null;
        this.nextScope = null;
        this.nextTail = null;
    }

    public SThread(Runtime runtime) {
        this.runtime = runtime;
    }
}// SThread
