package com.iql.policyadmin.repository;

import com.iql.policyadmin.domain.Farmer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Farmer entity.
 */
@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    @Query("select farmer from Farmer farmer where farmer.user.login = ?#{authentication.name}")
    List<Farmer> findByUserIsCurrentUser();

    default Optional<Farmer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Farmer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Farmer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select farmer from Farmer farmer left join fetch farmer.user left join fetch farmer.business",
        countQuery = "select count(farmer) from Farmer farmer"
    )
    Page<Farmer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select farmer from Farmer farmer left join fetch farmer.user left join fetch farmer.business")
    List<Farmer> findAllWithToOneRelationships();

    @Query("select farmer from Farmer farmer left join fetch farmer.user left join fetch farmer.business where farmer.id =:id")
    Optional<Farmer> findOneWithToOneRelationships(@Param("id") Long id);
}
