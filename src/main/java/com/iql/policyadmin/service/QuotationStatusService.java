package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.QuotationStatus}.
 */
public interface QuotationStatusService {
    /**
     * Save a quotationStatus.
     *
     * @param quotationStatusDTO the entity to save.
     * @return the persisted entity.
     */
    QuotationStatusDTO save(QuotationStatusDTO quotationStatusDTO);

    /**
     * Updates a quotationStatus.
     *
     * @param quotationStatusDTO the entity to update.
     * @return the persisted entity.
     */
    QuotationStatusDTO update(QuotationStatusDTO quotationStatusDTO);

    /**
     * Partially updates a quotationStatus.
     *
     * @param quotationStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuotationStatusDTO> partialUpdate(QuotationStatusDTO quotationStatusDTO);

    /**
     * Get all the quotationStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuotationStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" quotationStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuotationStatusDTO> findOne(Long id);

    /**
     * Delete the "id" quotationStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
