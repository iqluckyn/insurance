package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.Quotation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quotation entity.
 */
@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    default Optional<Quotation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quotation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quotation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select quotation from Quotation quotation left join fetch quotation.season left join fetch quotation.farmer left join fetch quotation.quotationStatus",
        countQuery = "select count(quotation) from Quotation quotation"
    )
    Page<Quotation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select quotation from Quotation quotation left join fetch quotation.season left join fetch quotation.farmer left join fetch quotation.quotationStatus"
    )
    List<Quotation> findAllWithToOneRelationships();

    @Query(
        "select quotation from Quotation quotation left join fetch quotation.season left join fetch quotation.farmer left join fetch quotation.quotationStatus where quotation.id =:id"
    )
    Optional<Quotation> findOneWithToOneRelationships(@Param("id") Long id);
}
