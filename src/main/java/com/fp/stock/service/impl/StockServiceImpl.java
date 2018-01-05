package com.fp.stock.service.impl;

import com.fp.stock.component.OperationValidator;
import com.fp.stock.component.validator.ValidatorResult;
import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.component.events.ExchangeRateErrorEvent;
import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.mapper.StockMapper;
import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import com.fp.stock.model.UserStock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.repository.UserStockRepository;
import com.fp.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private boolean isExchangeRateActual = false;

    @Autowired
    private OperationValidator operationValidator;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    @Autowired
    private EntityManager entityManager;

    @Async
    @EventListener
    @Override
    public void handleDownloadedExchangeRate(ExchangeRateDownloadedEvent<ExternalStockListDTO> stockList){
        log.info("Event handler - Exchange rate downloaded.");
        isExchangeRateActual = true;

        if(!stockList.getData().getItems().isEmpty()){
            ZonedDateTime publicationDate = stockList.getData().getPublicationDate();

            stockList.getData().getItems().stream().forEach(s -> {
                Optional<Stock> existingStock = stockRepository.findByNameAndCode(s.getName(), s.getCode()); //check if same exists

                if(existingStock.isPresent()){
                    Stock stock = existingStock.get();
                    stock.setExchangeRate(s.getPrice(), s.getUnit(), publicationDate);
                    stockRepository.save(stock);
                } else {
                    Stock stock = StockMapper.INSTANCE.externalStockDTOToStock(s);
                    stock.setExchangeRate(s.getPrice(), s.getUnit(), publicationDate);
                    stockRepository.save(stock);
                }
            });
        }
    }

    @Override
    public void handleExchangeRateError(ExchangeRateErrorEvent event) {
        log.info("Event handler - Exchange rate error.");
        isExchangeRateActual = false;
    }

    @Override
    public List<StockDTO> getExchangeRate() {
        List<Stock> all = stockRepository.findAll();
        all.stream().forEach(s -> {
            Set<ExchangeRate> exchangeRates = s.getExchangeRates();
            exchangeRates.stream().sorted((e1, e2) -> e1.getPublicationDate().compareTo(e2.getPublicationDate()));
        });
        List<StockDTO> stockDTOS = StockMapper.INSTANCE.mapListToDTO(all);

        return stockDTOS;
    }

    @Override
    public List<StockDTO> getPublicStocks() {
        List<Stock> all = stockRepository.findAll();
        all.stream().forEach(s -> {
            Set<ExchangeRate> exchangeRates = s.getExchangeRates();
            exchangeRates.stream().sorted((e1, e2) -> e1.getPublicationDate().compareTo(e2.getPublicationDate()));
        });

        List<StockDTO> stockDTOS = StockMapper.INSTANCE.mapListToDTO(all);

        stockDTOS.stream().forEach(s -> s.clearNonPublicData());

        return stockDTOS;
    }

    @Override
    @Transactional
    public boolean buyStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        if(!isExchangeRateActual){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
        }

        ValidatorResult purchase = operationValidator.validatePurchase(principal, stockDTO);

        //TODO: Test - co jeśli w tym samym czasie ktoś sprzeda akcje?

        changeAmountOfStock(purchase.getStock().getId(), stockDTO.getAmount(), true);

        BigDecimal restUserMoney = purchase.getUser().getFinancialResources().subtract(purchase.getStockPrice());
        purchase.getUser().setFinancialResources(restUserMoney);
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

    @Override
    @Transactional
    public boolean sellStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        if(!isExchangeRateActual){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
        }

        ValidatorResult sale = operationValidator.validateSale(principal, stockDTO);

        changeAmountOfStock(sale.getStock().getId(), stockDTO.getAmount(), false);

        sale.getUser().getStocks().stream().forEach(s -> {
            if(s.getStockId() == sale.getStock().getId()){
                int amount = s.getAmount();
                s.setAmount(amount - stockDTO.getAmount());
                userStockRepository.save(s);
            }
        });

        return true;
    }

    @Transactional
    private void changeAmountOfStock(Long stockId, int change, boolean isPurchase) {
        Stock stock = stockRepository.findOne(stockId);
        int amount = stock.getAmount();
        if(isPurchase) { //purchase
            stock.setAmount(amount - change);
        } else {        //sale
            stock.setAmount(amount + change);
        }
        stockRepository.save(stock);
        entityManager.flush();
    }

    @Override
    public Stock getExchangeRate(Long id) {
        Stock stock = stockRepository.findOne(id);
        return stock;
    }

    private Optional<UserStock> getUserStock(Long userId, Long stockId){
        return userStockRepository.findOneByUserIdAndStockId(userId, stockId);
    }

}
