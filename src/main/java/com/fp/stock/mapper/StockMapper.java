package com.fp.stock.mapper;

import com.fp.stock.dto.ExternalStockDTO;
import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StockMapper extends BasicMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class );

    @Mapping(source = "exchangeRates", target = "rate")
    StockDTO stockToStockDTO(Stock stock);

    Stock externalStockDTOToStock(ExternalStockDTO stockDTO);

    List<StockDTO> mapListToDTO(List<Stock> stocks);


}
