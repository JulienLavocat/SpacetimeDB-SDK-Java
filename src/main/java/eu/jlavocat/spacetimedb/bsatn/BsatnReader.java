package eu.jlavocat.spacetimedb.bsatn;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public final class BsatnReader {

    private final ByteBuffer buf;

    public BsatnReader(ByteBuffer buf) {
        this.buf = buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int remaining() {
        return buf.remaining();
    }

    public int position() {
        return buf.position();
    }

    public void position(int newPos) {
        buf.position(newPos);
    }

    public void skip(int n) {
        ensureRemaining(n);
        buf.position(buf.position() + n);
    }

    public byte[] readBytes(int n) {
        ensureRemaining(n);
        byte[] out = new byte[n];
        buf.get(out);
        return out;
    }

    private void ensureRemaining(int n) {
        if (buf.remaining() < n) {
            throw new IllegalStateException(
                    "BSATN decode error: need " + n + " bytes but only " + buf.remaining() + " remaining");
        }
    }

    public boolean readBool() {
        ensureRemaining(1);
        int b = Byte.toUnsignedInt(buf.get());
        if (b == 0)
            return false;
        if (b == 1)
            return true;
        throw new IllegalStateException("BSATN bool must be 0 or 1, got: " + b);
    }

    public byte readByte() {
        ensureRemaining(1);
        return buf.get();
    }

    public int readU8() {
        ensureRemaining(1);
        return Byte.toUnsignedInt(buf.get());
    }

    public byte readI8() {
        ensureRemaining(1);
        return buf.get();
    }

    public int readU16() {
        ensureRemaining(2);
        return Short.toUnsignedInt(buf.getShort());
    }

    public short readI16() {
        ensureRemaining(2);
        return buf.getShort();
    }

    public long readU32() {
        ensureRemaining(4);
        return Integer.toUnsignedLong(buf.getInt());
    }

    public int readI32() {
        ensureRemaining(4);
        return buf.getInt();
    }

    public long readU64() {
        ensureRemaining(8);
        return buf.getLong();
    }

    public long readI64() {
        ensureRemaining(8);
        return buf.getLong();
    }

    public float readF32() {
        int raw = (int) readU32();
        return Float.intBitsToFloat(raw);
    }

    public double readF64() {
        long raw = readU64();
        return Double.longBitsToDouble(raw);
    }

    public String readString() {
        long lenU32 = readU32();
        if (lenU32 > Integer.MAX_VALUE) {
            throw new IllegalStateException("BSATN string length too large: " + lenU32);
        }
        int len = (int) lenU32;
        byte[] bytes = readBytes(len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public int readArrayLength() {
        long lenU32 = readU32();
        if (lenU32 > Integer.MAX_VALUE) {
            throw new IllegalStateException("BSATN array length too large: " + lenU32);
        }
        return (int) lenU32;
    }

    public U128 readU128() {
        long lower = readU64();
        long upper = readU64();
        return new U128(upper, lower);
    }

    public I128 readI128() {
        long lo = readU64();
        long hi = readU64();
        return new I128(hi, lo);
    }

    public I256 readI256() {
        long w0 = readU64();
        long w1 = readU64();
        long w2 = readU64();
        long w3 = readU64();
        return new I256(w3, w2, w1, w0);
    }

    public U256 readU256() {
        long w0 = readU64();
        long w1 = readU64();
        long w2 = readU64();
        long w3 = readU64();
        return new U256(w3, w2, w1, w0);
    }

}
