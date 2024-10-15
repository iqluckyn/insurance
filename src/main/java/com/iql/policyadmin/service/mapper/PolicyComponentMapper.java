package com.iql.policyadmin.service.mapper;

import com.iql.policyadmin.domain.InsuredPolicy;
import com.iql.policyadmin.domain.PComponent;
import com.iql.policyadmin.domain.PolicyComponent;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import com.iql.policyadmin.service.dto.PComponentDTO;
import com.iql.policyadmin.service.dto.PolicyComponentDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PolicyComponent} and its DTO {@link PolicyComponentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PolicyComponentMapper extends EntityMapper<PolicyComponentDTO, PolicyComponent> {
    @Mapping(target = "component", source = "component", qualifiedByName = "pComponentName")
    @Mapping(target = "policies", source = "policies", qualifiedByName = "insuredPolicyIdSet")
    PolicyComponentDTO toDto(PolicyComponent s);

    @Mapping(target = "policies", ignore = true)
    @Mapping(target = "removePolicies", ignore = true)
    PolicyComponent toEntity(PolicyComponentDTO policyComponentDTO);

    @Named("pComponentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PComponentDTO toDtoPComponentName(PComponent pComponent);

    @Named("insuredPolicyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InsuredPolicyDTO toDtoInsuredPolicyId(InsuredPolicy insuredPolicy);

    @Named("insuredPolicyIdSet")
    default Set<InsuredPolicyDTO> toDtoInsuredPolicyIdSet(Set<InsuredPolicy> insuredPolicy) {
        return insuredPolicy.stream().map(this::toDtoInsuredPolicyId).collect(Collectors.toSet());
    }
}
