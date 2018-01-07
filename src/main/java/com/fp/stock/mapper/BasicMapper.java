package com.fp.stock.mapper;


import com.fp.stock.model.ExchangeRate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface BasicMapper {

    int MAX_SIZE = 20;

    default ExchangeRate map(Set<ExchangeRate> exchangeRates) {
        if(exchangeRates != null && !exchangeRates.isEmpty()){

            ExchangeRate rate = new ExchangeRate();

            Iterator<ExchangeRate> iterator = exchangeRates.iterator();
            rate = iterator.next();
            return map(rate);
        }

        return null;
    }

    default List<ExchangeRate> mapHistoricalData(Set<ExchangeRate> exchangeRates) {
        if(exchangeRates != null && !exchangeRates.isEmpty()){

            List<ExchangeRate> rates = new ArrayList<>(exchangeRates);
            int maxElement = rates.size() > MAX_SIZE ? MAX_SIZE : rates.size();
            return map(rates.subList(0, maxElement));
        }

        return null;
    }

    ExchangeRate map(ExchangeRate source);

    List<ExchangeRate> map(List<ExchangeRate> rates);
}
