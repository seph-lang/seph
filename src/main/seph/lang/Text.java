/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Text implements SephObject {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}// Text
