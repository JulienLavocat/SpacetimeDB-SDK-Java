package eu.jlavocat.spacetimedb.messages.server;

import java.util.Optional;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;
import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;
import eu.jlavocat.spacetimedb.bsatn.TimeDuration;
import eu.jlavocat.spacetimedb.bsatn.Timestamp;

public sealed interface ServerMessage
        permits ServerMessage.IdentityTokenMessage, ServerMessage.TransactionUpdateMessage,
        ServerMessage.SubscriptionErrorMessage, ServerMessage.SubscribeMultiAppliedMessage {

    static final byte TRANSACTION_UPDATE_MESSAGE = 1;
    static final byte IDENTITY_TOKEN_MESSAGE = 3;
    static final byte SUBSCRIPTION_ERROR_MESSAGE = 7;
    static final byte SUBSCRIBE_MULTI_APPLIED_MESSAGE = 8;

    /*
     * Implementation state:
     * | XXXX | 00 InitialSubscription, legacy subscription
     * | DONE | 01 TransactionUpdate
     * | ____ | 02 TransactionUpdateLight
     * | DONE | 03 IdentityToken
     * | ____ | 04 OneOffQueryResponse
     * | XXXX | 05 SubscribeApplied, legacy subscription
     * | XXXX | 06 UnsubscribeApplied, legacy subscription
     * | DONE | 07 SubscriptionError
     * | DONE | 08 SubscribeMultiApplied
     * | ____ | 09 UnsubscribeMultiApplied
     * | ____ | 10 ProcedureResult
     */

    record IdentityTokenMessage(IdentityToken payload) implements ServerMessage {
    }

    record TransactionUpdateMessage(TransactionUpdate payload) implements ServerMessage {
    }

    record SubscriptionErrorMessage(SubscriptionError payload) implements ServerMessage {
    }

    record SubscribeMultiAppliedMessage(SubscribeMultiApplied payload) implements ServerMessage {
    }

    public static ServerMessage fromBsatn(BsatnReader reader) {
        byte serverMsgType = reader.readByte();
        System.out.println("Server message type: " + serverMsgType);

        return switch (serverMsgType) {
            case IDENTITY_TOKEN_MESSAGE -> decodeIdentityTokenMessage(reader);
            case TRANSACTION_UPDATE_MESSAGE -> decodeTransactionUpdateMessage(reader);
            case SUBSCRIPTION_ERROR_MESSAGE -> decodeSubscriptionErrorMessage(reader);
            case SUBSCRIBE_MULTI_APPLIED_MESSAGE -> decodeSubscribeMultiApplied(reader);
            default -> throw new IllegalStateException("Unknown server message type: " + serverMsgType);
        };
    }

    private static ServerMessage.IdentityTokenMessage decodeIdentityTokenMessage(BsatnReader reader) {
        var identity = reader.readIdentity();
        var token = reader.readString();
        var connectionId = reader.readConnectionId();

        IdentityToken payload = new IdentityToken(identity, token, connectionId);
        return new ServerMessage.IdentityTokenMessage(payload);
    }

    private static ServerMessage.TransactionUpdateMessage decodeTransactionUpdateMessage(BsatnReader reader) {
        UpdateStatus status = UpdateStatus.fromBsatn(reader);
        Timestamp timestamp = reader.readTimestamp();
        Identity callerIdentity = reader.readIdentity();
        ConnectionId callerConnectionId = reader.readConnectionId();
        ReducerCallInfo reducerCallInfo = ReducerCallInfo.fromBsatn(reader);
        EnergyQuanta energyQuanta = EnergyQuanta.fromBsatn(reader);
        TimeDuration timeDuration = TimeDuration.fromBsatn(reader);

        TransactionUpdate payload = new TransactionUpdate(status, timestamp, callerIdentity,
                callerConnectionId, reducerCallInfo, energyQuanta, timeDuration);
        return new ServerMessage.TransactionUpdateMessage(payload);
    }

    private static ServerMessage decodeSubscriptionErrorMessage(BsatnReader reader) {
        long totalHostExecutionDurationMicros = reader.readU64();
        Optional<Long> requestId = reader.readOptional((r) -> r.readU32());
        Optional<Long> queryId = reader.readOptional((r) -> r.readU32());
        Optional<TableId> tableId = reader.readOptional((r) -> {
            String pascalCase = r.readString();
            String snakeCase = r.readString();
            return new TableId(pascalCase, snakeCase);
        });
        String error = reader.readString();

        SubscriptionError payload = new SubscriptionError(totalHostExecutionDurationMicros, requestId, queryId, tableId,
                error);

        return new ServerMessage.SubscriptionErrorMessage(payload);
    }

    private static ServerMessage decodeSubscribeMultiApplied(BsatnReader reader) {
        var requestId = reader.readU32();
        var totalHostExecutionDurationMicros = reader.readU64();
        var queryId = reader.readU32();
        var update = DatabaseUpdate.fromBsatn(reader);

        var payload = new SubscribeMultiApplied(requestId, totalHostExecutionDurationMicros, queryId, update);
        return new ServerMessage.SubscribeMultiAppliedMessage(payload);
    }

}
