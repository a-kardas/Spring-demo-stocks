package com.fp.stock.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "STOCK_EXCHANGE_RATE")
@Data
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="stock_id", insertable = false, updatable = false)
    private Long stockId;

    @NotNull
    @Column(name="price", nullable = false, precision = 10, scale = 4)
    private BigDecimal price;

    @Min(value = 1)
    @Column(name="unit", nullable = false)
    private int unit;

    @NotNull
    @Column(name="creation_date", nullable = false)
    private ZonedDateTime creationDate = ZonedDateTime.now();

    @NotNull
    @Column(name="publicationDate", nullable = false)
    private ZonedDateTime publicationDate;

}
