package com.mvs.stockmanager.service;

import com.mvs.stockmanager.service.dto.ActionDTO;
import com.mvs.stockmanager.service.dto.AlertDTO;
import com.mvs.stockmanager.service.dto.StockDTO;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mvs.stockmanager.domain.Alert}.
 */
public interface AlertService {
    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    AlertDTO save(AlertDTO alertDTO);

    /**
     * Updates a alert.
     *
     * @param alertDTO the entity to update.
     * @return the persisted entity.
     */
    AlertDTO update(AlertDTO alertDTO);

    /**
     * Partially updates a alert.
     *
     * @param alertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlertDTO> partialUpdate(AlertDTO alertDTO);

    /**
     * Get all the alerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlertDTO> findAll(Pageable pageable);

        /**
     * Get all the active alerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlertDTO> findAllActive(Pageable pageable);

    /**
     * Get the "id" alert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertDTO> findOne(Long id);

    /**
     * Delete the "id" alert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Creates a alert according to a stock.
     *
     * @param stockDTO the stock to create an alert about.
     * @return the persisted entity.
     */
    public AlertDTO createAlert(StockDTO stockDTO);

    /**
     * Rectificates an alert according to a stock.
     *
     * @param stockDTO the stock to rectificate an alert about.
     * @return the rectificated entity.
     */
    public AlertDTO rectificateAlert(StockDTO stockDTO);

}
