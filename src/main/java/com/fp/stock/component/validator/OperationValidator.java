package com.fp.stock.component.validator;

import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.User;
import com.fp.stock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Component
public class OperationValidator extends OperationBaseValidator {

    @Autowired
    private UserRepository userRepository;

    public ValidatorResult validatePurchase(User user, StockDTO stockDTO) throws OperationsNotAllowedException {
        ValidatorResult result = new ValidatorResult(validate(stockDTO));

        if(result.getStock().getAmount() < stockDTO.getAmount()) {
            throw new OperationsNotAllowedException(OperationsNotAllowedException.NOT_ENOUGH_UNITS_ON_STOCK);
        }

        BigDecimal stocksPrice = result.getRate().getPrice().multiply(BigDecimal.valueOf(stockDTO.getAmount()));

        //VALIDATE USER AND HIS RESOURCES
        if(user.getFinancialResources().compareTo(stocksPrice) < 0) {
            throw new OperationsNotAllowedException(OperationsNotAllowedException.NOT_ENOUGH_MONEY);
        }

        result.setStockPrice(stocksPrice);
        result.setUser(user);
        return result;
    }

    public ValidatorResult validateSale(User user, StockDTO stockDTO) throws OperationsNotAllowedException {
        ValidatorResult result = new ValidatorResult(validate(stockDTO));

        //VALIDATE USER AND HIS RESOURCES
        final int[] userStockAmount = {0};
        user.getStocks().stream().forEach(s -> {
            if(s.getStockId() == result.getStock().getId()){
                userStockAmount[0] = s.getAmount();
            }
        });

        if(userStockAmount[0] < stockDTO.getAmount()){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.USER_DONT_HAVE_ENOUGH_UNITS);
        }

        BigDecimal stocksPrice = result.getRate().getPrice().multiply(BigDecimal.valueOf(stockDTO.getAmount()));

        result.setStockPrice(stocksPrice);
        result.setUser(user);
        return result;
    }

    private User getUserOrThrow(Principal principal) throws OperationsNotAllowedException {
        Optional<User> user = userRepository.findOneByLogin(principal.getName());

        if(user.isPresent()){
            return user.get();
        }

        throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
    }
}
