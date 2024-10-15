package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.FarmDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.Farm}.
 */
public interface FarmService {
    /**
     * Save a farm.
     *
     * @param farmDTO the entity to save.
     * @return the persisted entity.
     */
    FarmDTO save(FarmDTO farmDTO);

    /**
     * Updates a farm.
     *
     * @param farmDTO the entity to update.
     * @return the persisted entity.
     */
    FarmDTO update(FarmDTO farmDTO);

    /**
     * Partially updates a farm.
     *
     * @param farmDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FarmDTO> partialUpdate(FarmDTO farmDTO);

    /**
     * Get all the farms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FarmDTO> findAll(Pageable pageable);

    /**
     * Get all the farms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FarmDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" farm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FarmDTO> findOne(Long id);

    /**
     * Delete the "id" farm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
