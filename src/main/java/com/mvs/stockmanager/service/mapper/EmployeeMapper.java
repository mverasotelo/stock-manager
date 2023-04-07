package com.mvs.stockmanager.service.mapper;

import com.mvs.stockmanager.domain.Employee;
import com.mvs.stockmanager.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
