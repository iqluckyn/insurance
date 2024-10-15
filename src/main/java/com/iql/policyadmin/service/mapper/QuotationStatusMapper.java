package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.QuotationStatus;
import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuotationStatus} and its DTO {@link QuotationStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationStatusMapper extends EntityMapper<QuotationStatusDTO, QuotationStatus> {}
