package eu.jlavocat.spacetimedb.bsatn;

import java.util.HexFormat;

public final class Identity {

    private final U256 value;

    public Identity(U256 value) {
        this.value = value;
    }

    @Override
    public String toString() {
        byte[] bytes = value.toBytesBE();
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.formatHex(bytes);
    }

}
