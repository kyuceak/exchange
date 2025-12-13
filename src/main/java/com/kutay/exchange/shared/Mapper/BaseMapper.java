package com.kutay.exchange.shared.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<E, REQD, RESD> {
    // subclasses must implement them
    public abstract E convertToEntity(REQD dto, Object... args);

    public abstract RESD convertToDTO(E entity, Object... args);

    public Collection<E> convertToEntity(Collection<REQD> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).collect(Collectors.toList());
    }

    public Collection<RESD> convertToDto(Collection<E> entity, Object... args) {
        return entity.stream().map(e -> convertToDTO(e, args)).collect(Collectors.toList());
    }

    public List<E> convertToEntityList(List<REQD> dtoList, Object... args) {
        return dtoList.stream().map(dto -> convertToEntity(dto, args)).collect(Collectors.toList());
    }

    public List<RESD> convertToDtoList(List<E> entityList, Object... args) {
        return entityList.stream().map(dto -> convertToDTO(dto, args)).collect(Collectors.toList());
    }


    // maybe implement sets in the future

}
