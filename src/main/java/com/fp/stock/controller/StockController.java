package com.fp.stock.controller;

import com.fp.stock.component.operations.DeferredStackOperation;
import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.User;
import com.fp.stock.service.StockService;
import com.fp.stock.service.UserService;
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

    @Autowired
    private UserService userService;

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
    public DeferredStackOperation buyStocks(Principal principal, @RequestBody StockDTO stockDTO) throws Exception {
        log.debug("REST request to buy stocks");
        User user = userService.findUserOrThrow(principal);
        return stockService.buyStocks(user, stockDTO);
    }

    @RequestMapping(value = "/sell",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredStackOperation sellStocks(Principal principal, @RequestBody StockDTO stockDTO) throws Exception {
        log.debug("REST request to sell stocks");
        User user = userService.findUserOrThrow(principal);
        return stockService.sellStocks(user, stockDTO);
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
