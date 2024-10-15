package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.QuotationStatusRepository;
import com.iql.policyadmin.service.QuotationStatusService;
import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import com.iql.policyadmin.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iql.policyadmin.domain.QuotationStatus}.
 */
@RestController
@RequestMapping("/api/quotation-statuses")
public class QuotationStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationStatusResource.class);

    private static final String ENTITY_NAME = "quotationStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationStatusService quotationStatusService;

    private final QuotationStatusRepository quotationStatusRepository;

    public QuotationStatusResource(QuotationStatusService quotationStatusService, QuotationStatusRepository quotationStatusRepository) {
        this.quotationStatusService = quotationStatusService;
        this.quotationStatusRepository = quotationStatusRepository;
    }

    /**
     * {@code POST  /quotation-statuses} : Create a new quotationStatus.
     *
     * @param quotationStatusDTO the quotationStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationStatusDTO, or with status {@code 400 (Bad Request)} if the quotationStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuotationStatusDTO> createQuotationStatus(@Valid @RequestBody QuotationStatusDTO quotationStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QuotationStatus : {}", quotationStatusDTO);
        if (quotationStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotationStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotationStatusDTO = quotationStatusService.save(quotationStatusDTO);
        return ResponseEntity.created(new URI("/api/quotation-statuses/" + quotationStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, quotationStatusDTO.getId().toString()))
            .body(quotationStatusDTO);
    }

    /**
     * {@code PUT  /quotation-statuses/:id} : Updates an existing quotationStatus.
     *
     * @param id the id of the quotationStatusDTO to save.
     * @param quotationStatusDTO the quotationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the quotationStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuotationStatusDTO> updateQuotationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuotationStatusDTO quotationStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuotationStatus : {}, {}", id, quotationStatusDTO);
        if (quotationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quotationStatusDTO = quotationStatusService.update(quotationStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationStatusDTO.getId().toString()))
            .body(quotationStatusDTO);
    }

    /**
     * {@code PATCH  /quotation-statuses/:id} : Partial updates given fields of an existing quotationStatus, field will ignore if it is null
     *
     * @param id the id of the quotationStatusDTO to save.
     * @param quotationStatusDTO the quotationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the quotationStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotationStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuotationStatusDTO> partialUpdateQuotationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuotationStatusDTO quotationStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuotationStatus partially : {}, {}", id, quotationStatusDTO);
        if (quotationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuotationStatusDTO> result = quotationStatusService.partialUpdate(quotationStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quotation-statuses} : get all the quotationStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotationStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuotationStatusDTO>> getAllQuotationStatuses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of QuotationStatuses");
        Page<QuotationStatusDTO> page = quotationStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quotation-statuses/:id} : get the "id" quotationStatus.
     *
     * @param id the id of the quotationStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuotationStatusDTO> getQuotationStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuotationStatus : {}", id);
        Optional<QuotationStatusDTO> quotationStatusDTO = quotationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotationStatusDTO);
    }

    /**
     * {@code DELETE  /quotation-statuses/:id} : delete the "id" quotationStatus.
     *
     * @param id the id of the quotationStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotationStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuotationStatus : {}", id);
        quotationStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
