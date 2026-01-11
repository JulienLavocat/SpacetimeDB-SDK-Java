package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public record BsatnRowList(RowSizeHint rowSizeHint, byte[] rowsData) {

    public static BsatnRowList fromBsatn(BsatnReader reader) {
        RowSizeHint rowSizeHint = RowSizeHint.fromBsatn(reader);
        int dataLength = reader.readArrayLength();
        byte[] rowsData = reader.readBytes(dataLength);
        return new BsatnRowList(rowSizeHint, rowsData);
    }
}
