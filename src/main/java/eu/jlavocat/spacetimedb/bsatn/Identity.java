package eu.jlavocat.spacetimedb.bsatn;

import java.util.HexFormat;

public final record Identity(U256 value) {

    public static final Identity ZERO = new Identity(new U256(0, 0, 0, 0));
    public static final Identity ONE = new Identity(new U256(0, 0, 0, 1));

    @Override
    public String toString() {
        byte[] bytes = value.toBytesBE();
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.formatHex(bytes);
    }

}
