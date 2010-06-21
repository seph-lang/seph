package seph.lang.persistent;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Based on persistent collections in Clojure - see LICENSE.clojure for copyright and licensing information
 */
public class Numbers{
    static interface Ops{
        Ops combine(Ops y);
        Ops opsWith(IntegerOps x);
        Ops opsWith(LongOps x);
        Ops opsWith(FloatOps x);
        Ops opsWith(DoubleOps x);
        Ops opsWith(BigIntegerOps x);
        Ops opsWith(BigDecimalOps x);
        public boolean lt(Number x, Number y);
    }

    static public int compare(Number x, Number y){
        Ops ops = ops(x).combine(ops(y));
        if(ops.lt(x, y))
            return -1;
        else if(ops.lt(y, x))
            return 1;
        return 0;
    }

    static BigInteger toBigInteger(Object x){
        if(x instanceof BigInteger)
            return (BigInteger) x;
        else
            return BigInteger.valueOf(((Number) x).longValue());
    }

    static BigDecimal toBigDecimal(Object x){
        if(x instanceof BigDecimal)
            return (BigDecimal) x;
        else if(x instanceof BigInteger)
            return new BigDecimal((BigInteger) x);
        else
            return BigDecimal.valueOf(((Number) x).longValue());
    }

    final static class IntegerOps implements Ops {
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return LONG_OPS;
        }

        final public Ops opsWith(FloatOps x){
            return FLOAT_OPS;
        }

        final public Ops opsWith(DoubleOps x){
            return DOUBLE_OPS;
        }

        final public Ops opsWith(BigIntegerOps x){
            return BIGINTEGER_OPS;
        }

        final public Ops opsWith(BigDecimalOps x){
            return BIGDECIMAL_OPS;
        }

        public boolean lt(Number x, Number y){
            return x.intValue() < y.intValue();
        }
    }

    final static class LongOps implements Ops{
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return this;
        }

        final public Ops opsWith(FloatOps x){
            return FLOAT_OPS;
        }

        final public Ops opsWith(DoubleOps x){
            return DOUBLE_OPS;
        }

        final public Ops opsWith(BigIntegerOps x){
            return BIGINTEGER_OPS;
        }

        final public Ops opsWith(BigDecimalOps x){
            return BIGDECIMAL_OPS;
        }
        public boolean lt(Number x, Number y){
            return x.longValue() < y.longValue();
        }
    }

    final static class FloatOps implements Ops{
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return this;
        }

        final public Ops opsWith(FloatOps x){
            return this;
        }

        final public Ops opsWith(DoubleOps x){
            return DOUBLE_OPS;
        }

        final public Ops opsWith(BigIntegerOps x){
            return this;
        }

        final public Ops opsWith(BigDecimalOps x){
            return this;
        }

        public boolean lt(Number x, Number y){
            return x.floatValue() < y.floatValue();
        }
    }

    final static class DoubleOps implements Ops{
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return this;
        }

        final public Ops opsWith(FloatOps x){
            return this;
        }

        final public Ops opsWith(DoubleOps x){
            return this;
        }

        final public Ops opsWith(BigIntegerOps x){
            return this;
        }

        final public Ops opsWith(BigDecimalOps x){
            return this;
        }

        public boolean lt(Number x, Number y){
            return x.doubleValue() < y.doubleValue();
        }
    }

    final static class BigIntegerOps implements Ops{
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return this;
        }

        final public Ops opsWith(FloatOps x){
            return FLOAT_OPS;
        }

        final public Ops opsWith(DoubleOps x){
            return DOUBLE_OPS;
        }

        final public Ops opsWith(BigIntegerOps x){
            return this;
        }

        final public Ops opsWith(BigDecimalOps x){
            return BIGDECIMAL_OPS;
        }

        public boolean lt(Number x, Number y){
            return toBigInteger(x).compareTo(toBigInteger(y)) < 0;
        }
    }

    final static class BigDecimalOps implements Ops{
        public Ops combine(Ops y){
            return y.opsWith(this);
        }

        final public Ops opsWith(IntegerOps x){
            return this;
        }

        final public Ops opsWith(LongOps x){
            return this;
        }

        final public Ops opsWith(FloatOps x){
            return FLOAT_OPS;
        }

        final public Ops opsWith(DoubleOps x){
            return DOUBLE_OPS;
        }

        final public Ops opsWith(BigIntegerOps x){
            return this;
        }

        final public Ops opsWith(BigDecimalOps x){
            return this;
        }

        public boolean lt(Number x, Number y){
            return toBigDecimal(x).compareTo(toBigDecimal(y)) < 0;
        }
    }

    static final IntegerOps INTEGER_OPS = new IntegerOps();
    static final LongOps LONG_OPS = new LongOps();
    static final FloatOps FLOAT_OPS = new FloatOps();
    static final DoubleOps DOUBLE_OPS = new DoubleOps();
    static final BigIntegerOps BIGINTEGER_OPS = new BigIntegerOps();
    static final BigDecimalOps BIGDECIMAL_OPS = new BigDecimalOps();

    static Ops ops(Object x){
        Class xc = x.getClass();

        if(xc == Integer.class)
            return INTEGER_OPS;
        else if(xc == Double.class)
            return DOUBLE_OPS;
        else if(xc == Float.class)
            return FLOAT_OPS;
        else if(xc == BigInteger.class)
            return BIGINTEGER_OPS;
        else if(xc == Long.class)
            return LONG_OPS;
        else if(xc == BigDecimal.class)
            return BIGDECIMAL_OPS;
        else
            return INTEGER_OPS;
    }
}
