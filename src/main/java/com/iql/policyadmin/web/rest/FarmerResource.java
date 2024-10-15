package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.FarmerRepository;
import com.iql.policyadmin.service.FarmerService;
import com.iql.policyadmin.service.dto.FarmerDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.Farmer}.
 */
@RestController
@RequestMapping("/api/farmers")
public class FarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerResource.class);

    private static final String ENTITY_NAME = "farmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerService farmerService;

    private final FarmerRepository farmerRepository;

    public FarmerResource(FarmerService farmerService, FarmerRepository farmerRepository) {
        this.farmerService = farmerService;
        this.farmerRepository = farmerRepository;
    }

    /**
     * {@code POST  /farmers} : Create a new farmer.
     *
     * @param farmerDTO the farmerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmerDTO, or with status {@code 400 (Bad Request)} if the farmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmerDTO> createFarmer(@Valid @RequestBody FarmerDTO farmerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Farmer : {}", farmerDTO);
        if (farmerDTO.getId() != null) {
            throw new BadRequestAlertException("A new farmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmerDTO = farmerService.save(farmerDTO);
        return ResponseEntity.created(new URI("/api/farmers/" + farmerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmerDTO.getId().toString()))
            .body(farmerDTO);
    }

    /**
     * {@code PUT  /farmers/:id} : Updates an existing farmer.
     *
     * @param id the id of the farmerDTO to save.
     * @param farmerDTO the farmerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerDTO,
     * or with status {@code 400 (Bad Request)} if the farmerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmerDTO> updateFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FarmerDTO farmerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Farmer : {}, {}", id, farmerDTO);
        if (farmerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmerDTO = farmerService.update(farmerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerDTO.getId().toString()))
            .body(farmerDTO);
    }

    /**
     * {@code PATCH  /farmers/:id} : Partial updates given fields of an existing farmer, field will ignore if it is null
     *
     * @param id the id of the farmerDTO to save.
     * @param farmerDTO the farmerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerDTO,
     * or with status {@code 400 (Bad Request)} if the farmerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the farmerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmerDTO> partialUpdateFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FarmerDTO farmerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Farmer partially : {}, {}", id, farmerDTO);
        if (farmerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmerDTO> result = farmerService.partialUpdate(farmerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /farmers} : get all the farmers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmerDTO>> getAllFarmers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Farmers");
        Page<FarmerDTO> page;
        if (eagerload) {
            page = farmerService.findAllWithEagerRelationships(pageable);
        } else {
            page = farmerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmers/:id} : get the "id" farmer.
     *
     * @param id the id of the farmerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmerDTO> getFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Farmer : {}", id);
        Optional<FarmerDTO> farmerDTO = farmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmerDTO);
    }

    /**
     * {@code DELETE  /farmers/:id} : delete the "id" farmer.
     *
     * @param id the id of the farmerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Farmer : {}", id);
        farmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
