/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import gnu.math.Numeric;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
@SephKind(parents="Something")
public class Number {
    @SephMethod
    public final static SephObject asText(SephObject receiver) {
        return new Text(((Numeric)receiver).toString());
    }
}// Number
