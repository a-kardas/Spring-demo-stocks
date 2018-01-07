package com.fp.stock.service;

import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.dto.ExternalStockDTO;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import com.fp.stock.repository.StockRepository;
import com.fp.stock.service.util.MockStockUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StockServiceTest {

    private final int STOCK_SIZE = 3;
    private final long STOCK_ID = 1;
    private final BigDecimal STOCK_NEW_PRICE = new BigDecimal(15).setScale(4);

    @Autowired
    private StockService stockService;


    @Autowired
    private StockRepository stockRepository;


    @Test
    public void shouldSaveExchangeRateAndFindAll() {
        //Given
        List<ExternalStockDTO> stocks = MockStockUtil.getExternalStockList();
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
        List<ExternalStockDTO> stocks = MockStockUtil.getExternalStockList();
        prepareStockEventAndSave(stocks);
        ExternalStockDTO stockUnderTest = stocks.get(0);

        //When
        Stock stock = stockRepository.findOne(STOCK_ID);

        //Then
        Assert.assertTrue(stock.getName().equals(stockUnderTest.getName()));
        Assert.assertTrue(stock.getCode().equals(stockUnderTest.getCode()));

        ExchangeRate rate = stock.getExchangeRates().iterator().next();
        Assert.assertTrue(rate.getPrice() == stockUnderTest.getPrice());
        Assert.assertTrue(rate.getUnit() == stockUnderTest.getUnit());
    }

    @Test
    public void shouldUpdateInsteadOfInsert() {
        //Given
        List<ExternalStockDTO> stocks = MockStockUtil.getExternalStockList();
        prepareStockEventAndSave(stocks);
        List<Stock> newStocks = stockRepository.findAll();

        //When
        List<ExternalStockDTO> stocksToUpdate = MockStockUtil.getExternalStockList();
        stocksToUpdate.stream().forEach(s -> s.setPrice(STOCK_NEW_PRICE));
        prepareStockEventAndSave(stocksToUpdate);

        //Then
        List<Stock> updatedStocks = stockRepository.findAll();

        Assert.assertTrue(updatedStocks.size() == STOCK_SIZE);

        for(int i = 0; i < newStocks.size(); i++){
            Assert.assertTrue(newStocks.get(i).equals(updatedStocks.get(i)));
        }
    }

    @Test
    public void shouldReturnExchangeRates(){
        //Given
        List<ExternalStockDTO> stocks = MockStockUtil.getExternalStockList();
        prepareStockEventAndSave(stocks);

        //When
        List<StockDTO> exchangeRate = stockService.getExchangeRate();

        //Then
        Assert.assertTrue(exchangeRate.size() == STOCK_SIZE);

        for (StockDTO stock : exchangeRate) {
            Assert.assertTrue(stock.getAmount() != null);
            Assert.assertTrue(stock.getRate() != null);
        }
    }

    @Test
    public void shouldReturnPublicExchangeRates(){
        //Given
        List<ExternalStockDTO> stocks = MockStockUtil.getExternalStockList();
        prepareStockEventAndSave(stocks);

        //When
        List<StockDTO> exchangeRate = stockService.getPublicStocks();

        //Then
        Assert.assertTrue(exchangeRate.size() == STOCK_SIZE);

        for (StockDTO stock : exchangeRate) {
            Assert.assertTrue(stock.getAmount() == null);
            Assert.assertTrue(stock.getRate() == null);
        }
    }

    private void prepareStockEventAndSave(List<ExternalStockDTO> stocks){
        ZonedDateTime publicationDate = ZonedDateTime.now();

        ExternalStockListDTO externalStockListDTO = new ExternalStockListDTO();
        externalStockListDTO.setData(stocks);
        externalStockListDTO.setPublicationDate(publicationDate);

        ExchangeRateDownloadedEvent<ExternalStockListDTO> event = new ExchangeRateDownloadedEvent<>(externalStockListDTO);
        stockService.handleDownloadedExchangeRate(event);
    }
}
