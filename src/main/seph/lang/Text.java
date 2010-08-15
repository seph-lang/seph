/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.persistent.IPersistentList;

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



    public SephObject get(String cellName) {
        return seph.lang.bim.TextBase.get(cellName);
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

    @SephMethod
    public final static SephObject asText(SephObject receiver) {
        return receiver;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof Text && ((Text)other).text.equals(this.text));
    }
}// Text
