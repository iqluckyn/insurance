package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.CropType;
import com.iql.policyadmin.domain.Season;
import com.iql.policyadmin.service.dto.CropTypeDTO;
import com.iql.policyadmin.service.dto.SeasonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Season} and its DTO {@link SeasonDTO}.
 */
@Mapper(componentModel = "spring")
public interface SeasonMapper extends EntityMapper<SeasonDTO, Season> {
    @Mapping(target = "cropType", source = "cropType", qualifiedByName = "cropTypeId")
    SeasonDTO toDto(Season s);

    @Named("cropTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CropTypeDTO toDtoCropTypeId(CropType cropType);
}
