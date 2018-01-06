package com.fp.stock.service.impl;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.mapper.UserMapper;
import com.fp.stock.model.Stock;
import com.fp.stock.model.User;
import com.fp.stock.model.UserStock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.repository.UserRepository;
import com.fp.stock.repository.UserStockRepository;
import com.fp.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);

        List<UserStock> userStockList = new ArrayList<>();
        userDTO.getStocks().stream().forEach(s -> {
            if(s.getAmount() != null && s.getAmount() > 0) {
                Optional<Stock> stock = stockRepository.findByNameAndCode(s.getName(), s.getCode());
                if(stock.isPresent()){
                    UserStock userStock = new UserStock();
                    userStock.setUserId(savedUser.getId());
                    userStock.setStockId(stock.get().getId());
                    userStock.setAmount(s.getAmount());
                    userStockList.add(userStock);
                }
            }
        });

        if(!userStockList.isEmpty()){
            userStockRepository.save(userStockList);
        }

        userDTO.setPassword("");
        return userDTO;
    }

    @Override
    public UserDTO findUserByLogin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        if(!user.isPresent())
            return null;

        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user.get());
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
