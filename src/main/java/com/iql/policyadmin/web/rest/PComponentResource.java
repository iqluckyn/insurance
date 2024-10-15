package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.PComponentRepository;
import com.iql.policyadmin.service.PComponentService;
import com.iql.policyadmin.service.dto.PComponentDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.PComponent}.
 */
@RestController
@RequestMapping("/api/p-components")
public class PComponentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PComponentResource.class);

    private static final String ENTITY_NAME = "pComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PComponentService pComponentService;

    private final PComponentRepository pComponentRepository;

    public PComponentResource(PComponentService pComponentService, PComponentRepository pComponentRepository) {
        this.pComponentService = pComponentService;
        this.pComponentRepository = pComponentRepository;
    }

    /**
     * {@code POST  /p-components} : Create a new pComponent.
     *
     * @param pComponentDTO the pComponentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pComponentDTO, or with status {@code 400 (Bad Request)} if the pComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PComponentDTO> createPComponent(@Valid @RequestBody PComponentDTO pComponentDTO) throws URISyntaxException {
        LOG.debug("REST request to save PComponent : {}", pComponentDTO);
        if (pComponentDTO.getId() != null) {
            throw new BadRequestAlertException("A new pComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pComponentDTO = pComponentService.save(pComponentDTO);
        return ResponseEntity.created(new URI("/api/p-components/" + pComponentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, pComponentDTO.getId().toString()))
            .body(pComponentDTO);
    }

    /**
     * {@code PUT  /p-components/:id} : Updates an existing pComponent.
     *
     * @param id the id of the pComponentDTO to save.
     * @param pComponentDTO the pComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pComponentDTO,
     * or with status {@code 400 (Bad Request)} if the pComponentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PComponentDTO> updatePComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PComponentDTO pComponentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PComponent : {}, {}", id, pComponentDTO);
        if (pComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pComponentDTO = pComponentService.update(pComponentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pComponentDTO.getId().toString()))
            .body(pComponentDTO);
    }

    /**
     * {@code PATCH  /p-components/:id} : Partial updates given fields of an existing pComponent, field will ignore if it is null
     *
     * @param id the id of the pComponentDTO to save.
     * @param pComponentDTO the pComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pComponentDTO,
     * or with status {@code 400 (Bad Request)} if the pComponentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pComponentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PComponentDTO> partialUpdatePComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PComponentDTO pComponentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PComponent partially : {}, {}", id, pComponentDTO);
        if (pComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pComponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PComponentDTO> result = pComponentService.partialUpdate(pComponentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pComponentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /p-components} : get all the pComponents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pComponents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PComponentDTO>> getAllPComponents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PComponents");
        Page<PComponentDTO> page = pComponentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /p-components/:id} : get the "id" pComponent.
     *
     * @param id the id of the pComponentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pComponentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PComponentDTO> getPComponent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PComponent : {}", id);
        Optional<PComponentDTO> pComponentDTO = pComponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pComponentDTO);
    }

    /**
     * {@code DELETE  /p-components/:id} : delete the "id" pComponent.
     *
     * @param id the id of the pComponentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePComponent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PComponent : {}", id);
        pComponentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
