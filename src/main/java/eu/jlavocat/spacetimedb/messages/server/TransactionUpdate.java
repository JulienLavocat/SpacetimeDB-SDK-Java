package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;
import eu.jlavocat.spacetimedb.bsatn.TimeDuration;
import eu.jlavocat.spacetimedb.bsatn.Timestamp;

public record TransactionUpdate(
        UpdateStatus status, Timestamp timestamp, Identity callerIdentity, ConnectionId callerConnectionId,
        ReducerCallInfo reducerCall, EnergyQuanta energyQuantaUsed, TimeDuration totalHostExecutionDuration) {
}
