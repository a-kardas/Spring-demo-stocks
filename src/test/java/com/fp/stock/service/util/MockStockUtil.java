package com.fp.stock.service.util;


import com.fp.stock.dto.ExternalStockDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MockStockUtil {

    private static List<ExternalStockDTO> externalStocks = new ArrayList<>();


    public static ExternalStockDTO getStock(){

        if(externalStocks.isEmpty()){
            getExternalStockList();
        }

        return externalStocks.get(0);

    }

    public static List<ExternalStockDTO> getExternalStockList(){

        if(!externalStocks.isEmpty()){

            return externalStocks;
        } else {

            ExternalStockDTO stock1 = new ExternalStockDTO();
            stock1.setName("Test 1");
            stock1.setCode("TS1");
            stock1.setUnit(10);
            stock1.setPrice(BigDecimal.valueOf(10.3456));

            ExternalStockDTO stock2 = new ExternalStockDTO();
            stock2.setName("Test 2");
            stock2.setCode("TS2");
            stock2.setUnit(5);
            stock2.setPrice(BigDecimal.valueOf(6.0025));

            ExternalStockDTO stock3 = new ExternalStockDTO();
            stock3.setName("Test 3");
            stock3.setCode("TS3");
            stock3.setUnit(25);
            stock3.setPrice(BigDecimal.valueOf(25.5000));

            externalStocks = Arrays.asList(stock1, stock2, stock3);
            return externalStocks;
        }
    }

}
