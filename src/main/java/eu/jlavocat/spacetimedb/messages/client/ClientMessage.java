package eu.jlavocat.spacetimedb.messages.client;

import java.nio.ByteBuffer;

import eu.jlavocat.spacetimedb.bsatn.BsatnWriter;
import eu.jlavocat.spacetimedb.bsatn.ToBsatn;

public sealed interface ClientMessage extends ToBsatn
        permits ClientMessage.CallReducerMessage, ClientMessage.SubscribeMultiMessage {

    public static final byte CALL_REDUCER_MESSAGE_TYPE = 0;
    static final byte SUBSCRIBE_MULTI_MESSAGE_TYPE = 4;

    /*
     * Implementation state
     * | DONE | 00 CallReducer
     * | XXXX | 01 Subscribe, legacy subscription
     * | ____ | 02 OneOffQuery
     * | XXXX | 03 SubscribeSingle, deprecated as per Phoebe
     * | DONE | 04 SubscribeMulti
     * | XXXX | 05 Unsubscribe, deprecated as per Phoebe
     * | ____ | 06 UnsubscribeMulti
     * | ____ | 07 CallProcedure
     */

    record CallReducerMessage(String reducer, byte[] args, long requestId, boolean isLightMode)
            implements ClientMessage {

        @Override
        public ByteBuffer toBsatn() {
            BsatnWriter w = new BsatnWriter();

            w.writeByte(CALL_REDUCER_MESSAGE_TYPE);

            w.writeString(reducer);
            w.writeByteArray(args);
            w.writeU32(requestId);
            w.writeBool(isLightMode);

            return w.toByteBuffer();
        }
    }

    record SubscribeMultiMessage(String[] queries, long requestId, long queryId) implements ClientMessage {

        @Override
        public ByteBuffer toBsatn() {
            BsatnWriter w = new BsatnWriter();

            w.writeByte(SUBSCRIBE_MULTI_MESSAGE_TYPE);
            w.writeArrayLength(queries.length);
            for (String query : queries) {
                w.writeString(query);
            }
            w.writeU32(requestId);
            w.writeU32(queryId);

            return w.toByteBuffer();
        }
    }

}
