package com.mvs.stockmanager.service;

import com.mvs.stockmanager.service.dto.StockDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mvs.stockmanager.domain.Stock}.
 */
public interface StockService {
    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save.
     * @return the persisted entity.
     */
    StockDTO save(StockDTO stockDTO);

    /**
     * Updates a stock.
     *
     * @param stockDTO the entity to update.
     * @return the persisted entity.
     */
    StockDTO update(StockDTO stockDTO);

    /**
     * Partially updates a stock.
     *
     * @param stockDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockDTO> partialUpdate(StockDTO stockDTO);

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stock.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockDTO> findOne(Long id);

    /**
     * Delete the "id" stock.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

     /**
     * Get all the total stocks grouped by article.
     *
     * @param pageable the pagination information.
     */
    Page<StockDTO> findTotalStocks(Pageable pageable);

    /**
     * Get one stock by article and store.
     *     
     * @param articleId the id of the article.
     * @param storeId the id of the store.
     * @return the entity.
     */
    public Optional<StockDTO> getByArticleAndStore(Long articleId, Long storeId);

}
