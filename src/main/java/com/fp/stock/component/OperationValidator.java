package com.fp.stock.component;

import com.fp.stock.component.operations.DeferredStackOperation;
import com.fp.stock.component.validator.OperationBaseValidator;
import com.fp.stock.component.validator.ValidatorResult;
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

    public ValidatorResult validatePurchase(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        ValidatorResult result = new ValidatorResult(validate(stockDTO));

        BigDecimal stocksPrice = result.getRate().getPrice().multiply(BigDecimal.valueOf(stockDTO.getAmount()));

        //VALIDATE USER AND HIS RESOURCES
        User user = getUserOrThrow(principal);

        if(user.getFinancialResources().compareTo(stocksPrice) < 0) {
            throw new OperationsNotAllowedException(OperationsNotAllowedException.NOT_ENOUGH_MONEY);
        }

        result.setStockPrice(stocksPrice);
        result.setUser(user);
        return result;
    }

    public ValidatorResult validateSale(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        ValidatorResult result = (ValidatorResult) validate(stockDTO);

        //VALIDATE USER AND HIS RESOURCES
        User user = getUserOrThrow(principal);

        final int[] userStockAmount = {0};
        user.getStocks().stream().forEach(s -> {
            if(s.getStockId() == result.getStock().getId()){
                userStockAmount[0] = s.getAmount();
            }
        });

        if(userStockAmount[0] < stockDTO.getAmount()){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.USER_DONT_HAVE_ENOUGH_UNITS);
        }

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
