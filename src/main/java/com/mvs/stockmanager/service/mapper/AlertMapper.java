package com.mvs.stockmanager.service.mapper;

import com.mvs.stockmanager.domain.Alert;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.service.dto.AlertDTO;
import com.mvs.stockmanager.service.dto.StockDTO;
import com.mvs.stockmanager.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "stock", source = "stock")
    AlertDTO toDto(Alert s);

    @Named("storeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoreDTO toDtoStoreId(Store store);

    @Named("stockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StockDTO toDtoStockId(Stock stock);
}
