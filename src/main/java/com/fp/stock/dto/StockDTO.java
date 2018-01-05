package com.fp.stock.dto;


import com.fp.stock.model.ExchangeRate;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class StockDTO implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Integer amount;

    private ExchangeRate rate;

    public void clearNonPublicData(){
        this.amount = null;
        this.rate = null;
    }
}
