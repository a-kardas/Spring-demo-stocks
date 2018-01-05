package com.fp.stock.repository;

import com.fp.stock.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {

    Optional<UserStock> findOneByUserIdAndStockId(Long userId, Long stockId);
}
