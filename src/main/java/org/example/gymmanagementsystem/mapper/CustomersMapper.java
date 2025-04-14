package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.CustomerEntity;
import org.example.gymmanagementsystem.model.CustomerResponseDto;
import org.example.gymmanagementsystem.model.CustomersDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CustomersMapper {


    @Mapping(source = "card.cardNumber", target = "cardNumber")
    @Mapping(source = "trainer.id", target = "trainerId")
    CustomerResponseDto toResponseDto(CustomerEntity customerEntity);

    CustomerEntity toEntity(CustomersDto customersDto);

    @Mapping(source = "card.cardNumber", target = "cardNumber")
    @Mapping(source = "trainer.id", target = "trainerId")
    CustomersDto toDto(CustomerEntity customerEntity);


}
