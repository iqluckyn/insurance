package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.InsuredPolicy}.
 */
public interface InsuredPolicyService {
    /**
     * Save a insuredPolicy.
     *
     * @param insuredPolicyDTO the entity to save.
     * @return the persisted entity.
     */
    InsuredPolicyDTO save(InsuredPolicyDTO insuredPolicyDTO);

    /**
     * Updates a insuredPolicy.
     *
     * @param insuredPolicyDTO the entity to update.
     * @return the persisted entity.
     */
    InsuredPolicyDTO update(InsuredPolicyDTO insuredPolicyDTO);

    /**
     * Partially updates a insuredPolicy.
     *
     * @param insuredPolicyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InsuredPolicyDTO> partialUpdate(InsuredPolicyDTO insuredPolicyDTO);

    /**
     * Get all the insuredPolicies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsuredPolicyDTO> findAll(Pageable pageable);

    /**
     * Get all the insuredPolicies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsuredPolicyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" insuredPolicy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsuredPolicyDTO> findOne(Long id);

    /**
     * Delete the "id" insuredPolicy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
