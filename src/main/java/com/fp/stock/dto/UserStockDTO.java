package com.fp.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserStockDTO implements Serializable {

    private Long stockId;

    private int amount;

    private StockDTO stock;
}
