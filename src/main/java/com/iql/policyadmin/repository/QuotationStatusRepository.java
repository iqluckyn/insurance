package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.QuotationStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuotationStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationStatusRepository extends JpaRepository<QuotationStatus, Long> {}
