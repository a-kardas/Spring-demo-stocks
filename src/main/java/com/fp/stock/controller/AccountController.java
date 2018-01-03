package com.fp.stock.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/public")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

/*    @RequestMapping(value = "/authenticate",
            method = RequestMethod.POST)
    public void authorize(@RequestParam String username, @RequestParam String password) {

    }*/

    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Principal isAuthenticated(Principal user) {
        log.debug("REST request to check if the current user is authenticated");
        return user;
    }

    /*@RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUser())
                .map(user -> new ResponseEntity<>(UserDTO.builder().email(user.getLogin()).build(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }*/
}
