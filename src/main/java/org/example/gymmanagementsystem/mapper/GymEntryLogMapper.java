package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.GymEntryLogEntity;
import org.example.gymmanagementsystem.model.GymEntryLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GymEntryLogMapper {

    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.surname", target = "customerSurname")
    GymEntryLogDto toDto(GymEntryLogEntity gymEntryLogEntity);

    List<GymEntryLogDto> toDtoList(List<GymEntryLogEntity> gymEntryLogEntities);
}
