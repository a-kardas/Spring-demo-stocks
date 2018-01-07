package com.fp.stock.service;


import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.component.operations.DeferredStackOperation;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.Stock;
import com.fp.stock.model.User;

import java.security.Principal;
import java.util.List;

public interface StockService {

    void handleDownloadedExchangeRate(ExchangeRateDownloadedEvent<ExternalStockListDTO> stockList);

    List<StockDTO> getExchangeRate();

    List<StockDTO> getPublicStocks();

    DeferredStackOperation buyStocks(User user, StockDTO stockDTO) throws OperationsNotAllowedException;

    DeferredStackOperation sellStocks(User user, StockDTO stockDTO) throws OperationsNotAllowedException;

    Stock getExchangeRate(Long id);
}
