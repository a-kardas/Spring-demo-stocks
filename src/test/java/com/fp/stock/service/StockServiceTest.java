package com.fp.stock.service;

import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.model.Stock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.service.util.StockGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {

    private final int STOCK_SIZE = 3;
    private final long STOCK_ID = 1;
    private final BigDecimal STOCK_NEW_PRICE = new BigDecimal(15).setScale(2);

    @Autowired
    private StockService stockService;


    @Autowired
    private StockRepository stockRepository;


    /*@Test
    public void shouldSaveExchangeRateAndFindAll() {
        //Given
        List<Stock> stocks = StockGenerator.getStockList();
        ZonedDateTime publicationDate = ZonedDateTime.now();

        ExternalStockListDTO externalStockListDTO = new ExternalStockListDTO();
        externalStockListDTO.setData(stocks);
        externalStockListDTO.setPublicationDate(publicationDate);

        ExchangeRateDownloadedEvent<ExternalStockListDTO> event = new ExchangeRateDownloadedEvent<>(externalStockListDTO);

        //When
        stockService.handleDownloadedExchangeRate(event);

        //Then
        List<Stock> all = stockRepository.findAll();
        Assert.assertTrue(all.size() == STOCK_SIZE);
    }

    @Test
    public void shouldFindOneById() {
        //Given
        List<Stock> stocks = StockGenerator.getStockList();
        prepareStockEventAndSave(stocks);
        Stock stockUnderTest = stocks.stream().findFirst().get();

        //When
        Stock stock = stockRepository.findOne(STOCK_ID);

        //Then
        Assert.assertTrue(stock.getName().equals(stockUnderTest.getName()));
        Assert.assertTrue(stock.getCode().equals(stockUnderTest.getCode()));
        Assert.assertTrue(stock.getPrice() == stockUnderTest.getPrice());
        Assert.assertTrue(stock.getUnit() == stockUnderTest.getUnit());
    }

    @Test
    public void shouldUpdateInsteadOfInsert() {
        //Given
        List<Stock> stocks = StockGenerator.getStockList();
        prepareStockEventAndSave(stocks);
        List<Stock> newStocks = stockRepository.findAll();

        //When
        List<Stock> stocksToUpdate = StockGenerator.getStockList();
        stocksToUpdate.stream().forEach(s -> s.setPrice(STOCK_NEW_PRICE));
        prepareStockEventAndSave(stocksToUpdate);

        //Then
        List<Stock> updatedStocks = stockRepository.findAll();

        for(int i = 0; i < newStocks.size(); i++){
            Assert.assertTrue(newStocks.get(i).equals(updatedStocks.get(i)));
            Assert.assertTrue(updatedStocks.get(i).getPrice().equals(STOCK_NEW_PRICE));
        }
    }

    private void prepareStockEventAndSave(List<Stock> stocks){
        ZonedDateTime publicationDate = ZonedDateTime.now();

        ExternalStockListDTO externalStockListDTO = new ExternalStockListDTO();
        externalStockListDTO.setData(stocks);
        externalStockListDTO.setPublicationDate(publicationDate);

        ExchangeRateDownloadedEvent<ExternalStockListDTO> event = new ExchangeRateDownloadedEvent<>(externalStockListDTO);
        stockService.handleDownloadedExchangeRate(event);
    }*/
}
