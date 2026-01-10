package eu.jlavocat.spacetimedb.messages.client;

import java.nio.ByteBuffer;

import eu.jlavocat.spacetimedb.bsatn.BsatnWriter;
import eu.jlavocat.spacetimedb.bsatn.ToBsatn;

public sealed interface ClientMessage extends ToBsatn
        permits ClientMessage.CallReducerMessage {

    public static final byte CALL_REDUCER_MESSAGE_TYPE = 0;

    record CallReducerMessage(CallReducer payload) implements ClientMessage {

        @Override
        public ByteBuffer toBsatn() {
            BsatnWriter w = new BsatnWriter();

            w.writeByte(CALL_REDUCER_MESSAGE_TYPE);

            w.writeString(payload.reducer());
            w.writeByteArray(payload.args());
            w.writeU32(payload.requestId());
            w.writeByte(payload.isLightMode() ? (byte) 1 : (byte) 0);

            return w.toByteBuffer();
        }
    }

}
