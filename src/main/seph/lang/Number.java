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
    @SephMethod(name="+")
    public final static SephObject plus(SephObject receiver, SephObject addend) {
        //        new Exception().printStackTrace();
        return ((Numeric)receiver).add(addend);
    }

    @SephMethod(name="-")
    public final static SephObject minus(SephObject receiver, SephObject subtrahend) {
               // new Exception().printStackTrace();
        return ((Numeric)receiver).sub(subtrahend);
    }

    @SephMethod(name="*")
    public final static SephObject times(SephObject receiver, SephObject multiplicand) {
        return ((Numeric)receiver).mul(multiplicand);
    }

    @SephMethod(name="<")
    public final static SephObject lt(SephObject receiver, SephObject other) {
        switch(((Numeric)receiver).compare(other)) {
        case -3:
        case -2:
            return Runtime.NIL;
        case -1:
            return Runtime.TRUE;
        default:
            return Runtime.FALSE;
        }
    }

    @SephMethod(name=">")
    public final static SephObject gt(SephObject receiver, SephObject other) {
        int res = ((Numeric)receiver).compare(other);
        switch(res) {
        case -3:
        case -2:
            return Runtime.NIL;
        case -1:
        case 0:
            return Runtime.FALSE;
        default:
            return Runtime.TRUE;
        }
    }
}// Number
