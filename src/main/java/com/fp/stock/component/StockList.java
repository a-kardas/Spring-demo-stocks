package com.fp.stock.component;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fp.stock.model.Stock;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class StockList implements Serializable {

    private ZonedDateTime publicationDate;

    private List<Stock> items;

    public void setData(List<Stock> stock){
        this.items = stock;
        this.publicationDate = stock.stream().findAny().get().getPublicationDate();
    }
}
