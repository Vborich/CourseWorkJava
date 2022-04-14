package com.example.coursework.mappers;

import com.example.coursework.dto.UserDto;
import com.example.coursework.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toModel(UserDto userDto);
}