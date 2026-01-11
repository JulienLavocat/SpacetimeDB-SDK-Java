package eu.jlavocat.spacetimedb.messages.server;

import java.util.ArrayList;
import java.util.List;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public record TableUpdate(long tableId, String tableName, long numRows, List<QueryUpdate> updates) {

    public static TableUpdate fromBsatn(BsatnReader reader) {
        long tableId = reader.readU32();
        String tableName = reader.readString();
        long numRows = reader.readU64();
        int updatesCount = reader.readArrayLength();
        System.out.println("TableUpdate: tableId=" + tableId + ", tableName=" + tableName + ", numRows=" + numRows
                + ", updatesCount=" + updatesCount);

        List<QueryUpdate> updates = new ArrayList<>();
        for (int i = 0; i < updatesCount; i++) {
            byte isCompressed = reader.readByte();
            // TODO: support compressed updates
            if (isCompressed != 0) {
                throw new IllegalStateException("Compressed updates are not supported yet");
            }

            QueryUpdate update = QueryUpdate.fromBsatn(reader);
            updates.add(update);
        }

        return new TableUpdate(tableId, tableName, numRows, updates);
    }

}
