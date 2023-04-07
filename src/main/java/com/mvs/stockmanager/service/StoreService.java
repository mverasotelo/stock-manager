package com.mvs.stockmanager.service;

import com.mvs.stockmanager.service.dto.StoreDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mvs.stockmanager.domain.Store}.
 */
public interface StoreService {
    /**
     * Save a store.
     *
     * @param storeDTO the entity to save.
     * @return the persisted entity.
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     * Updates a store.
     *
     * @param storeDTO the entity to update.
     * @return the persisted entity.
     */
    StoreDTO update(StoreDTO storeDTO);

    /**
     * Partially updates a store.
     *
     * @param storeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StoreDTO> partialUpdate(StoreDTO storeDTO);

    /**
     * Get all the stores.
     *
     * @return the list of entities.
     */
    List<StoreDTO> findAll();

    /**
     * Get all the stores except fot the one passed by parameter.
     *
     * @param id the id.
     * @return the list of entities.
     */
    List<StoreDTO> findAllByIdNot(Long id);

    /**
     * Get the "id" store.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StoreDTO> findOne(Long id);

    /**
     * Delete the "id" store.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


}
