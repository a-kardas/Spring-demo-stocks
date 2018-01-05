package com.fp.stock.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ExternalStockListDTO implements Serializable {

    private ZonedDateTime publicationDate;

    private List<ExternalStockDTO> items;

    public void setData(List<ExternalStockDTO> stock){
        this.items = stock;
    }
}
