package com.fp.stock.component;

import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import com.fp.stock.repository.ExchangeRateRepository;
import com.fp.stock.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("default")
public class DataCleaner {

    private final Logger log = LoggerFactory.getLogger(DataCleaner.class);

    private final int MAX_RATES_SIZE = 20;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Scheduled(cron = "0 0/5 * * * *") //every 5 minutes
    public void removeOldData ()  {
        log.info("Scheduled job - cleaning old data");

        List<Stock> stocks = stockRepository.findAll();

        stocks.stream().forEach(s -> {
            long howManyRates = exchangeRateRepository.countByStockId(s.getId());

            if(howManyRates > MAX_RATES_SIZE) {
                log.info(String.format("Scheduled job - cleaning old data - stock %s exceeded MAX_RATES_SIZE", s.getCode()));
                //REMOVE OLD DATA
                List<ExchangeRate> rates = exchangeRateRepository.findAllByStockIdOrderByCreationDateDesc(s.getId());
                List<ExchangeRate> toRemove = rates.subList(MAX_RATES_SIZE, rates.size());
                exchangeRateRepository.delete(toRemove);
            }
        });
    }
}
