package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.PComponent;
import com.iql.policyadmin.service.dto.PComponentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PComponent} and its DTO {@link PComponentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PComponentMapper extends EntityMapper<PComponentDTO, PComponent> {}
