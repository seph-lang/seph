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
        return new Text(new StringUtils().replaceEscapes(stringBeforeEscapeMangling));
    }

    public Text newUnescapedText(String text) {
        return new Text(text);
    }

    public Regexp newRegexp(String pattern, String flags) {
        return Regexp.create(new StringUtils().replaceRegexpEscapes(pattern), flags);
    }
}// Runtime
