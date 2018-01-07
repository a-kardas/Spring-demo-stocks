package com.fp.stock.dto;


import com.fp.stock.model.ExchangeRate;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@Data
public class StockDTO implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Integer amount;

    private ExchangeRate rate;

    private List<ExchangeRate> historicalData;

    public void clearNonPublicData(){
        this.amount = null;
        this.rate = null;
        this.historicalData = null;
    }
}
