package org.example.gymmanagementsystem.mapper;

import org.example.gymmanagementsystem.dao.entity.UserEntity;
import org.example.gymmanagementsystem.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserDto userDto);
}
