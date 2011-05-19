/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentMap;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class SimpleSephObject implements SephObject {
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

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, IPersistentList arguments, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, SephObject arg0, SephObject arg1, SephObject arg2, SephObject arg3, SephObject arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public SephObject activateWith(SephObject receiver, SThread thread, LexicalScope scope, MethodHandle arg0, MethodHandle arg1, MethodHandle arg2, MethodHandle arg3, MethodHandle arg4, String[] keywordNames, MethodHandle[] keywordArguments) {
        throw new RuntimeException(" *** couldn't activate: " + this);
    }

    public Object identity() {
        return this;
    }
}// SimpleSephObject
