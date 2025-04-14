package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.EmployeeEntity;
import org.example.gymmanagementsystem.model.EmployeesDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeesMapper {
    EmployeesDto toDto(EmployeeEntity employeeEntity);
    EmployeeEntity toEntity(EmployeesDto employeesDto);
    List<EmployeesDto> toDtoList(List<EmployeeEntity> employeesEntities);


}
