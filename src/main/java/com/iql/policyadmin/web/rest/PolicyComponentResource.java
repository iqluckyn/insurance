package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.PolicyComponentRepository;
import com.iql.policyadmin.service.PolicyComponentService;
import com.iql.policyadmin.service.dto.PolicyComponentDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.PolicyComponent}.
 */
@RestController
@RequestMapping("/api/policy-components")
public class PolicyComponentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyComponentResource.class);

    private static final String ENTITY_NAME = "policyComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolicyComponentService policyComponentService;

    private final PolicyComponentRepository policyComponentRepository;

    public PolicyComponentResource(PolicyComponentService policyComponentService, PolicyComponentRepository policyComponentRepository) {
        this.policyComponentService = policyComponentService;
        this.policyComponentRepository = policyComponentRepository;
    }

    /**
     * {@code POST  /policy-components} : Create a new policyComponent.
     *
     * @param policyComponentDTO the policyComponentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new policyComponentDTO, or with status {@code 400 (Bad Request)} if the policyComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PolicyComponentDTO> createPolicyComponent(@Valid @RequestBody PolicyComponentDTO policyComponentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PolicyComponent : {}", policyComponentDTO);
        if (policyComponentDTO.getId() != null) {
            throw new BadRequestAlertException("A new policyComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        policyComponentDTO = policyComponentService.save(policyComponentDTO);
        return ResponseEntity.created(new URI("/api/policy-components/" + policyComponentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, policyComponentDTO.getId().toString()))
            .body(policyComponentDTO);
    }

    /**
     * {@code PUT  /policy-components/:id} : Updates an existing policyComponent.
     *
     * @param id the id of the policyComponentDTO to save.
     * @param policyComponentDTO the policyComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyComponentDTO,
     * or with status {@code 400 (Bad Request)} if the policyComponentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the policyComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PolicyComponentDTO> updatePolicyComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PolicyComponentDTO policyComponentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PolicyComponent : {}, {}", id, policyComponentDTO);
        if (policyComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        policyComponentDTO = policyComponentService.update(policyComponentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, policyComponentDTO.getId().toString()))
            .body(policyComponentDTO);
    }

    /**
     * {@code PATCH  /policy-components/:id} : Partial updates given fields of an existing policyComponent, field will ignore if it is null
     *
     * @param id the id of the policyComponentDTO to save.
     * @param policyComponentDTO the policyComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyComponentDTO,
     * or with status {@code 400 (Bad Request)} if the policyComponentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the policyComponentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the policyComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PolicyComponentDTO> partialUpdatePolicyComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PolicyComponentDTO policyComponentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PolicyComponent partially : {}, {}", id, policyComponentDTO);
        if (policyComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PolicyComponentDTO> result = policyComponentService.partialUpdate(policyComponentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, policyComponentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /policy-components} : get all the policyComponents.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of policyComponents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PolicyComponentDTO>> getAllPolicyComponents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PolicyComponents");
        Page<PolicyComponentDTO> page;
        if (eagerload) {
            page = policyComponentService.findAllWithEagerRelationships(pageable);
        } else {
            page = policyComponentService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /policy-components/:id} : get the "id" policyComponent.
     *
     * @param id the id of the policyComponentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the policyComponentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PolicyComponentDTO> getPolicyComponent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PolicyComponent : {}", id);
        Optional<PolicyComponentDTO> policyComponentDTO = policyComponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(policyComponentDTO);
    }

    /**
     * {@code DELETE  /policy-components/:id} : delete the "id" policyComponent.
     *
     * @param id the id of the policyComponentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicyComponent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PolicyComponent : {}", id);
        policyComponentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
