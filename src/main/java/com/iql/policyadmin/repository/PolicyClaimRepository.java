package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.PolicyClaim;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PolicyClaim entity.
 */
@Repository
public interface PolicyClaimRepository extends JpaRepository<PolicyClaim, Long> {
    default Optional<PolicyClaim> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PolicyClaim> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PolicyClaim> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select policyClaim from PolicyClaim policyClaim left join fetch policyClaim.policy",
        countQuery = "select count(policyClaim) from PolicyClaim policyClaim"
    )
    Page<PolicyClaim> findAllWithToOneRelationships(Pageable pageable);

    @Query("select policyClaim from PolicyClaim policyClaim left join fetch policyClaim.policy")
    List<PolicyClaim> findAllWithToOneRelationships();

    @Query("select policyClaim from PolicyClaim policyClaim left join fetch policyClaim.policy where policyClaim.id =:id")
    Optional<PolicyClaim> findOneWithToOneRelationships(@Param("id") Long id);
}
