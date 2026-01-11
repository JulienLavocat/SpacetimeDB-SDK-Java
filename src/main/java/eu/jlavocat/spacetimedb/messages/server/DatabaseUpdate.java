package eu.jlavocat.spacetimedb.messages.server;

import java.util.ArrayList;
import java.util.List;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public record DatabaseUpdate(List<TableUpdate> tables) {

    public static DatabaseUpdate fromBsatn(BsatnReader reader) {
        int size = reader.readArrayLength();
        System.out.println("DatabaseUpdate tables count: " + size);
        ArrayList<TableUpdate> tables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TableUpdate tableUpdate = TableUpdate.fromBsatn(reader);
            tables.add(tableUpdate);
        }
        return new DatabaseUpdate(tables);
    }
}
