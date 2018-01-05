package com.fp.stock.component.events;


import com.fp.stock.dto.ExternalStockListDTO;
import lombok.Getter;

@Getter
public class ExchangeRateDownloadedEvent<T extends ExternalStockListDTO>{

    private T data;

    public ExchangeRateDownloadedEvent(T data){
        this.data = data;
    }
}
