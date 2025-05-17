package fr.globalmarket.adapter.out.db.mapper;

@FunctionalInterface
public interface Mapper<R, T> {

    T toDomain(R entity);

}
