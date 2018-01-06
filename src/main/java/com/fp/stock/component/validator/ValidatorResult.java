package com.fp.stock.component.validator;


import com.fp.stock.model.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ValidatorResult extends ValidatorBaseResult {

    private User user;

    private BigDecimal stockPrice;

    public ValidatorResult(ValidatorBaseResult result){
        this.stock = result.getStock();
        this.rate = result.getRate();
    }
}
