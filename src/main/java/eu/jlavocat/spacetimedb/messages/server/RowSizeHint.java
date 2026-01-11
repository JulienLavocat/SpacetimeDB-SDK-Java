package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public sealed interface RowSizeHint permits RowSizeHint.FixedSize, RowSizeHint.RowOffsets {

    static final byte FIXED_SIZE_TYPE = 0;
    static final byte ROW_OFFSETS_TYPE = 1;

    record FixedSize(int size) implements RowSizeHint {
    }

    record RowOffsets(long[] offsets) implements RowSizeHint {
    }

    public static RowSizeHint fromBsatn(BsatnReader reader) {
        byte hintType = reader.readByte();
        switch (hintType) {
            case FIXED_SIZE_TYPE -> {
                int size = reader.readU16();
                return new FixedSize(size);
            }
            case ROW_OFFSETS_TYPE -> {
                int length = reader.readArrayLength();
                long[] offsets = new long[length];
                for (int i = 0; i < length; i++) {
                    offsets[i] = reader.readU64();
                }
                return new RowOffsets(offsets);
            }
            default -> throw new IllegalArgumentException("Unknown RowSizeHint type: " + hintType);
        }
    }
}
