package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.BusinessTypeRepository;
import com.iql.policyadmin.service.BusinessTypeService;
import com.iql.policyadmin.service.dto.BusinessTypeDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.BusinessType}.
 */
@RestController
@RequestMapping("/api/business-types")
public class BusinessTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessTypeResource.class);

    private static final String ENTITY_NAME = "businessType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessTypeService businessTypeService;

    private final BusinessTypeRepository businessTypeRepository;

    public BusinessTypeResource(BusinessTypeService businessTypeService, BusinessTypeRepository businessTypeRepository) {
        this.businessTypeService = businessTypeService;
        this.businessTypeRepository = businessTypeRepository;
    }

    /**
     * {@code POST  /business-types} : Create a new businessType.
     *
     * @param businessTypeDTO the businessTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessTypeDTO, or with status {@code 400 (Bad Request)} if the businessType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BusinessTypeDTO> createBusinessType(@Valid @RequestBody BusinessTypeDTO businessTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BusinessType : {}", businessTypeDTO);
        if (businessTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        businessTypeDTO = businessTypeService.save(businessTypeDTO);
        return ResponseEntity.created(new URI("/api/business-types/" + businessTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(businessTypeDTO);
    }

    /**
     * {@code PUT  /business-types/:id} : Updates an existing businessType.
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BusinessTypeDTO> updateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessTypeDTO businessTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BusinessType : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        businessTypeDTO = businessTypeService.update(businessTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(businessTypeDTO);
    }

    /**
     * {@code PATCH  /business-types/:id} : Partial updates given fields of an existing businessType, field will ignore if it is null
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessTypeDTO> partialUpdateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessTypeDTO businessTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BusinessType partially : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessTypeDTO> result = businessTypeService.partialUpdate(businessTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-types} : get all the businessTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BusinessTypeDTO>> getAllBusinessTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of BusinessTypes");
        Page<BusinessTypeDTO> page = businessTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-types/:id} : get the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusinessTypeDTO> getBusinessType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BusinessType : {}", id);
        Optional<BusinessTypeDTO> businessTypeDTO = businessTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessTypeDTO);
    }

    /**
     * {@code DELETE  /business-types/:id} : delete the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BusinessType : {}", id);
        businessTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
