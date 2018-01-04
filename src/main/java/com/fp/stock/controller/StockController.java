package com.fp.stock.controller;

import com.fp.stock.component.StockList;
import com.fp.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final Logger log = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockList> getStocks(Principal user) {
        log.debug("REST request to get stocks");
        StockList exchangeRate = stockService.getExchangeRate();
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }
}
