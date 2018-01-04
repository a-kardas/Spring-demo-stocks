package com.fp.stock.mapper;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );

    @Mapping(source = "login", target = "email")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "email", target = "login")
    User userDTOToUser(UserDTO userDTO);
}
