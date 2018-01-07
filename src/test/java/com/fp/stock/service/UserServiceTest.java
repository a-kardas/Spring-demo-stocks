package com.fp.stock.service;

import com.fp.stock.dto.UserDTO;
import com.fp.stock.model.User;
import com.fp.stock.repository.UserRepository;
import org.assertj.core.api.Fail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SqlGroup({@Sql(scripts = "/scripts/truncate.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql(scripts = "/scripts/user_service_test.sql", executionPhase = BEFORE_TEST_METHOD)})
public class UserServiceTest {

    private final String USER_EMAIL = "test@test.pl";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() throws Exception {
        //Given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(USER_EMAIL);
        userDTO.setPassword("test");
        userDTO.setName("Test test");
        userDTO.setFinancialResources(BigDecimal.ZERO);

        //When
        userService.saveUser(userDTO);

        //Then
        Optional<User> oneByLogin = userRepository.findOneByLogin("test@test.pl");

        if(oneByLogin.isPresent()){
            User user = oneByLogin.get();
            Assert.assertTrue(user.getLogin().equals(USER_EMAIL));
            Assert.assertFalse(user.getPassword().equals("test"));
        } else {
            Fail.fail("Registered user doesn't saved.");
        }
    }

    @Test
    public void shouldntSaveUserDuplicatedEmail() throws Exception {

        //Given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test2@test.pl");
        userDTO.setPassword("test2");
        userDTO.setName("Test2 Test2");
        userDTO.setFinancialResources(BigDecimal.ZERO);

        try {
            //When
            userService.saveUser(userDTO);
        } catch (Exception e){

            //Then
            Assert.assertTrue(e.getMessage().equals("E-mail is already registered"));
            Optional<User> oneByLogin = userRepository.findOneByLogin("test2@test.pl");
            User user = oneByLogin.get();
            Assert.assertTrue(user != null);
            return;
        }
        Assert.fail();
    }

    @Test
    public void shouldFindUser() throws Exception {
        //Given
        shouldSaveUser();

        //When
        UserDTO userDTO = userService.findUserByLogin(USER_EMAIL);

        //Then
        Assert.assertTrue(userDTO.getEmail().equals(USER_EMAIL));
        Assert.assertTrue(userDTO.getPassword() == null);
    }

    @Test
    public void shouldntFindUserUnexistingEmail(){
        //Given
        String nonexistingEmail = "nonexisting@test.pl";

        //When
        UserDTO userDTO = userService.findUserByLogin(nonexistingEmail);

        //Then
        Assert.assertTrue(userDTO == null);
    }
}
