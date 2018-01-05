package com.fp.stock.mapper;


import com.fp.stock.model.ExchangeRate;

import java.util.Iterator;
import java.util.Set;

public interface BasicMapper {

    default ExchangeRate map(Set<ExchangeRate> exchangeRates) {
        if(exchangeRates != null && !exchangeRates.isEmpty()){

            ExchangeRate rate = new ExchangeRate();

            Iterator<ExchangeRate> iterator = exchangeRates.iterator();
            rate = iterator.next();
            //We want last, the most up-to-date rate
            while(iterator.hasNext()){
                rate = iterator.next();
            }
            return map(rate);
        }

        return null;
    }

    ExchangeRate map(ExchangeRate source);
}
