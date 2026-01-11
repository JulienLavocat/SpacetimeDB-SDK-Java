package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public record QueryUpdate(BsatnRowList deletes, BsatnRowList inserts) {
    public static QueryUpdate fromBsatn(BsatnReader reader) {
        BsatnRowList deletes = BsatnRowList.fromBsatn(reader);
        BsatnRowList inserts = BsatnRowList.fromBsatn(reader);
        return new QueryUpdate(deletes, inserts);
    }
}
