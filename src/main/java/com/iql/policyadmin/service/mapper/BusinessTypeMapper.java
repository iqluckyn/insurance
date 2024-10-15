package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.BusinessType;
import com.iql.policyadmin.service.dto.BusinessTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessType} and its DTO {@link BusinessTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessTypeMapper extends EntityMapper<BusinessTypeDTO, BusinessType> {}
