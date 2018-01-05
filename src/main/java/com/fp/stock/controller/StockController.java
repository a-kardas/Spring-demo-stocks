package com.fp.stock.controller;

import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.ExternalStockListDTO;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final Logger log = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> getStocks(Principal user) {
        log.debug("REST request to get stocks");
        List<StockDTO> exchangeRate = stockService.getExchangeRate();
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    @RequestMapping(value = "/buy",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> buyStocks(Principal user, @RequestBody StockDTO stockDTO) throws OperationsNotAllowedException {
        log.debug("REST request to buy stocks");
        boolean executed = stockService.buyStocks(user, stockDTO);
        return new ResponseEntity<>(executed, HttpStatus.OK);
    }

    @RequestMapping(value = "/sell",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sellStocks(Principal user, @RequestBody StockDTO stockDTO) throws OperationsNotAllowedException {
        log.debug("REST request to sell stocks");
        boolean executed = stockService.sellStocks(user, stockDTO);
        return new ResponseEntity<>(executed, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> getPublicStocks() {
        log.debug("REST request to get public stocks");
        List<StockDTO> publicStocks = stockService.getPublicStocks();
        return new ResponseEntity<>(publicStocks, HttpStatus.OK);
    }
}
