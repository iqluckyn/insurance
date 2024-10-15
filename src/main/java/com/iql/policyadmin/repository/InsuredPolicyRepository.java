package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.InsuredPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuredPolicy entity.
 *
 * When extending this class, extend InsuredPolicyRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface InsuredPolicyRepository extends InsuredPolicyRepositoryWithBagRelationships, JpaRepository<InsuredPolicy, Long> {
    default Optional<InsuredPolicy> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<InsuredPolicy> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<InsuredPolicy> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
