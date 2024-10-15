package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.Business;
import com.iql.policyadmin.domain.BusinessType;
import com.iql.policyadmin.service.dto.BusinessDTO;
import com.iql.policyadmin.service.dto.BusinessTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Business} and its DTO {@link BusinessDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessMapper extends EntityMapper<BusinessDTO, Business> {
    @Mapping(target = "businessType", source = "businessType", qualifiedByName = "businessTypeName")
    BusinessDTO toDto(Business s);

    @Named("businessTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BusinessTypeDTO toDtoBusinessTypeName(BusinessType businessType);
}
