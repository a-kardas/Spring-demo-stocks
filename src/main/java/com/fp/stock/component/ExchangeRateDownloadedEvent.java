package com.fp.stock.component;


import lombok.Getter;

@Getter
public class ExchangeRateDownloadedEvent<T extends StockList>{

    private T data;

    public ExchangeRateDownloadedEvent(T data){
        this.data = data;
    }
}
