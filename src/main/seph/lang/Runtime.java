/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import seph.lang.parser.StringUtils;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Runtime {
    public Text newText(String stringBeforeEscapeMangling) {
        return new Text(new StringUtils().replaceEscapes(stringBeforeEscapeMangling.toString()));
    }
}// Runtime
