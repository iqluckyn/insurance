package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.Season;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Season entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {}
