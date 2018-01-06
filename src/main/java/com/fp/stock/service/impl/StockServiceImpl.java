package com.fp.stock.service.impl;

import com.fp.stock.component.StackOperationComponent;
import com.fp.stock.component.operations.OperationType;
import com.fp.stock.component.operations.DeferredStackOperation;
import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.dto.ExternalStockListDTO;
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

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StackOperationComponent stackOperationComponent;

    @Async
    @EventListener
    @Override
    public void handleDownloadedExchangeRate(ExchangeRateDownloadedEvent<ExternalStockListDTO> stockList){
        log.info("Event handler - Exchange rate downloaded.");

        if(!stockList.getData().getItems().isEmpty()){
            ZonedDateTime publicationDate = stockList.getData().getPublicationDate();

            log.debug("Event handler - get rates with publication date " + publicationDate.toString());

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
    public List<StockDTO> getExchangeRate() {
        List<Stock> all = stockRepository.findAll();
        List<StockDTO> stockDTOS = StockMapper.INSTANCE.mapListToDTO(all);

        return stockDTOS;
    }

    @Override
    public List<StockDTO> getPublicStocks() {
        List<Stock> all = stockRepository.findAll();
        List<StockDTO> stockDTOS = StockMapper.INSTANCE.mapListToDTO(all);

        stockDTOS.stream().forEach(s -> s.clearNonPublicData());

        return stockDTOS;
    }

    @Override
    public DeferredStackOperation buyStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        DeferredStackOperation operation = new DeferredStackOperation();
        operation.setPrincipal(principal);
        operation.setStockDTO(stockDTO);
        operation.setType(OperationType.PURCHASE);

        stackOperationComponent.addOperation(operation);
        return operation;
    }

    @Override
    public DeferredStackOperation sellStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException {
        DeferredStackOperation operation = new DeferredStackOperation();
        operation.setPrincipal(principal);
        operation.setStockDTO(stockDTO);
        operation.setType(OperationType.SALE);

        stackOperationComponent.addOperation(operation);
        return operation;
    }

    @Override
    public Stock getExchangeRate(Long id) {
        Stock stock = stockRepository.findOne(id);
        return stock;
    }

}
