package com.fp.stock.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
@Entity
@Table(name = "STOCK")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name="name", nullable = false)
    private String name;

    @NotBlank
    @Column(name="code", nullable = false)
    private String code;

    @Min(value = 1)
    @Column(name="unit", nullable = false)
    private int unit;

    @NotNull
    @Column(name="price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name="publicationDate", nullable = false)
    private ZonedDateTime publicationDate;

    @NotNull
    @Column(name="amount", nullable = false)
    private int amount;

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Stock))
            return false;

        Stock comparedStock = (Stock) object;

        if(this.name.equals(comparedStock.getName()) && this.code.equals(comparedStock.getCode()))
            return true;


        return false;
    }

    public void setExchangeRate(Stock stock, ZonedDateTime publicationDate){
        this.unit = stock.getUnit();
        this.price = stock.getPrice();
        this.publicationDate = publicationDate;
    }

}
