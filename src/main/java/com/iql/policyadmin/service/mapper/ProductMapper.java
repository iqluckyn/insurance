package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.Product;
import com.iql.policyadmin.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
