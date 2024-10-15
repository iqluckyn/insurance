package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.PolicyComponent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PolicyComponent entity.
 */
@Repository
public interface PolicyComponentRepository extends JpaRepository<PolicyComponent, Long> {
    default Optional<PolicyComponent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PolicyComponent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PolicyComponent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select policyComponent from PolicyComponent policyComponent left join fetch policyComponent.component",
        countQuery = "select count(policyComponent) from PolicyComponent policyComponent"
    )
    Page<PolicyComponent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select policyComponent from PolicyComponent policyComponent left join fetch policyComponent.component")
    List<PolicyComponent> findAllWithToOneRelationships();

    @Query(
        "select policyComponent from PolicyComponent policyComponent left join fetch policyComponent.component where policyComponent.id =:id"
    )
    Optional<PolicyComponent> findOneWithToOneRelationships(@Param("id") Long id);
}
