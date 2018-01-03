package com.fp.stock.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@Entity
@Table(name = "STOCK")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @Min(value = 1)
    private int unit;

    @NotNull
    private BigDecimal price;

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Stock))
            return false;

        Stock comparedStock = (Stock) object;

        if(this.name.equals(comparedStock.getName()) && this.code.equals(comparedStock.getCode()))
            return true;


        return false;
    }

    public void setExchangeRate(Stock stock){
        this.unit = stock.getUnit();
        this.price = stock.getPrice();
    }

}
