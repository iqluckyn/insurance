package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.PComponent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PComponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PComponentRepository extends JpaRepository<PComponent, Long> {}
