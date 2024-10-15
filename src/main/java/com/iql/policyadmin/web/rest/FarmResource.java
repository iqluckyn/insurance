package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.FarmRepository;
import com.iql.policyadmin.service.FarmService;
import com.iql.policyadmin.service.dto.FarmDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.Farm}.
 */
@RestController
@RequestMapping("/api/farms")
public class FarmResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmResource.class);

    private static final String ENTITY_NAME = "farm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmService farmService;

    private final FarmRepository farmRepository;

    public FarmResource(FarmService farmService, FarmRepository farmRepository) {
        this.farmService = farmService;
        this.farmRepository = farmRepository;
    }

    /**
     * {@code POST  /farms} : Create a new farm.
     *
     * @param farmDTO the farmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmDTO, or with status {@code 400 (Bad Request)} if the farm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmDTO> createFarm(@Valid @RequestBody FarmDTO farmDTO) throws URISyntaxException {
        LOG.debug("REST request to save Farm : {}", farmDTO);
        if (farmDTO.getId() != null) {
            throw new BadRequestAlertException("A new farm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmDTO = farmService.save(farmDTO);
        return ResponseEntity.created(new URI("/api/farms/" + farmDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmDTO.getId().toString()))
            .body(farmDTO);
    }

    /**
     * {@code PUT  /farms/:id} : Updates an existing farm.
     *
     * @param id the id of the farmDTO to save.
     * @param farmDTO the farmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmDTO,
     * or with status {@code 400 (Bad Request)} if the farmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmDTO> updateFarm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FarmDTO farmDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Farm : {}, {}", id, farmDTO);
        if (farmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmDTO = farmService.update(farmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmDTO.getId().toString()))
            .body(farmDTO);
    }

    /**
     * {@code PATCH  /farms/:id} : Partial updates given fields of an existing farm, field will ignore if it is null
     *
     * @param id the id of the farmDTO to save.
     * @param farmDTO the farmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmDTO,
     * or with status {@code 400 (Bad Request)} if the farmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the farmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmDTO> partialUpdateFarm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FarmDTO farmDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Farm partially : {}, {}", id, farmDTO);
        if (farmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmDTO> result = farmService.partialUpdate(farmDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /farms} : get all the farms.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmDTO>> getAllFarms(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Farms");
        Page<FarmDTO> page;
        if (eagerload) {
            page = farmService.findAllWithEagerRelationships(pageable);
        } else {
            page = farmService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farms/:id} : get the "id" farm.
     *
     * @param id the id of the farmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarm(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Farm : {}", id);
        Optional<FarmDTO> farmDTO = farmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmDTO);
    }

    /**
     * {@code DELETE  /farms/:id} : delete the "id" farm.
     *
     * @param id the id of the farmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarm(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Farm : {}", id);
        farmService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
