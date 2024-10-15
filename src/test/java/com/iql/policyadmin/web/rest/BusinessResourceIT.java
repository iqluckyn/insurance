package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.BusinessAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.Business;
import com.iql.policyadmin.repository.BusinessRepository;
import com.iql.policyadmin.service.BusinessService;
import com.iql.policyadmin.service.dto.BusinessDTO;
import com.iql.policyadmin.service.mapper.BusinessMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BusinessResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BusinessResourceIT {

    private static final String DEFAULT_REGISTERED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REGISTERED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VAT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/businesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusinessRepository businessRepository;

    @Mock
    private BusinessRepository businessRepositoryMock;

    @Autowired
    private BusinessMapper businessMapper;

    @Mock
    private BusinessService businessServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessMockMvc;

    private Business business;

    private Business insertedBusiness;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Business createEntity() {
        return new Business()
            .registeredName(DEFAULT_REGISTERED_NAME)
            .organisationName(DEFAULT_ORGANISATION_NAME)
            .vatNumber(DEFAULT_VAT_NUMBER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Business createUpdatedEntity() {
        return new Business()
            .registeredName(UPDATED_REGISTERED_NAME)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .vatNumber(UPDATED_VAT_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        business = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBusiness != null) {
            businessRepository.delete(insertedBusiness);
            insertedBusiness = null;
        }
    }

    @Test
    @Transactional
    void createBusiness() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);
        var returnedBusinessDTO = om.readValue(
            restBusinessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusinessDTO.class
        );

        // Validate the Business in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBusiness = businessMapper.toEntity(returnedBusinessDTO);
        assertBusinessUpdatableFieldsEquals(returnedBusiness, getPersistedBusiness(returnedBusiness));

        insertedBusiness = returnedBusiness;
    }

    @Test
    @Transactional
    void createBusinessWithExistingId() throws Exception {
        // Create the Business with an existing ID
        business.setId(1L);
        BusinessDTO businessDTO = businessMapper.toDto(business);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegisteredNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        business.setRegisteredName(null);

        // Create the Business, which fails.
        BusinessDTO businessDTO = businessMapper.toDto(business);

        restBusinessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinesses() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        // Get all the businessList
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(business.getId().intValue())))
            .andExpect(jsonPath("$.[*].registeredName").value(hasItem(DEFAULT_REGISTERED_NAME)))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME)))
            .andExpect(jsonPath("$.[*].vatNumber").value(hasItem(DEFAULT_VAT_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessesWithEagerRelationshipsIsEnabled() throws Exception {
        when(businessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(businessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(businessRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBusiness() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        // Get the business
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL_ID, business.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(business.getId().intValue()))
            .andExpect(jsonPath("$.registeredName").value(DEFAULT_REGISTERED_NAME))
            .andExpect(jsonPath("$.organisationName").value(DEFAULT_ORGANISATION_NAME))
            .andExpect(jsonPath("$.vatNumber").value(DEFAULT_VAT_NUMBER))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBusiness() throws Exception {
        // Get the business
        restBusinessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusiness() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the business
        Business updatedBusiness = businessRepository.findById(business.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusiness are not directly saved in db
        em.detach(updatedBusiness);
        updatedBusiness
            .registeredName(UPDATED_REGISTERED_NAME)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .vatNumber(UPDATED_VAT_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BusinessDTO businessDTO = businessMapper.toDto(updatedBusiness);

        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessDTO))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusinessToMatchAllProperties(updatedBusiness);
    }

    @Test
    @Transactional
    void putNonExistingBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the business using partial update
        Business partialUpdatedBusiness = new Business();
        partialUpdatedBusiness.setId(business.getId());

        partialUpdatedBusiness.organisationName(UPDATED_ORGANISATION_NAME).vatNumber(UPDATED_VAT_NUMBER).updatedAt(UPDATED_UPDATED_AT);

        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusiness))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBusiness, business), getPersistedBusiness(business));
    }

    @Test
    @Transactional
    void fullUpdateBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the business using partial update
        Business partialUpdatedBusiness = new Business();
        partialUpdatedBusiness.setId(business.getId());

        partialUpdatedBusiness
            .registeredName(UPDATED_REGISTERED_NAME)
            .organisationName(UPDATED_ORGANISATION_NAME)
            .vatNumber(UPDATED_VAT_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusiness))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessUpdatableFieldsEquals(partialUpdatedBusiness, getPersistedBusiness(partialUpdatedBusiness));
    }

    @Test
    @Transactional
    void patchNonExistingBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(businessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Business in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusiness() throws Exception {
        // Initialize the database
        insertedBusiness = businessRepository.saveAndFlush(business);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the business
        restBusinessMockMvc
            .perform(delete(ENTITY_API_URL_ID, business.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return businessRepository.count();
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

    protected Business getPersistedBusiness(Business business) {
        return businessRepository.findById(business.getId()).orElseThrow();
    }

    protected void assertPersistedBusinessToMatchAllProperties(Business expectedBusiness) {
        assertBusinessAllPropertiesEquals(expectedBusiness, getPersistedBusiness(expectedBusiness));
    }

    protected void assertPersistedBusinessToMatchUpdatableProperties(Business expectedBusiness) {
        assertBusinessAllUpdatablePropertiesEquals(expectedBusiness, getPersistedBusiness(expectedBusiness));
    }
}
