package eu.jlavocat.spacetimedb.bsatn;

public interface FromBsatn<T> {
    public T fromBsatn(BsatnReader reader);
}
