/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephSingleton(parents={"IODefaultBehavior", "ControlDefaultBehavior"})
public class DefaultBehavior implements SephObject {
    public final static DefaultBehavior instance = new DefaultBehavior();

    public final SephObject parent1 = IODefaultBehavior.instance;
    public final SephObject parent2 = ControlDefaultBehavior.instance;


    public SephObject get(String cellName) {
        return seph.lang.bim.DefaultBehaviorBase.get(cellName);
    }

    public Object identity() {
        return seph.lang.bim.DefaultBehaviorBase.IDENTITY;
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

    @SephMethod(name="")
    public final static SephObject blank(SephObject receiver, SephObject arg) {
        return arg;
    }
}// DefaultBehavior
