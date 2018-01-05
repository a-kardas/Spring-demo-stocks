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
import java.util.HashSet;
import java.util.Set;


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

    @NotNull
    @Min(value = 0)
    @Column(name="amount", nullable = false)
    private int amount;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id")
    private Set<ExchangeRate> exchangeRates = new HashSet<>();

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Stock))
            return false;

        Stock comparedStock = (Stock) object;

        if(this.name.equals(comparedStock.getName()) && this.code.equals(comparedStock.getCode()))
            return true;


        return false;
    }

    public void setExchangeRate(BigDecimal price, int unit,  ZonedDateTime publicationDate){
        ExchangeRate rate = new ExchangeRate();
        rate.setPrice(price);
        rate.setUnit(unit);
        rate.setPublicationDate(publicationDate);

        this.exchangeRates.add(rate);
    }

}
