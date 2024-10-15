package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.PolicyClaimRepository;
import com.iql.policyadmin.service.PolicyClaimService;
import com.iql.policyadmin.service.dto.PolicyClaimDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.PolicyClaim}.
 */
@RestController
@RequestMapping("/api/policy-claims")
public class PolicyClaimResource {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyClaimResource.class);

    private static final String ENTITY_NAME = "policyClaim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolicyClaimService policyClaimService;

    private final PolicyClaimRepository policyClaimRepository;

    public PolicyClaimResource(PolicyClaimService policyClaimService, PolicyClaimRepository policyClaimRepository) {
        this.policyClaimService = policyClaimService;
        this.policyClaimRepository = policyClaimRepository;
    }

    /**
     * {@code POST  /policy-claims} : Create a new policyClaim.
     *
     * @param policyClaimDTO the policyClaimDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new policyClaimDTO, or with status {@code 400 (Bad Request)} if the policyClaim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PolicyClaimDTO> createPolicyClaim(@Valid @RequestBody PolicyClaimDTO policyClaimDTO) throws URISyntaxException {
        LOG.debug("REST request to save PolicyClaim : {}", policyClaimDTO);
        if (policyClaimDTO.getId() != null) {
            throw new BadRequestAlertException("A new policyClaim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        policyClaimDTO = policyClaimService.save(policyClaimDTO);
        return ResponseEntity.created(new URI("/api/policy-claims/" + policyClaimDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, policyClaimDTO.getId().toString()))
            .body(policyClaimDTO);
    }

    /**
     * {@code PUT  /policy-claims/:id} : Updates an existing policyClaim.
     *
     * @param id the id of the policyClaimDTO to save.
     * @param policyClaimDTO the policyClaimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyClaimDTO,
     * or with status {@code 400 (Bad Request)} if the policyClaimDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the policyClaimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PolicyClaimDTO> updatePolicyClaim(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PolicyClaimDTO policyClaimDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PolicyClaim : {}, {}", id, policyClaimDTO);
        if (policyClaimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyClaimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyClaimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        policyClaimDTO = policyClaimService.update(policyClaimDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, policyClaimDTO.getId().toString()))
            .body(policyClaimDTO);
    }

    /**
     * {@code PATCH  /policy-claims/:id} : Partial updates given fields of an existing policyClaim, field will ignore if it is null
     *
     * @param id the id of the policyClaimDTO to save.
     * @param policyClaimDTO the policyClaimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyClaimDTO,
     * or with status {@code 400 (Bad Request)} if the policyClaimDTO is not valid,
     * or with status {@code 404 (Not Found)} if the policyClaimDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the policyClaimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PolicyClaimDTO> partialUpdatePolicyClaim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PolicyClaimDTO policyClaimDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PolicyClaim partially : {}, {}", id, policyClaimDTO);
        if (policyClaimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyClaimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyClaimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PolicyClaimDTO> result = policyClaimService.partialUpdate(policyClaimDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, policyClaimDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /policy-claims} : get all the policyClaims.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of policyClaims in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PolicyClaimDTO>> getAllPolicyClaims(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PolicyClaims");
        Page<PolicyClaimDTO> page;
        if (eagerload) {
            page = policyClaimService.findAllWithEagerRelationships(pageable);
        } else {
            page = policyClaimService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /policy-claims/:id} : get the "id" policyClaim.
     *
     * @param id the id of the policyClaimDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the policyClaimDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PolicyClaimDTO> getPolicyClaim(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PolicyClaim : {}", id);
        Optional<PolicyClaimDTO> policyClaimDTO = policyClaimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(policyClaimDTO);
    }

    /**
     * {@code DELETE  /policy-claims/:id} : delete the "id" policyClaim.
     *
     * @param id the id of the policyClaimDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicyClaim(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PolicyClaim : {}", id);
        policyClaimService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
