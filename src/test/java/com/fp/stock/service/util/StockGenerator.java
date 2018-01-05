package com.fp.stock.service.util;


import com.fp.stock.model.Stock;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public final class StockGenerator {

    public static Stock getStock(){
        Stock stock1 = new Stock();
        stock1.setName("Test 1");
        stock1.setCode("TS1");
       /* stock1.setPrice(BigDecimal.valueOf(10.25));
        stock1.setUnit(10);*/

        return stock1;
    }

    public static List<Stock> getStockList(){
        Stock stock1 = new Stock();
        stock1.setName("Test 1");
        stock1.setCode("TS1");
       /* stock1.setPrice(BigDecimal.valueOf(10.25));
        stock1.setUnit(10);*/

        Stock stock2 = new Stock();
        stock2.setName("Test 2");
        stock2.setCode("TS2");
        /*stock2.setPrice(BigDecimal.valueOf(20.50));
        stock2.setUnit(15);*/

        Stock stock3 = new Stock();
        stock3.setName("Test 3");
        stock3.setCode("TS3");
/*        stock3.setPrice(BigDecimal.valueOf(5.50));
        stock3.setUnit(5);*/

        List<Stock> result = Arrays.asList(stock1, stock2, stock3);
        return result;
    }
}
