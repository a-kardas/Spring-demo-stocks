package com.fp.stock.mapper;

import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class );

    StockDTO stockToStockDTO(Stock stock);
}
