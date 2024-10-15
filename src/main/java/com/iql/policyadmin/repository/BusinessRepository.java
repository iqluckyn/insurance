package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.Business;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Business entity.
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    default Optional<Business> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Business> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Business> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select business from Business business left join fetch business.businessType",
        countQuery = "select count(business) from Business business"
    )
    Page<Business> findAllWithToOneRelationships(Pageable pageable);

    @Query("select business from Business business left join fetch business.businessType")
    List<Business> findAllWithToOneRelationships();

    @Query("select business from Business business left join fetch business.businessType where business.id =:id")
    Optional<Business> findOneWithToOneRelationships(@Param("id") Long id);
}
