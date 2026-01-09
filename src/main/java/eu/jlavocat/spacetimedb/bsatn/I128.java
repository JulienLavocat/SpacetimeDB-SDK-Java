package eu.jlavocat.spacetimedb.bsatn;

import java.math.BigInteger;

import eu.jlavocat.spacetimedb.utils.BigIntegerUtils;

public final record I128(long hi, long lo) {

    public BigInteger toBigInteger() {
        byte[] be = new byte[16];
        BigIntegerUtils.putLongBE(be, 0, hi);
        BigIntegerUtils.putLongBE(be, 8, lo);
        return new BigInteger(be);
    }

    public byte[] toBytesLE() {
        byte[] le = new byte[16];
        BigIntegerUtils.putLongLE(le, 0, lo);
        BigIntegerUtils.putLongLE(le, 8, hi);
        return le;
    }

    @Override
    public String toString() {
        return toBigInteger().toString();
    }

}
