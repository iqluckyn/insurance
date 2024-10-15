package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.PComponentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.PComponent}.
 */
public interface PComponentService {
    /**
     * Save a pComponent.
     *
     * @param pComponentDTO the entity to save.
     * @return the persisted entity.
     */
    PComponentDTO save(PComponentDTO pComponentDTO);

    /**
     * Updates a pComponent.
     *
     * @param pComponentDTO the entity to update.
     * @return the persisted entity.
     */
    PComponentDTO update(PComponentDTO pComponentDTO);

    /**
     * Partially updates a pComponent.
     *
     * @param pComponentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PComponentDTO> partialUpdate(PComponentDTO pComponentDTO);

    /**
     * Get all the pComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PComponentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pComponent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PComponentDTO> findOne(Long id);

    /**
     * Delete the "id" pComponent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
