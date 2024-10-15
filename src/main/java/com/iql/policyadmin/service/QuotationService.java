package com.iql.policyadmin.service;

import com.iql.policyadmin.service.dto.QuotationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iql.policyadmin.domain.Quotation}.
 */
public interface QuotationService {
    /**
     * Save a quotation.
     *
     * @param quotationDTO the entity to save.
     * @return the persisted entity.
     */
    QuotationDTO save(QuotationDTO quotationDTO);

    /**
     * Updates a quotation.
     *
     * @param quotationDTO the entity to update.
     * @return the persisted entity.
     */
    QuotationDTO update(QuotationDTO quotationDTO);

    /**
     * Partially updates a quotation.
     *
     * @param quotationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuotationDTO> partialUpdate(QuotationDTO quotationDTO);

    /**
     * Get all the quotations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuotationDTO> findAll(Pageable pageable);

    /**
     * Get all the quotations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuotationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quotation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuotationDTO> findOne(Long id);

    /**
     * Delete the "id" quotation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
