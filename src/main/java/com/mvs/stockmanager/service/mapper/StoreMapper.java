package com.mvs.stockmanager.service.mapper;

import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {}
