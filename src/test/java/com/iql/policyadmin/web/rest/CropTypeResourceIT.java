package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.CropTypeAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.CropType;
import com.iql.policyadmin.repository.CropTypeRepository;
import com.iql.policyadmin.service.dto.CropTypeDTO;
import com.iql.policyadmin.service.mapper.CropTypeMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropTypeResourceIT {

    private static final String DEFAULT_CROP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CROP_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropTypeRepository cropTypeRepository;

    @Autowired
    private CropTypeMapper cropTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropTypeMockMvc;

    private CropType cropType;

    private CropType insertedCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropType createEntity() {
        return new CropType().cropName(DEFAULT_CROP_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropType createUpdatedEntity() {
        return new CropType().cropName(UPDATED_CROP_NAME);
    }

    @BeforeEach
    public void initTest() {
        cropType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCropType != null) {
            cropTypeRepository.delete(insertedCropType);
            insertedCropType = null;
        }
    }

    @Test
    @Transactional
    void createCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);
        var returnedCropTypeDTO = om.readValue(
            restCropTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropTypeDTO.class
        );

        // Validate the CropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCropType = cropTypeMapper.toEntity(returnedCropTypeDTO);
        assertCropTypeUpdatableFieldsEquals(returnedCropType, getPersistedCropType(returnedCropType));

        insertedCropType = returnedCropType;
    }

    @Test
    @Transactional
    void createCropTypeWithExistingId() throws Exception {
        // Create the CropType with an existing ID
        cropType.setId(1L);
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCropNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cropType.setCropName(null);

        // Create the CropType, which fails.
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        restCropTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCropTypes() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cropName").value(hasItem(DEFAULT_CROP_NAME)));
    }

    @Test
    @Transactional
    void getCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get the cropType
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropType.getId().intValue()))
            .andExpect(jsonPath("$.cropName").value(DEFAULT_CROP_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCropType() throws Exception {
        // Get the cropType
        restCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType
        CropType updatedCropType = cropTypeRepository.findById(cropType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropType are not directly saved in db
        em.detach(updatedCropType);
        updatedCropType.cropName(UPDATED_CROP_NAME);
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(updatedCropType);

        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropTypeToMatchAllProperties(updatedCropType);
    }

    @Test
    @Transactional
    void putNonExistingCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType using partial update
        CropType partialUpdatedCropType = new CropType();
        partialUpdatedCropType.setId(cropType.getId());

        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCropType, cropType), getPersistedCropType(cropType));
    }

    @Test
    @Transactional
    void fullUpdateCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType using partial update
        CropType partialUpdatedCropType = new CropType();
        partialUpdatedCropType.setId(cropType.getId());

        partialUpdatedCropType.cropName(UPDATED_CROP_NAME);

        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropTypeUpdatableFieldsEquals(partialUpdatedCropType, getPersistedCropType(partialUpdatedCropType));
    }

    @Test
    @Transactional
    void patchNonExistingCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // Create the CropType
        CropTypeDTO cropTypeDTO = cropTypeMapper.toDto(cropType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropType
        restCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CropType getPersistedCropType(CropType cropType) {
        return cropTypeRepository.findById(cropType.getId()).orElseThrow();
    }

    protected void assertPersistedCropTypeToMatchAllProperties(CropType expectedCropType) {
        assertCropTypeAllPropertiesEquals(expectedCropType, getPersistedCropType(expectedCropType));
    }

    protected void assertPersistedCropTypeToMatchUpdatableProperties(CropType expectedCropType) {
        assertCropTypeAllUpdatablePropertiesEquals(expectedCropType, getPersistedCropType(expectedCropType));
    }
}
