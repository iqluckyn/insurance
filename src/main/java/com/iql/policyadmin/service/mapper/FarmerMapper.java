package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.Business;
import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.domain.User;
import com.iql.policyadmin.service.dto.BusinessDTO;
import com.iql.policyadmin.service.dto.FarmerDTO;
import com.iql.policyadmin.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Farmer} and its DTO {@link FarmerDTO}.
 */
@Mapper(componentModel = "spring")
public interface FarmerMapper extends EntityMapper<FarmerDTO, Farmer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "business", source = "business", qualifiedByName = "businessRegisteredName")
    FarmerDTO toDto(Farmer s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("businessRegisteredName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "registeredName", source = "registeredName")
    BusinessDTO toDtoBusinessRegisteredName(Business business);
}
