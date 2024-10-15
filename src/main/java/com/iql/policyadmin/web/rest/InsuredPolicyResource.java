package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.InsuredPolicyRepository;
import com.iql.policyadmin.service.InsuredPolicyService;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.InsuredPolicy}.
 */
@RestController
@RequestMapping("/api/insured-policies")
public class InsuredPolicyResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuredPolicyResource.class);

    private static final String ENTITY_NAME = "insuredPolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuredPolicyService insuredPolicyService;

    private final InsuredPolicyRepository insuredPolicyRepository;

    public InsuredPolicyResource(InsuredPolicyService insuredPolicyService, InsuredPolicyRepository insuredPolicyRepository) {
        this.insuredPolicyService = insuredPolicyService;
        this.insuredPolicyRepository = insuredPolicyRepository;
    }

    /**
     * {@code POST  /insured-policies} : Create a new insuredPolicy.
     *
     * @param insuredPolicyDTO the insuredPolicyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuredPolicyDTO, or with status {@code 400 (Bad Request)} if the insuredPolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuredPolicyDTO> createInsuredPolicy(@Valid @RequestBody InsuredPolicyDTO insuredPolicyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InsuredPolicy : {}", insuredPolicyDTO);
        if (insuredPolicyDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuredPolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuredPolicyDTO = insuredPolicyService.save(insuredPolicyDTO);
        return ResponseEntity.created(new URI("/api/insured-policies/" + insuredPolicyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuredPolicyDTO.getId().toString()))
            .body(insuredPolicyDTO);
    }

    /**
     * {@code PUT  /insured-policies/:id} : Updates an existing insuredPolicy.
     *
     * @param id the id of the insuredPolicyDTO to save.
     * @param insuredPolicyDTO the insuredPolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuredPolicyDTO,
     * or with status {@code 400 (Bad Request)} if the insuredPolicyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuredPolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuredPolicyDTO> updateInsuredPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InsuredPolicyDTO insuredPolicyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuredPolicy : {}, {}", id, insuredPolicyDTO);
        if (insuredPolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuredPolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuredPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuredPolicyDTO = insuredPolicyService.update(insuredPolicyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuredPolicyDTO.getId().toString()))
            .body(insuredPolicyDTO);
    }

    /**
     * {@code PATCH  /insured-policies/:id} : Partial updates given fields of an existing insuredPolicy, field will ignore if it is null
     *
     * @param id the id of the insuredPolicyDTO to save.
     * @param insuredPolicyDTO the insuredPolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuredPolicyDTO,
     * or with status {@code 400 (Bad Request)} if the insuredPolicyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insuredPolicyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuredPolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuredPolicyDTO> partialUpdateInsuredPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InsuredPolicyDTO insuredPolicyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuredPolicy partially : {}, {}", id, insuredPolicyDTO);
        if (insuredPolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuredPolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuredPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuredPolicyDTO> result = insuredPolicyService.partialUpdate(insuredPolicyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuredPolicyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insured-policies} : get all the insuredPolicies.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuredPolicies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuredPolicyDTO>> getAllInsuredPolicies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of InsuredPolicies");
        Page<InsuredPolicyDTO> page;
        if (eagerload) {
            page = insuredPolicyService.findAllWithEagerRelationships(pageable);
        } else {
            page = insuredPolicyService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insured-policies/:id} : get the "id" insuredPolicy.
     *
     * @param id the id of the insuredPolicyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuredPolicyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuredPolicyDTO> getInsuredPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuredPolicy : {}", id);
        Optional<InsuredPolicyDTO> insuredPolicyDTO = insuredPolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuredPolicyDTO);
    }

    /**
     * {@code DELETE  /insured-policies/:id} : delete the "id" insuredPolicy.
     *
     * @param id the id of the insuredPolicyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuredPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuredPolicy : {}", id);
        insuredPolicyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
