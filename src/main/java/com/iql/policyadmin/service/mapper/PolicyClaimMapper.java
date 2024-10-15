package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.InsuredPolicy;
import com.iql.policyadmin.domain.PolicyClaim;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import com.iql.policyadmin.service.dto.PolicyClaimDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PolicyClaim} and its DTO {@link PolicyClaimDTO}.
 */
@Mapper(componentModel = "spring")
public interface PolicyClaimMapper extends EntityMapper<PolicyClaimDTO, PolicyClaim> {
    @Mapping(target = "policy", source = "policy", qualifiedByName = "insuredPolicyPolicyNumber")
    PolicyClaimDTO toDto(PolicyClaim s);

    @Named("insuredPolicyPolicyNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "policyNumber", source = "policyNumber")
    InsuredPolicyDTO toDtoInsuredPolicyPolicyNumber(InsuredPolicy insuredPolicy);
}
