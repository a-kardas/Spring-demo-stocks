package com.fp.stock.controller;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByLogin(Principal user){
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UserDTO userDTO = userService.findUserByLogin(user.getName());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
