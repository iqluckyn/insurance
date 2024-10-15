package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.BusinessDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.Business}.
 */
public interface BusinessService {
    /**
     * Save a business.
     *
     * @param businessDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessDTO save(BusinessDTO businessDTO);

    /**
     * Updates a business.
     *
     * @param businessDTO the entity to update.
     * @return the persisted entity.
     */
    BusinessDTO update(BusinessDTO businessDTO);

    /**
     * Partially updates a business.
     *
     * @param businessDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessDTO> partialUpdate(BusinessDTO businessDTO);

    /**
     * Get all the businesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessDTO> findAll(Pageable pageable);

    /**
     * Get all the businesses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" business.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessDTO> findOne(Long id);

    /**
     * Delete the "id" business.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
