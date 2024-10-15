package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.FarmerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.Farmer}.
 */
public interface FarmerService {
    /**
     * Save a farmer.
     *
     * @param farmerDTO the entity to save.
     * @return the persisted entity.
     */
    FarmerDTO save(FarmerDTO farmerDTO);

    /**
     * Updates a farmer.
     *
     * @param farmerDTO the entity to update.
     * @return the persisted entity.
     */
    FarmerDTO update(FarmerDTO farmerDTO);

    /**
     * Partially updates a farmer.
     *
     * @param farmerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FarmerDTO> partialUpdate(FarmerDTO farmerDTO);

    /**
     * Get all the farmers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FarmerDTO> findAll(Pageable pageable);

    /**
     * Get all the farmers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FarmerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" farmer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FarmerDTO> findOne(Long id);

    /**
     * Delete the "id" farmer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
