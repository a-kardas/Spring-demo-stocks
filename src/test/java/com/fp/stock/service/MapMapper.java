package com.fp.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.stock.dto.StockDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(value = JUnit4.class)
public class MapMapper {

    @Test
    public void shouldSerializeMap(){
        Map<StockDTO, Integer> stocks = new HashMap<>();
        stocks.put(new StockDTO(), 1);
        stocks.put(new StockDTO(), 2);
        ObjectMapper mapper = new ObjectMapper();
        String value= "";
        try {
            value = mapper.writeValueAsString(stocks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
