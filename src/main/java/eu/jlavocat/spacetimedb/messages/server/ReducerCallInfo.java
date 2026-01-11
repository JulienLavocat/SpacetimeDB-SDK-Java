package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public record ReducerCallInfo(String reducerName, long reducerId, byte[] args, long requestId) {

    @Override
    public String toString() {
        return "ReducerCallInfo{" +
                "reducerName='" + reducerName + '\'' +
                ", reducerId=" + reducerId +
                ", argsLength=" + args.length +
                ", requestId=" + requestId +
                '}';
    }

    public static ReducerCallInfo fromBsatn(BsatnReader reader) {
        String reducerName = reader.readString();
        long reducerId = reader.readU32();
        int argsLength = reader.readArrayLength();
        byte[] args = reader.readBytes(argsLength);
        long requestId = reader.readU32();
        return new ReducerCallInfo(reducerName, reducerId, args, requestId);
    }

}
