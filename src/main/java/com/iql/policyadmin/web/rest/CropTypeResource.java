package com.iql.policyadmin.web.rest;

import com.iql.policyadmin.repository.CropTypeRepository;
import com.iql.policyadmin.service.CropTypeService;
import com.iql.policyadmin.service.dto.CropTypeDTO;
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
 * REST controller for managing {@link com.iql.policyadmin.domain.CropType}.
 */
@RestController
@RequestMapping("/api/crop-types")
public class CropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropTypeResource.class);

    private static final String ENTITY_NAME = "cropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropTypeService cropTypeService;

    private final CropTypeRepository cropTypeRepository;

    public CropTypeResource(CropTypeService cropTypeService, CropTypeRepository cropTypeRepository) {
        this.cropTypeService = cropTypeService;
        this.cropTypeRepository = cropTypeRepository;
    }

    /**
     * {@code POST  /crop-types} : Create a new cropType.
     *
     * @param cropTypeDTO the cropTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropTypeDTO, or with status {@code 400 (Bad Request)} if the cropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropTypeDTO> createCropType(@Valid @RequestBody CropTypeDTO cropTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save CropType : {}", cropTypeDTO);
        if (cropTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropTypeDTO = cropTypeService.save(cropTypeDTO);
        return ResponseEntity.created(new URI("/api/crop-types/" + cropTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropTypeDTO.getId().toString()))
            .body(cropTypeDTO);
    }

    /**
     * {@code PUT  /crop-types/:id} : Updates an existing cropType.
     *
     * @param id the id of the cropTypeDTO to save.
     * @param cropTypeDTO the cropTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cropTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropTypeDTO> updateCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CropTypeDTO cropTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropType : {}, {}", id, cropTypeDTO);
        if (cropTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropTypeDTO = cropTypeService.update(cropTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropTypeDTO.getId().toString()))
            .body(cropTypeDTO);
    }

    /**
     * {@code PATCH  /crop-types/:id} : Partial updates given fields of an existing cropType, field will ignore if it is null
     *
     * @param id the id of the cropTypeDTO to save.
     * @param cropTypeDTO the cropTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cropTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cropTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropTypeDTO> partialUpdateCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CropTypeDTO cropTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropType partially : {}, {}", id, cropTypeDTO);
        if (cropTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropTypeDTO> result = cropTypeService.partialUpdate(cropTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-types} : get all the cropTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropTypeDTO>> getAllCropTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CropTypes");
        Page<CropTypeDTO> page = cropTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-types/:id} : get the "id" cropType.
     *
     * @param id the id of the cropTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropTypeDTO> getCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropType : {}", id);
        Optional<CropTypeDTO> cropTypeDTO = cropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropTypeDTO);
    }

    /**
     * {@code DELETE  /crop-types/:id} : delete the "id" cropType.
     *
     * @param id the id of the cropTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropType : {}", id);
        cropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
