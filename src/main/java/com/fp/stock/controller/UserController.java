package com.fp.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/public")
public class UserController {

    /*@Autowired
    private UserService userService;

    @RequestMapping(value = "/user/email/{email}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String email){
        Optional<User> user = userService.getUserByLogin(email);

        if(user.isPresent()) {
            UserDTO userDTO = UserDTO.builder().email(user.get().getEmail()).build();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/
}
