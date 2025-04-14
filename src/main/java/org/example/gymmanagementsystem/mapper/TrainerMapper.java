package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.TrainerEntity;
import org.example.gymmanagementsystem.model.TrainerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    List<TrainerDto> toDtoList(List<TrainerEntity> list);
    TrainerDto toDto (TrainerEntity entity);
    TrainerEntity toEntity (TrainerDto dto);
}
