package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.BusinessType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BusinessType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {}
