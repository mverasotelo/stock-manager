package com.mvs.stockmanager.repository;

import com.mvs.stockmanager.domain.Alert;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

    public Page<Alert> findAllByRectificationDatetimeNull(Pageable pageable);

    public Optional<Alert> getOneByStockIdAndRectificationDatetimeNull(Long id);

}
