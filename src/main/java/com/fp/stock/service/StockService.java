package com.fp.stock.service;


import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.component.events.ExchangeRateErrorEvent;
import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.Stock;

import java.security.Principal;
import java.util.List;

public interface StockService {

    void handleDownloadedExchangeRate(ExchangeRateDownloadedEvent<ExternalStockListDTO> stockList);

    void handleExchangeRateError(ExchangeRateErrorEvent event);

    List<StockDTO> getExchangeRate();

    List<StockDTO> getPublicStocks();

    boolean buyStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException;

    boolean sellStocks(Principal principal, StockDTO stockDTO) throws OperationsNotAllowedException;

    Stock getExchangeRate(Long id);
}
