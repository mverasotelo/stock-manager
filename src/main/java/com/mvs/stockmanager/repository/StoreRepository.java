package com.mvs.stockmanager.repository;

import com.mvs.stockmanager.domain.Store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Store entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Boolean existsByCodeIgnoreCase(String code);

    Boolean existsByCodeAndIdNot(String code, Long id);

    List<Store> findByIdNot(Long id);

}
