package com.fp.stock.component;


import com.fp.stock.model.Stock;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
public class StockList implements Serializable {

    private ZonedDateTime publicationDate; /*ZonedDateTime*/

    private List<Stock> items;
}
