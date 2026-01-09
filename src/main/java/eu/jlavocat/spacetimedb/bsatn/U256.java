package eu.jlavocat.spacetimedb.bsatn;

import java.math.BigInteger;

import eu.jlavocat.spacetimedb.utils.BigIntegerUtils;

// w3: most significant word
// w0: least significant word
public record U256(long w3, long w2, long w1, long w0) {

    public BigInteger toBigInteger() {
        byte[] be = new byte[32];
        BigIntegerUtils.putLongBE(be, 0, w3);
        BigIntegerUtils.putLongBE(be, 8, w2);
        BigIntegerUtils.putLongBE(be, 16, w1);
        BigIntegerUtils.putLongBE(be, 24, w0);
        return new BigInteger(1, be);
    }

    public byte[] toBytesLE() {
        byte[] le = new byte[32];
        BigIntegerUtils.putLongLE(le, 0, w0);
        BigIntegerUtils.putLongLE(le, 8, w1);
        BigIntegerUtils.putLongLE(le, 16, w2);
        BigIntegerUtils.putLongLE(le, 24, w3);
        return le;
    }

    public byte[] toBytesBE() {
        byte[] be = new byte[32];
        BigIntegerUtils.putLongBE(be, 0, w3);
        BigIntegerUtils.putLongBE(be, 8, w2);
        BigIntegerUtils.putLongBE(be, 16, w1);
        BigIntegerUtils.putLongBE(be, 24, w0);
        return be;
    }

    @Override
    public String toString() {
        return toBigInteger().toString();
    }
}
