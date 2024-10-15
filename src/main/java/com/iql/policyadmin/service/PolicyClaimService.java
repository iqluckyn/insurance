package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.PolicyClaimDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.PolicyClaim}.
 */
public interface PolicyClaimService {
    /**
     * Save a policyClaim.
     *
     * @param policyClaimDTO the entity to save.
     * @return the persisted entity.
     */
    PolicyClaimDTO save(PolicyClaimDTO policyClaimDTO);

    /**
     * Updates a policyClaim.
     *
     * @param policyClaimDTO the entity to update.
     * @return the persisted entity.
     */
    PolicyClaimDTO update(PolicyClaimDTO policyClaimDTO);

    /**
     * Partially updates a policyClaim.
     *
     * @param policyClaimDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PolicyClaimDTO> partialUpdate(PolicyClaimDTO policyClaimDTO);

    /**
     * Get all the policyClaims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PolicyClaimDTO> findAll(Pageable pageable);

    /**
     * Get all the policyClaims with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PolicyClaimDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" policyClaim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PolicyClaimDTO> findOne(Long id);

    /**
     * Delete the "id" policyClaim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
