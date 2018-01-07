package com.fp.stock.controller;


import com.fp.stock.dto.AuthUserDTO;
import com.fp.stock.dto.UserDTO;
import com.fp.stock.mapper.UserMapper;
import com.fp.stock.model.User;
import com.fp.stock.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Enumeration;


@RestController
@RequestMapping("/api/public")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authenticate",
            method = RequestMethod.POST)
    public void authorize(@RequestBody @Valid AuthUserDTO userDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> isAuthenticated(Principal user) {
        log.debug("REST request to check if the current user is authenticated");
        if(user == null)
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        UserDTO userDTO = userService.findUserByLogin(user.getName());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) throws Exception {
        log.debug("REST request to register a new user");
        UserDTO result = userService.saveUser(userDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/logout",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> logout(HttpServletRequest request) throws ServletException {

        SecurityContextHolder.clearContext();

        // Remove all session data
        HttpSession session = request.getSession();

        for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
            session.removeAttribute((String) e.nextElement());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
