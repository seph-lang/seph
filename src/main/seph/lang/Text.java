/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

import java.lang.invoke.MethodHandle;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephKind(parents="Something")
public class Text implements SephObject {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }

    public String toString() {
        return "\"" + text + "\"";
    }



    @Override
    public SephObject get(String cellName) {
        return seph.lang.bim.TextBase.get(cellName);
    }

    @Override
    public Object identity() {
        return seph.lang.bim.TextBase.IDENTITY;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public MethodHandle activationFor(int arity, boolean keywords) {
        return ActivationHelpers.noActivateFor(this, arity, keywords);
    }

    @SephMethod
    public final static SephObject asText(SephObject receiver) {
        return receiver;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof Text && ((Text)other).text.equals(this.text));
    }
}// Text
