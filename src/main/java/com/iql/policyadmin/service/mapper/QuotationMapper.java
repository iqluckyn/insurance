package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.Business;
import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.domain.Product;
import com.iql.policyadmin.domain.Quotation;
import com.iql.policyadmin.domain.QuotationStatus;
import com.iql.policyadmin.domain.Season;
import com.iql.policyadmin.service.dto.BusinessDTO;
import com.iql.policyadmin.service.dto.FarmerDTO;
import com.iql.policyadmin.service.dto.ProductDTO;
import com.iql.policyadmin.service.dto.QuotationDTO;
import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import com.iql.policyadmin.service.dto.SeasonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quotation} and its DTO {@link QuotationDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationMapper extends EntityMapper<QuotationDTO, Quotation> {
    @Mapping(target = "season", source = "season", qualifiedByName = "seasonSeasonName")
    @Mapping(target = "farmer", source = "farmer", qualifiedByName = "farmerFirstname")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "business", source = "business", qualifiedByName = "businessId")
    @Mapping(target = "quotationStatus", source = "quotationStatus", qualifiedByName = "quotationStatusStatusCode")
    QuotationDTO toDto(Quotation s);

    @Named("seasonSeasonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "seasonName", source = "seasonName")
    SeasonDTO toDtoSeasonSeasonName(Season season);

    @Named("farmerFirstname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstname", source = "firstname")
    FarmerDTO toDtoFarmerFirstname(Farmer farmer);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("businessId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BusinessDTO toDtoBusinessId(Business business);

    @Named("quotationStatusStatusCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statusCode", source = "statusCode")
    QuotationStatusDTO toDtoQuotationStatusStatusCode(QuotationStatus quotationStatus);
}
