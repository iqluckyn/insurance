package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.CropType;
import com.iql.policyadmin.domain.Farm;
import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.service.dto.CropTypeDTO;
import com.iql.policyadmin.service.dto.FarmDTO;
import com.iql.policyadmin.service.dto.FarmerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Farm} and its DTO {@link FarmDTO}.
 */
@Mapper(componentModel = "spring")
public interface FarmMapper extends EntityMapper<FarmDTO, Farm> {
    @Mapping(target = "cropType", source = "cropType", qualifiedByName = "cropTypeCropName")
    @Mapping(target = "farmer", source = "farmer", qualifiedByName = "farmerId")
    FarmDTO toDto(Farm s);

    @Named("cropTypeCropName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cropName", source = "cropName")
    CropTypeDTO toDtoCropTypeCropName(CropType cropType);

    @Named("farmerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FarmerDTO toDtoFarmerId(Farmer farmer);
}
