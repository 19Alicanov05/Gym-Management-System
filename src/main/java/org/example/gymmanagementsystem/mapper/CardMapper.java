package org.example.gymmanagementsystem.mapper;
import org.example.gymmanagementsystem.dao.entity.CardEntity;
import org.example.gymmanagementsystem.model.CardDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDto toDto(CardEntity cardEntity);
    CardEntity toEntity(CardDto cardDto);
    List<CardDto> toDtoList(List<CardEntity> cardEntities);
}
