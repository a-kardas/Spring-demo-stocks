package com.fp.stock.service;


import com.fp.stock.component.ExchangeRateDownloadedEvent;
import com.fp.stock.component.StockList;
import com.fp.stock.model.Stock;

public interface StockService {

    void saveExchangeRate(ExchangeRateDownloadedEvent<StockList> stockList);

    StockList getExchangeRate();

    StockList getPublicStocks();

    Stock getExchangeRate(Long id);
}
