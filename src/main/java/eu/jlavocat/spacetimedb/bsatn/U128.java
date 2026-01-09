package eu.jlavocat.spacetimedb.bsatn;

import java.math.BigInteger;

import eu.jlavocat.spacetimedb.utils.BigIntegerUtils;

public record U128(long upper, long lower) {

    public BigInteger toBigInteger() {
        byte[] be = new byte[16];
        BigIntegerUtils.putLongBE(be, 0, upper);
        BigIntegerUtils.putLongBE(be, 8, lower);
        return new BigInteger(1, be);
    }

    public byte[] toBytesLE() {
        byte[] le = new byte[16];
        BigIntegerUtils.putLongLE(le, 0, lower);
        BigIntegerUtils.putLongLE(le, 8, upper);
        return le;
    }

    @Override
    public String toString() {
        return toBigInteger().toString();
    }
}
