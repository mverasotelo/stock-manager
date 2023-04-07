package com.mvs.stockmanager.service.mapper;

import com.mvs.stockmanager.domain.Action;
import com.mvs.stockmanager.domain.Employee;
import com.mvs.stockmanager.domain.Stock;
import com.mvs.stockmanager.domain.Store;
import com.mvs.stockmanager.service.dto.ActionDTO;
import com.mvs.stockmanager.service.dto.EmployeeDTO;
import com.mvs.stockmanager.service.dto.StockDTO;
import com.mvs.stockmanager.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Action} and its DTO {@link ActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActionMapper extends EntityMapper<ActionDTO, Action> {
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "store", source = "store")
    ActionDTO toDto(Action s);

//     @Named("employeeId")
//     @BeanMapping(ignoreByDefault = true)
//     @Mapping(target = "id", source = "id")
//     EmployeeDTO toDtoEmployeeId(Employee employee);

//     @Named("stockId")
//     @BeanMapping(ignoreByDefault = true)
//     @Mapping(target = "id", source = "id")
//     StockDTO toDtoStockId(Stock stock);

//     @Named("storeId")
//     @BeanMapping(ignoreByDefault = true)
//     @Mapping(target = "id", source = "id")
//     StoreDTO toDtoStoreId(Store store);
}
