package com.fp.stock.service;

import com.fp.stock.component.validator.OperationValidator;
import com.fp.stock.component.exceptions.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import com.fp.stock.model.User;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.repository.UserRepository;
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
        @Sql(scripts = "/scripts/operation_validator_test.sql", executionPhase = BEFORE_TEST_METHOD)})
public class OperationValidatorTest {

    private final String USER_EMAIL = "test@test.pl";

    private final BigDecimal NON_RESOURCE = new BigDecimal(0).setScale(2);

    @Autowired
    private OperationValidator operationValidator;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void invalidateBecauseOfStock() throws Exception {
        //Given
        StockDTO stockDTO = new StockDTO();
        stockDTO.setName("NonExisting");
        stockDTO.setCode("NE");
        stockDTO.setAmount(1000);

        try {
            //When
            operationValidator.validatePurchase(null, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.DEFAULT));
            return;
        }

        Assert.fail();

    }

    @Test

    public void invalidateBecauseOfRates() throws Exception {
        //Given
        Optional<Stock> byNameAndCode = stockRepository.findByNameAndCode("TEST", "TS");
        Stock stock = byNameAndCode.get();

        ExchangeRate rate = stock.getExchangeRates().iterator().next();
        BigDecimal price = rate.getPrice().add(BigDecimal.TEN);
        rate.setPrice(price);

        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCode(stock.getCode());
        stockDTO.setAmount(stock.getAmount());
        stockDTO.setRate(rate);

        try {
            //When
            operationValidator.validatePurchase(null, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.DEFAULT));
            return;
        }

        Assert.fail();

    }

    @Test
    public void invalidateBecauseOfAmountIsNotMultiple(){
        Optional<Stock> byNameAndCode = stockRepository.findByNameAndCode("TEST", "TS");
        Stock stock = byNameAndCode.get();
        ExchangeRate rate = stock.getExchangeRates().iterator().next();

        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCode(stock.getCode());
        stockDTO.setAmount(stock.getAmount() + 1);
        stockDTO.setRate(rate);

        try {
            //When
            operationValidator.validatePurchase(null, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.AMOUNT_IS_NOT_MULTIPLE));
            return;
        }

        Assert.fail();

    }

    @Test
    public void invalidateNotEnoughUnits(){
        Optional<Stock> byNameAndCode = stockRepository.findByNameAndCode("TEST2", "TS2");
        Stock stock = byNameAndCode.get();
        ExchangeRate rate = stock.getExchangeRates().iterator().next();

        Assert.assertTrue(stock.getAmount() == 0);

        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCode(stock.getCode());
        stockDTO.setAmount(10);
        stockDTO.setRate(rate);

        try {
            //When
            operationValidator.validatePurchase(null, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.NOT_ENOUGH_UNITS_ON_STOCK));
            return;
        }

        Assert.fail();

    }

    @Test
    public void invalidateUserDontHaveFinancialResources(){
        Optional<User> oneByLogin = userRepository.findOneByLogin(USER_EMAIL);
        User user = oneByLogin.get();

        Assert.assertTrue(user.getFinancialResources().equals(NON_RESOURCE));

        Optional<Stock> byNameAndCode = stockRepository.findByNameAndCode("TEST", "TS");
        Stock stock = byNameAndCode.get();
        ExchangeRate rate = stock.getExchangeRates().iterator().next();

        Assert.assertTrue(stock.getAmount() == 1000);

        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCode(stock.getCode());
        stockDTO.setAmount(10);
        stockDTO.setRate(rate);

        try {
            //When
            operationValidator.validatePurchase(user, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.NOT_ENOUGH_MONEY));
            return;
        }

        Assert.fail();

    }

    @Test
    public void invalidateUserDontHaveUnits(){
        Optional<User> oneByLogin = userRepository.findOneByLogin(USER_EMAIL);
        User user = oneByLogin.get();

        Assert.assertTrue(user.getFinancialResources().equals(NON_RESOURCE));

        Optional<Stock> byNameAndCode = stockRepository.findByNameAndCode("TEST", "TS");
        Stock stock = byNameAndCode.get();
        ExchangeRate rate = stock.getExchangeRates().iterator().next();

        Assert.assertTrue(stock.getAmount() == 1000);

        StockDTO stockDTO = new StockDTO();
        stockDTO.setName(stock.getName());
        stockDTO.setCode(stock.getCode());
        stockDTO.setAmount(10);
        stockDTO.setRate(rate);

        try {
            //When
            operationValidator.validateSale(user, stockDTO);

        } catch(OperationsNotAllowedException e){

            //Then
            Assert.assertTrue(e.getMessage().equals(OperationsNotAllowedException.USER_DONT_HAVE_ENOUGH_UNITS));
            return;
        }

        Assert.fail();

    }
}
