package com.fp.stock.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class StockDTO implements Serializable {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @Min(value = 1)
    private int unit;

    @NotNull
    private BigDecimal price;
}
