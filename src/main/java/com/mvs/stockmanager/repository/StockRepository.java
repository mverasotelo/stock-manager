package com.mvs.stockmanager.repository;

import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.service.criteria.StockCriteria;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
    
    @Query("SELECT s.id, s.article, SUM(s.actualStock) as actualStock FROM Stock s GROUP BY s.id, s.article")
    Page<Stock> findTotalStocksByArticle(Pageable page);

    Boolean existsByArticleIdAndStoreId(Long articleId, Long storeId);

    Optional<Stock> getByArticleIdAndStoreId(Long articleId, Long storeId);
}
