package com.fp.stock.service;

import com.fp.stock.dto.StockDTO;
import com.fp.stock.mapper.StockMapper;
import com.fp.stock.model.Stock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
public class StockServiceMapper {

    @Test
    public void shouldMapAllFieldsToStockDTO(){
        //Given
        Stock stock = new Stock();
        stock.setName("Test");
        stock.setCode("TS");
        stock.setUnit(10);
        stock.setPrice(BigDecimal.valueOf(10.25));
        stock.setPublicationDate(ZonedDateTime.now());

        //When
        StockDTO stockDTO = StockMapper.INSTANCE.stockToStockDTO(stock);


        //Then
        Assert.assertTrue(stockDTO.getName().equals(stock.getName()));
        Assert.assertTrue(stockDTO.getCode().equals(stock.getCode()));
        Assert.assertTrue(stockDTO.getUnit() == stock.getUnit());
        Assert.assertTrue(stockDTO.getPrice() == stock.getPrice());
    }
}
