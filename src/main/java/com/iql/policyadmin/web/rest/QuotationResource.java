package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.QuotationRepository;
import com.iql.policyadmin.service.QuotationService;
import com.iql.policyadmin.service.dto.QuotationDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.Quotation}.
 */
@RestController
@RequestMapping("/api/quotations")
public class QuotationResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationResource.class);

    private static final String ENTITY_NAME = "quotation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationService quotationService;

    private final QuotationRepository quotationRepository;

    public QuotationResource(QuotationService quotationService, QuotationRepository quotationRepository) {
        this.quotationService = quotationService;
        this.quotationRepository = quotationRepository;
    }

    /**
     * {@code POST  /quotations} : Create a new quotation.
     *
     * @param quotationDTO the quotationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationDTO, or with status {@code 400 (Bad Request)} if the quotation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuotationDTO> createQuotation(@Valid @RequestBody QuotationDTO quotationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Quotation : {}", quotationDTO);
        if (quotationDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotationDTO = quotationService.save(quotationDTO);
        return ResponseEntity.created(new URI("/api/quotations/" + quotationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, quotationDTO.getId().toString()))
            .body(quotationDTO);
    }

    /**
     * {@code PUT  /quotations/:id} : Updates an existing quotation.
     *
     * @param id the id of the quotationDTO to save.
     * @param quotationDTO the quotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationDTO,
     * or with status {@code 400 (Bad Request)} if the quotationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuotationDTO> updateQuotation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuotationDTO quotationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Quotation : {}, {}", id, quotationDTO);
        if (quotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quotationDTO = quotationService.update(quotationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationDTO.getId().toString()))
            .body(quotationDTO);
    }

    /**
     * {@code PATCH  /quotations/:id} : Partial updates given fields of an existing quotation, field will ignore if it is null
     *
     * @param id the id of the quotationDTO to save.
     * @param quotationDTO the quotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationDTO,
     * or with status {@code 400 (Bad Request)} if the quotationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuotationDTO> partialUpdateQuotation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuotationDTO quotationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Quotation partially : {}, {}", id, quotationDTO);
        if (quotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quotationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuotationDTO> result = quotationService.partialUpdate(quotationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quotations} : get all the quotations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuotationDTO>> getAllQuotations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Quotations");
        Page<QuotationDTO> page;
        if (eagerload) {
            page = quotationService.findAllWithEagerRelationships(pageable);
        } else {
            page = quotationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quotations/:id} : get the "id" quotation.
     *
     * @param id the id of the quotationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Quotation : {}", id);
        Optional<QuotationDTO> quotationDTO = quotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotationDTO);
    }

    /**
     * {@code DELETE  /quotations/:id} : delete the "id" quotation.
     *
     * @param id the id of the quotationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Quotation : {}", id);
        quotationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
