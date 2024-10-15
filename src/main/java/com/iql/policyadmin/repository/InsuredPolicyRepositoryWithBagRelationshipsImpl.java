package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.InsuredPolicy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class InsuredPolicyRepositoryWithBagRelationshipsImpl implements InsuredPolicyRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String INSUREDPOLICIES_PARAMETER = "insuredPolicies";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<InsuredPolicy> fetchBagRelationships(Optional<InsuredPolicy> insuredPolicy) {
        return insuredPolicy.map(this::fetchComponents);
    }

    @Override
    public Page<InsuredPolicy> fetchBagRelationships(Page<InsuredPolicy> insuredPolicies) {
        return new PageImpl<>(
            fetchBagRelationships(insuredPolicies.getContent()),
            insuredPolicies.getPageable(),
            insuredPolicies.getTotalElements()
        );
    }

    @Override
    public List<InsuredPolicy> fetchBagRelationships(List<InsuredPolicy> insuredPolicies) {
        return Optional.of(insuredPolicies).map(this::fetchComponents).orElse(Collections.emptyList());
    }

    InsuredPolicy fetchComponents(InsuredPolicy result) {
        return entityManager
            .createQuery(
                "select insuredPolicy from InsuredPolicy insuredPolicy left join fetch insuredPolicy.components where insuredPolicy.id = :id",
                InsuredPolicy.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<InsuredPolicy> fetchComponents(List<InsuredPolicy> insuredPolicies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, insuredPolicies.size()).forEach(index -> order.put(insuredPolicies.get(index).getId(), index));
        List<InsuredPolicy> result = entityManager
            .createQuery(
                "select insuredPolicy from InsuredPolicy insuredPolicy left join fetch insuredPolicy.components where insuredPolicy in :insuredPolicies",
                InsuredPolicy.class
            )
            .setParameter(INSUREDPOLICIES_PARAMETER, insuredPolicies)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
