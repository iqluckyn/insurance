package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.Farm;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Farm entity.
 */
@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
    default Optional<Farm> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Farm> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Farm> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select farm from Farm farm left join fetch farm.cropType", countQuery = "select count(farm) from Farm farm")
    Page<Farm> findAllWithToOneRelationships(Pageable pageable);

    @Query("select farm from Farm farm left join fetch farm.cropType")
    List<Farm> findAllWithToOneRelationships();

    @Query("select farm from Farm farm left join fetch farm.cropType where farm.id =:id")
    Optional<Farm> findOneWithToOneRelationships(@Param("id") Long id);
}
