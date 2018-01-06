package com.fp.stock.component.validator;

import com.fp.stock.model.ExchangeRate;
import com.fp.stock.model.Stock;
import lombok.Data;


@Data
public class ValidatorBaseResult {

    protected Stock stock;

    protected ExchangeRate rate;
}
