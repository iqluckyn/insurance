package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.CropTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.CropType}.
 */
public interface CropTypeService {
    /**
     * Save a cropType.
     *
     * @param cropTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CropTypeDTO save(CropTypeDTO cropTypeDTO);

    /**
     * Updates a cropType.
     *
     * @param cropTypeDTO the entity to update.
     * @return the persisted entity.
     */
    CropTypeDTO update(CropTypeDTO cropTypeDTO);

    /**
     * Partially updates a cropType.
     *
     * @param cropTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CropTypeDTO> partialUpdate(CropTypeDTO cropTypeDTO);

    /**
     * Get all the cropTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CropTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cropType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CropTypeDTO> findOne(Long id);

    /**
     * Delete the "id" cropType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
