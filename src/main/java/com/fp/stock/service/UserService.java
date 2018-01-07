package com.fp.stock.service;


import com.fp.stock.dto.UserDTO;
import com.fp.stock.model.User;

import java.security.Principal;

public interface UserService {

    UserDTO saveUser(UserDTO userDTO) throws Exception;

    UserDTO findUserByLogin(String login);

    User findUserOrThrow(Principal principal) throws Exception;
}
