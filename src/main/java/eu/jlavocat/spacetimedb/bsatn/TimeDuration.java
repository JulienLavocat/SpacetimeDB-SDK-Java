package eu.jlavocat.spacetimedb.bsatn;

public record TimeDuration(long micros) {

    public static TimeDuration fromBsatn(BsatnReader reader) {
        long micros = reader.readI64();
        return new TimeDuration(micros);
    }

}
