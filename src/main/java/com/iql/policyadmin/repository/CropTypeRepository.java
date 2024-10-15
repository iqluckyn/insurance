package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.CropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropTypeRepository extends JpaRepository<CropType, Long> {}
