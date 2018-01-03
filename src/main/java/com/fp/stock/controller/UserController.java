package com.fp.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/public")
public class UserController {

    /*@Autowired
    private UserService userService;

    @RequestMapping(value = "/user/login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login){
        Optional<User> user = userService.getUserByLogin(login);

        if(user.isPresent()) {
            UserDTO userDTO = UserDTO.builder().email(user.get().getLogin()).build();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/
}
