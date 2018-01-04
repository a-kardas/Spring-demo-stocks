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

import java.time.ZonedDateTime;
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
            ZonedDateTime publicationDate = stockList.getData().getPublicationDate();
            stockList.getData().getItems().stream().forEach(s -> {
                Optional<Stock> existingStock = stockRepository.findByNameAndCode(s.getName(), s.getCode()); //check if same exists
                if(existingStock.isPresent()){
                    Stock stock = existingStock.get();
                    stock.setExchangeRate(s, publicationDate);
                    stockRepository.save(stock);
                } else {
                    s.setPublicationDate(publicationDate);
                    stockRepository.save(s);
                }
            });
        }
    }

    @Override
    public StockList getExchangeRate() {
        List<Stock> all = stockRepository.findAll();
        StockList result = new StockList();

        if(all != null && !all.isEmpty())
            result.setData(all);

        return result;
    }

    @Override
    public Stock getExchangeRate(Long id) {
        Stock stock = stockRepository.findOne(id);
        return stock;
    }

}
