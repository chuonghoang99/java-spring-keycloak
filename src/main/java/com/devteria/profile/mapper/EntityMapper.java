package com.devteria.profile.mapper;

import java.util.List;

public interface EntityMapper<D,E> {

    D toDto(E e);

    E toEntity(D d);

    List<D> toDto(List<E> e);

    List<E> toEntity(List<D> d);


}
