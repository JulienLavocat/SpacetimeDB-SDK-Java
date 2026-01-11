
package eu.jlavocat.spacetimedb.bsatn;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public final class BsatnWriter {

    private ByteBuffer buf;

    public BsatnWriter(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("initialCapacity < 0");
        this.buf = ByteBuffer.allocate(initialCapacity).order(ByteOrder.LITTLE_ENDIAN);
    }

    public BsatnWriter() {
        this(256);
    }

    public int position() {
        return buf.position();
    }

    public void position(int newPos) {
        if (newPos < 0 || newPos > buf.position()) {
            throw new IllegalArgumentException(
                    "newPos must be between 0 and current position (" + buf.position() + "), got " + newPos);
        }
        buf.position(newPos);
    }

    public int size() {
        return buf.position();
    }

    private void ensureCapacity(int additionalBytes) {
        if (additionalBytes < 0)
            throw new IllegalArgumentException("additionalBytes < 0");

        int required = buf.position() + additionalBytes;
        if (required <= buf.capacity())
            return;

        int newCap = buf.capacity();
        if (newCap == 0)
            newCap = 1;
        while (newCap < required) {
            // grow ~2x
            newCap = newCap * 2;
            if (newCap < 0) {
                // overflow
                throw new IllegalStateException("BSATN encode error: buffer too large");
            }
        }

        ByteBuffer newBuf = ByteBuffer.allocate(newCap).order(ByteOrder.LITTLE_ENDIAN);

        int oldPos = buf.position();
        buf.flip();
        newBuf.put(buf);
        newBuf.position(oldPos);

        this.buf = newBuf;
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer result = buf.duplicate();
        result.flip();
        return result;
    }

    public byte[] toByteArray() {
        byte[] result = new byte[buf.position()];
        ByteBuffer duplicate = buf.duplicate();
        duplicate.flip();
        duplicate.get(result);
        return result;
    }

    public BsatnWriter writeByteArray(byte[] bytes) {
        if (bytes == null)
            throw new NullPointerException("bytes");
        ensureCapacity(bytes.length);
        writeArrayLength(bytes.length);
        buf.put(bytes);
        return this;
    }

    public BsatnWriter writeByte(byte b) {
        ensureCapacity(1);
        buf.put(b);
        return this;
    }

    public BsatnWriter writeBool(boolean value) {
        return writeByte((byte) (value ? 1 : 0));
    }

    public BsatnWriter writeU8(int value) {
        if ((value & ~0xFF) != 0) {
            throw new IllegalArgumentException("u8 out of range: " + value);
        }
        return writeByte((byte) value);
    }

    public BsatnWriter writeI8(byte value) {
        return writeByte(value);
    }

    public BsatnWriter writeU16(int value) {
        if ((value & ~0xFFFF) != 0) {
            throw new IllegalArgumentException("u16 out of range: " + value);
        }
        ensureCapacity(2);
        buf.putShort((short) value);
        return this;
    }

    public BsatnWriter writeI16(short value) {
        ensureCapacity(2);
        buf.putShort(value);
        return this;
    }

    public BsatnWriter writeU32(long value) {
        if ((value & ~0xFFFF_FFFFL) != 0) {
            throw new IllegalArgumentException("u32 out of range: " + value);
        }
        ensureCapacity(4);
        buf.putInt((int) value);
        return this;
    }

    public BsatnWriter writeI32(int value) {
        ensureCapacity(4);
        buf.putInt(value);
        return this;
    }

    public BsatnWriter writeU64(long value) {
        ensureCapacity(8);
        buf.putLong(value);
        return this;
    }

    public BsatnWriter writeI64(long value) {
        ensureCapacity(8);
        buf.putLong(value);
        return this;
    }

    public BsatnWriter writeF32(float value) {
        return writeU32(Integer.toUnsignedLong(Float.floatToRawIntBits(value)));
    }

    public BsatnWriter writeF64(double value) {
        return writeU64(Double.doubleToRawLongBits(value));
    }

    public BsatnWriter writeString(String s) {
        if (s == null)
            throw new NullPointerException("s");
        byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
        writeU32(utf8.length);
        if (utf8.length == 0) {
            return this;
        }

        buf.put(utf8);
        return this;
    }

    public BsatnWriter writeArrayLength(int length) {
        if (length < 0)
            throw new IllegalArgumentException("array length < 0: " + length);
        writeU32(length);
        return this;
    }

    public BsatnWriter writeU128(U128 v) {
        if (v == null)
            throw new NullPointerException("v");
        writeU64(v.lower());
        writeU64(v.upper());
        return this;
    }

    public BsatnWriter writeI128(I128 v) {
        if (v == null)
            throw new NullPointerException("v");
        writeU64(v.lo());
        writeU64(v.hi());
        return this;
    }

    public BsatnWriter writeU256(U256 v) {
        if (v == null)
            throw new NullPointerException("v");
        writeU64(v.w0());
        writeU64(v.w1());
        writeU64(v.w2());
        writeU64(v.w3());
        return this;
    }

    public BsatnWriter writeI256(I256 v) {
        if (v == null)
            throw new NullPointerException("v");
        writeU64(v.w0());
        writeU64(v.w1());
        writeU64(v.w2());
        writeU64(v.w3());
        return this;
    }

    public BsatnWriter writeIdentity(Identity identity) {
        if (identity == null)
            throw new NullPointerException("identity");
        writeU256(identity.value());
        return this;
    }
}
