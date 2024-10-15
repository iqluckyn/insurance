package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.BusinessTypeAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.BusinessType;
import com.iql.policyadmin.repository.BusinessTypeRepository;
import com.iql.policyadmin.service.dto.BusinessTypeDTO;
import com.iql.policyadmin.service.mapper.BusinessTypeMapper;
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
 * Integration tests for the {@link BusinessTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessTypeMockMvc;

    private BusinessType businessType;

    private BusinessType insertedBusinessType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessType createEntity() {
        return new BusinessType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessType createUpdatedEntity() {
        return new BusinessType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        businessType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBusinessType != null) {
            businessTypeRepository.delete(insertedBusinessType);
            insertedBusinessType = null;
        }
    }

    @Test
    @Transactional
    void createBusinessType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);
        var returnedBusinessTypeDTO = om.readValue(
            restBusinessTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusinessTypeDTO.class
        );

        // Validate the BusinessType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBusinessType = businessTypeMapper.toEntity(returnedBusinessTypeDTO);
        assertBusinessTypeUpdatableFieldsEquals(returnedBusinessType, getPersistedBusinessType(returnedBusinessType));

        insertedBusinessType = returnedBusinessType;
    }

    @Test
    @Transactional
    void createBusinessTypeWithExistingId() throws Exception {
        // Create the BusinessType with an existing ID
        businessType.setId(1L);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessType.setName(null);

        // Create the BusinessType, which fails.
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        restBusinessTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessTypes() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        // Get all the businessTypeList
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        // Get the businessType
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, businessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBusinessType() throws Exception {
        // Get the businessType
        restBusinessTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType
        BusinessType updatedBusinessType = businessTypeRepository.findById(businessType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusinessType are not directly saved in db
        em.detach(updatedBusinessType);
        updatedBusinessType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(updatedBusinessType);

        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusinessTypeToMatchAllProperties(updatedBusinessType);
    }

    @Test
    @Transactional
    void putNonExistingBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.description(UPDATED_DESCRIPTION);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBusinessType, businessType),
            getPersistedBusinessType(businessType)
        );
    }

    @Test
    @Transactional
    void fullUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessTypeUpdatableFieldsEquals(partialUpdatedBusinessType, getPersistedBusinessType(partialUpdatedBusinessType));
    }

    @Test
    @Transactional
    void patchNonExistingBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndFlush(businessType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the businessType
        restBusinessTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return businessTypeRepository.count();
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

    protected BusinessType getPersistedBusinessType(BusinessType businessType) {
        return businessTypeRepository.findById(businessType.getId()).orElseThrow();
    }

    protected void assertPersistedBusinessTypeToMatchAllProperties(BusinessType expectedBusinessType) {
        assertBusinessTypeAllPropertiesEquals(expectedBusinessType, getPersistedBusinessType(expectedBusinessType));
    }

    protected void assertPersistedBusinessTypeToMatchUpdatableProperties(BusinessType expectedBusinessType) {
        assertBusinessTypeAllUpdatablePropertiesEquals(expectedBusinessType, getPersistedBusinessType(expectedBusinessType));
    }
}
