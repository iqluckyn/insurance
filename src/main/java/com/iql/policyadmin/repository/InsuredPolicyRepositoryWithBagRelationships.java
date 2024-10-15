package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.InsuredPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InsuredPolicyRepositoryWithBagRelationships {
    Optional<InsuredPolicy> fetchBagRelationships(Optional<InsuredPolicy> insuredPolicy);

    List<InsuredPolicy> fetchBagRelationships(List<InsuredPolicy> insuredPolicies);

    Page<InsuredPolicy> fetchBagRelationships(Page<InsuredPolicy> insuredPolicies);
}
