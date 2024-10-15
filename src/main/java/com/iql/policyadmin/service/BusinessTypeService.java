package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.BusinessTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.BusinessType}.
 */
public interface BusinessTypeService {
    /**
     * Save a businessType.
     *
     * @param businessTypeDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessTypeDTO save(BusinessTypeDTO businessTypeDTO);

    /**
     * Updates a businessType.
     *
     * @param businessTypeDTO the entity to update.
     * @return the persisted entity.
     */
    BusinessTypeDTO update(BusinessTypeDTO businessTypeDTO);

    /**
     * Partially updates a businessType.
     *
     * @param businessTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessTypeDTO> partialUpdate(BusinessTypeDTO businessTypeDTO);

    /**
     * Get all the businessTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" businessType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessTypeDTO> findOne(Long id);

    /**
     * Delete the "id" businessType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
