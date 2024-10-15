package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.CropType;
import com.iql.policyadmin.service.dto.CropTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CropType} and its DTO {@link CropTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CropTypeMapper extends EntityMapper<CropTypeDTO, CropType> {}
