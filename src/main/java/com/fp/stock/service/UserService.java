package com.fp.stock.service;


import com.fp.stock.dto.UserDTO;

public interface UserService {

    UserDTO saveUser(UserDTO userDTO);

    UserDTO findUserByLogin(String login);
}
