package com.fp.stock.repository;

import com.fp.stock.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findAllByStockIdAndUnitAndPrice(Long stockId, int unit, BigDecimal price);

    long countByStockId(Long stockId);

    List<ExchangeRate> findAllByStockIdOrderByCreationDateDesc(Long stockId);
}
