package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.ProductEntity;
import org.example.gymmanagementsystem.model.ProductDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(ProductEntity productEntity);
    ProductEntity toEntity(ProductDto productDto);
    List<ProductDto> toDtoList(List<ProductEntity> productEntities);
}
