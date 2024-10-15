package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.PolicyComponentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.PolicyComponent}.
 */
public interface PolicyComponentService {
    /**
     * Save a policyComponent.
     *
     * @param policyComponentDTO the entity to save.
     * @return the persisted entity.
     */
    PolicyComponentDTO save(PolicyComponentDTO policyComponentDTO);

    /**
     * Updates a policyComponent.
     *
     * @param policyComponentDTO the entity to update.
     * @return the persisted entity.
     */
    PolicyComponentDTO update(PolicyComponentDTO policyComponentDTO);

    /**
     * Partially updates a policyComponent.
     *
     * @param policyComponentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PolicyComponentDTO> partialUpdate(PolicyComponentDTO policyComponentDTO);

    /**
     * Get all the policyComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PolicyComponentDTO> findAll(Pageable pageable);

    /**
     * Get all the policyComponents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PolicyComponentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" policyComponent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PolicyComponentDTO> findOne(Long id);

    /**
     * Delete the "id" policyComponent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
