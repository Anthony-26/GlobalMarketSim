package fr.globalmarket.adapter.outbound.persistence.mapper;

@FunctionalInterface
public interface Mapper<R, T> {

    T toDomain(R entity);

}
