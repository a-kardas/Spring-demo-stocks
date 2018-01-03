package com.fp.stock.service.impl;

import com.fp.stock.component.ExchangeRateDownloadedEvent;
import com.fp.stock.component.StockList;
import com.fp.stock.model.Stock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private StockRepository stockRepository;

    @Async
    @EventListener
    @Override
    public void saveExchangeRate(ExchangeRateDownloadedEvent<StockList> stockList){
        log.info("Event handler - Exchange rate downloaded.");

        if(!stockList.getData().getItems().isEmpty()){

            stockList.getData().getItems().stream().forEach(s -> {
                Optional<Stock> existingStock = stockRepository.findByNameAndCode(s.getName(), s.getCode()); //check if same exists
                if(existingStock.isPresent()){
                    Stock stock = existingStock.get();
                    stock.setExchangeRate(s);
                } else {
                    stockRepository.save(s);
                }
            });
        }

        List<Stock> all = stockRepository.findAll();
    }

    @Override
    public StockList getExchangeRate() {
        return null;
    }

    @Override
    public Stock getCurrentExchangeRate(Long id) {
        return null;
    }

}
