package com.fp.stock.mapper;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class );

    @Mappings({
            @Mapping(source = "login", target = "email"),
            @Mapping(target = "stocks", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(source = "stocks", target = "userStocks")
    })
    UserDTO userToUserDTO(User user);


    @Mappings({
            @Mapping(source = "email", target = "login"),
            @Mapping(target = "stocks", ignore = true)
    })
    User userDTOToUser(UserDTO userDTO);
}
