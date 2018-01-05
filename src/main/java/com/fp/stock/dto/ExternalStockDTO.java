package com.fp.stock.dto;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExternalStockDTO implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private int unit;

    @NotNull
    private BigDecimal price;



}


