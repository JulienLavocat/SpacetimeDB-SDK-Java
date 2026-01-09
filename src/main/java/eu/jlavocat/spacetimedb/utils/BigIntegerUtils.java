package eu.jlavocat.spacetimedb.utils;

public class BigIntegerUtils {

    public static void putLongBE(byte[] arr, int offset, long value) {
        for (int i = 7; i >= 0; i--) {
            arr[offset + i] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    public static void putLongLE(byte[] arr, int offset, long value) {
        for (int i = 0; i < 8; i++) {
            arr[offset + i] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }
}
