package com.fp.stock.service.impl;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.mapper.UserMapper;
import com.fp.stock.model.User;
import com.fp.stock.repository.UserRepository;
import com.fp.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        userDTO.setPassword("");
        return userDTO;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findOneByLogin(username);
        if(user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
    }
}
