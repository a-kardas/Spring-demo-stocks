package com.fp.stock.component.validator;


import com.fp.stock.config.OperationsNotAllowedException;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import com.fp.stock.repository.ExchangeRateRepository;
import com.fp.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class OperationBaseValidator {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    protected ValidatorBaseResult validate(StockDTO stockDTO) throws OperationsNotAllowedException {
        //VALIDATE STOCK
        Stock stock = getStockOrThrow(stockDTO);

        if(stock.getAmount() < stockDTO.getAmount()) {
            throw new OperationsNotAllowedException(OperationsNotAllowedException.NOT_ENOUGH_UNITS_ON_STOCK);
        }

        //VALIDATE RATES
        List<ExchangeRate> rates = getRatesOrThrow(stock.getId(), stockDTO);
        ExchangeRate rate = rates.stream().findFirst().get();

        if(stockDTO.getAmount() % rate.getUnit() != 0) {
            throw new OperationsNotAllowedException(OperationsNotAllowedException.AMOUNT_IS_NOT_MULTIPLE);
        }

        ValidatorBaseResult result = new ValidatorBaseResult();
        result.setStock(stock);
        result.setRate(rate);

        return result;
    }

    protected Stock getStockOrThrow(StockDTO stockDTO) throws OperationsNotAllowedException {
        Optional<Stock> stock = stockRepository.findByNameAndCode(stockDTO.getName(), stockDTO.getCode());
        if(stock.isPresent()){
            /*Set<ExchangeRate> sorted = stock.get().getExchangeRates().stream()
                    .sorted((e1, e2) -> e1.getPublicationDate().compareTo(e2.getPublicationDate()))
                    .collect(Collectors.toSet());

            stock.get().setExchangeRates(sorted);*/
            return stock.get();
        }

        throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
    }

    protected List<ExchangeRate> getRatesOrThrow(Long stockId, StockDTO stockDTO) throws OperationsNotAllowedException {

        List<ExchangeRate> rates = exchangeRateRepository.findAllByStockIdAndUnitAndPrice(stockId,
                stockDTO.getRate().getUnit(),
                stockDTO.getRate().getPrice());

        if(rates == null || rates.isEmpty()){
            throw new OperationsNotAllowedException(OperationsNotAllowedException.DEFAULT);
        }

        return rates;
    }
}
