package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.Farm;
import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.domain.InsuredPolicy;
import com.iql.policyadmin.domain.PolicyComponent;
import com.iql.policyadmin.domain.Quotation;
import com.iql.policyadmin.service.dto.FarmDTO;
import com.iql.policyadmin.service.dto.FarmerDTO;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import com.iql.policyadmin.service.dto.PolicyComponentDTO;
import com.iql.policyadmin.service.dto.QuotationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InsuredPolicy} and its DTO {@link InsuredPolicyDTO}.
 */
@Mapper(componentModel = "spring")
public interface InsuredPolicyMapper extends EntityMapper<InsuredPolicyDTO, InsuredPolicy> {
    @Mapping(target = "insuredFarmer", source = "insuredFarmer", qualifiedByName = "farmerId")
    @Mapping(target = "farm", source = "farm", qualifiedByName = "farmId")
    @Mapping(target = "components", source = "components", qualifiedByName = "policyComponentIdSet")
    @Mapping(target = "quotation", source = "quotation", qualifiedByName = "quotationId")
    InsuredPolicyDTO toDto(InsuredPolicy s);

    @Mapping(target = "removeComponents", ignore = true)
    InsuredPolicy toEntity(InsuredPolicyDTO insuredPolicyDTO);

    @Named("farmerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FarmerDTO toDtoFarmerId(Farmer farmer);

    @Named("farmId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FarmDTO toDtoFarmId(Farm farm);

    @Named("policyComponentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PolicyComponentDTO toDtoPolicyComponentId(PolicyComponent policyComponent);

    @Named("policyComponentIdSet")
    default Set<PolicyComponentDTO> toDtoPolicyComponentIdSet(Set<PolicyComponent> policyComponent) {
        return policyComponent.stream().map(this::toDtoPolicyComponentId).collect(Collectors.toSet());
    }

    @Named("quotationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotationDTO toDtoQuotationId(Quotation quotation);
}
