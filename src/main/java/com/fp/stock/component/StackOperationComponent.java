package com.fp.stock.component;

import com.fp.stock.component.events.ExchangeRateErrorEvent;
import com.fp.stock.component.events.ExchangeRateSuccessEvent;
import com.fp.stock.component.operations.DeferredStackOperation;
import com.fp.stock.component.operations.OperationType;
import com.fp.stock.component.validator.OperationValidator;
import com.fp.stock.component.validator.ValidatorResult;
import com.fp.stock.component.exceptions.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.Stock;
import com.fp.stock.model.User;
import com.fp.stock.model.UserStock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.repository.UserRepository;
import com.fp.stock.repository.UserStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class StackOperationComponent {

    private final Logger log = LoggerFactory.getLogger(StackOperationComponent.class);

    private final Queue<DeferredStackOperation> queue = new ConcurrentLinkedQueue<>();

    private boolean isWorking = false;

    private boolean isExchangeRateActual = true;

    @Autowired
    private OperationValidator operationValidator;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private UserRepository userRepository;

    @Async
    @EventListener
    public void handleExchangeRateError(ExchangeRateErrorEvent event) {
        log.info("Event handler - Exchange rate error.");
        isExchangeRateActual = false;
        queue.stream().forEach(o ->
                setErrorForRequest(o, new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT))
        );
    }

    @Async
    @EventListener
    public void handleExchangeRateSuccess(ExchangeRateSuccessEvent event) {
        log.info("Event handler - Exchange rate success.");
        isExchangeRateActual = true;
    }

    public void addOperation(DeferredStackOperation stackOperation) throws OperationsNotAllowedException {
        queue.add(stackOperation);
        processQueue();
    }

    private void processQueue() throws OperationsNotAllowedException {
        if(!queue.isEmpty() && !isWorking){

            isWorking = true;

            DeferredStackOperation operation = queue.poll();
            boolean isSuccess = false;

            try {

                if(operation.getType().equals(OperationType.PURCHASE)){

                    isSuccess = buyStocks(operation.getUser(), operation.getStockDTO(), operation);
                    setResultForRequest(operation, isSuccess);

                } else if (operation.getType().equals(OperationType.SALE)){

                    isSuccess = sellStocks(operation.getUser(), operation.getStockDTO(), operation);
                    setResultForRequest(operation, isSuccess);

                } else {
                    throw new OperationsNotAllowedException(OperationsNotAllowedException.UNKNOWN_OPERATION);
                }

            } catch (OperationsNotAllowedException exception){
                setErrorForRequest(operation, exception);
            } finally {
                isWorking = false;
            }

            processQueue();
        }
    }

    @Transactional
    boolean buyStocks(User user, StockDTO stockDTO, DeferredStackOperation operation) throws OperationsNotAllowedException {
        if(!isExchangeRateActual){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
        }

        ValidatorResult purchase = operationValidator.validatePurchase(user, stockDTO);

        changeAmountOfStock(purchase.getStock().getId(), stockDTO.getAmount(), true);

        BigDecimal restUserMoney = purchase.getUser().getFinancialResources().subtract(purchase.getStockPrice());
        purchase.getUser().setFinancialResources(restUserMoney);
        userRepository.save(purchase.getUser());

        Optional<UserStock> userStock = getUserStock(purchase.getUser().getId(), purchase.getStock().getId());

        if(userStock.isPresent()){
            int amount = userStock.get().getAmount() + stockDTO.getAmount();
            userStock.get().setAmount(amount);
            userStockRepository.save(userStock.get());
        } else {
            UserStock newUserStock = new UserStock();
            newUserStock.setUserId(purchase.getUser().getId());
            newUserStock.setStockId(purchase.getStock().getId());
            newUserStock.setAmount(stockDTO.getAmount());
            userStockRepository.save(newUserStock);
        }

        return true;
    }

    @Transactional
    boolean sellStocks(User user, StockDTO stockDTO, DeferredStackOperation operation) throws OperationsNotAllowedException {
        if(!isExchangeRateActual){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
        }

        ValidatorResult sale = operationValidator.validateSale(user, stockDTO);

        changeAmountOfStock(sale.getStock().getId(), stockDTO.getAmount(), false);

        Set<UserStock> toRemove = new LinkedHashSet<>();

        sale.getUser().getStocks().stream().forEach(s -> {
            if(s.getStockId() == sale.getStock().getId()){
                int amount = s.getAmount();

                if(amount - stockDTO.getAmount() == 0) {
                    userStockRepository.delete(s.getId());
                    toRemove.add(s);

                } else {
                    s.setAmount(amount - stockDTO.getAmount());
                    userStockRepository.save(s);
                }
            }
        });

        sale.getUser().getStocks().removeAll(toRemove);

        BigDecimal userMoney = sale.getUser().getFinancialResources().add(sale.getStockPrice());
        sale.getUser().setFinancialResources(userMoney);
        userRepository.save(sale.getUser());

        return true;
    }

    private void changeAmountOfStock(Long stockId, int change, boolean isPurchase) {
        Stock stock = stockRepository.findOne(stockId);
        int amount = stock.getAmount();
        if(isPurchase) { //purchase
            stock.setAmount(amount - change);
        } else {        //sale
            stock.setAmount(amount + change);
        }
        stockRepository.save(stock);
    }

    private Optional<UserStock> getUserStock(Long userId, Long stockId){
        return userStockRepository.findOneByUserIdAndStockId(userId, stockId);
    }

    private void setErrorForRequest(DeferredStackOperation operation, Exception e) {
        operation.setErrorResult(e);
    }

    public void setResultForRequest(DeferredStackOperation operation, boolean isSuccess) {
        ResponseEntity<Boolean> result = new ResponseEntity<>(isSuccess, HttpStatus.OK);
        operation.setResult(result);
    }


}
