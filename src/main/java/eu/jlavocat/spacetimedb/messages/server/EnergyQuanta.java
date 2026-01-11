package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;
import eu.jlavocat.spacetimedb.bsatn.U128;

public record EnergyQuanta(U128 quanta) {

    public static EnergyQuanta fromBsatn(BsatnReader reader) {
        return new EnergyQuanta(reader.readU128());
    }
}
