package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.FarmerAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.repository.FarmerRepository;
import com.iql.policyadmin.repository.UserRepository;
import com.iql.policyadmin.service.FarmerService;
import com.iql.policyadmin.service.dto.FarmerDTO;
import com.iql.policyadmin.service.mapper.FarmerMapper;
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
 * Integration tests for the {@link FarmerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FarmerResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_REGISTRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private FarmerRepository farmerRepositoryMock;

    @Autowired
    private FarmerMapper farmerMapper;

    @Mock
    private FarmerService farmerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerMockMvc;

    private Farmer farmer;

    private Farmer insertedFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmer createEntity() {
        return new Farmer()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .email(DEFAULT_EMAIL)
            .position(DEFAULT_POSITION)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE)
            .country(DEFAULT_COUNTRY)
            .postalCode(DEFAULT_POSTAL_CODE)
            .registrationDate(DEFAULT_REGISTRATION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmer createUpdatedEntity() {
        return new Farmer()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .registrationDate(UPDATED_REGISTRATION_DATE);
    }

    @BeforeEach
    public void initTest() {
        farmer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFarmer != null) {
            farmerRepository.delete(insertedFarmer);
            insertedFarmer = null;
        }
    }

    @Test
    @Transactional
    void createFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);
        var returnedFarmerDTO = om.readValue(
            restFarmerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmerDTO.class
        );

        // Validate the Farmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFarmer = farmerMapper.toEntity(returnedFarmerDTO);
        assertFarmerUpdatableFieldsEquals(returnedFarmer, getPersistedFarmer(returnedFarmer));

        insertedFarmer = returnedFarmer;
    }

    @Test
    @Transactional
    void createFarmerWithExistingId() throws Exception {
        // Create the Farmer with an existing ID
        farmer.setId(1L);
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmer.setFirstname(null);

        // Create the Farmer, which fails.
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmer.setLastname(null);

        // Create the Farmer, which fails.
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmer.setEmail(null);

        // Create the Farmer, which fails.
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmer.setPosition(null);

        // Create the Farmer, which fails.
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFarmers() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFarmersWithEagerRelationshipsIsEnabled() throws Exception {
        when(farmerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFarmerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(farmerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFarmersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(farmerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFarmerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(farmerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get the farmer
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, farmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmer.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFarmer() throws Exception {
        // Get the farmer
        restFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer
        Farmer updatedFarmer = farmerRepository.findById(farmer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFarmer are not directly saved in db
        em.detach(updatedFarmer);
        updatedFarmer
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .registrationDate(UPDATED_REGISTRATION_DATE);
        FarmerDTO farmerDTO = farmerMapper.toDto(updatedFarmer);

        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerToMatchAllProperties(updatedFarmer);
    }

    @Test
    @Transactional
    void putNonExistingFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer using partial update
        Farmer partialUpdatedFarmer = new Farmer();
        partialUpdatedFarmer.setId(farmer.getId());

        partialUpdatedFarmer
            .firstname(UPDATED_FIRSTNAME)
            .position(UPDATED_POSITION)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE);

        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmer))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFarmer, farmer), getPersistedFarmer(farmer));
    }

    @Test
    @Transactional
    void fullUpdateFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer using partial update
        Farmer partialUpdatedFarmer = new Farmer();
        partialUpdatedFarmer.setId(farmer.getId());

        partialUpdatedFarmer
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .email(UPDATED_EMAIL)
            .position(UPDATED_POSITION)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .registrationDate(UPDATED_REGISTRATION_DATE);

        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmer))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerUpdatableFieldsEquals(partialUpdatedFarmer, getPersistedFarmer(partialUpdatedFarmer));
    }

    @Test
    @Transactional
    void patchNonExistingFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // Create the Farmer
        FarmerDTO farmerDTO = farmerMapper.toDto(farmer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(farmerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmer
        restFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerRepository.count();
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

    protected Farmer getPersistedFarmer(Farmer farmer) {
        return farmerRepository.findById(farmer.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerToMatchAllProperties(Farmer expectedFarmer) {
        assertFarmerAllPropertiesEquals(expectedFarmer, getPersistedFarmer(expectedFarmer));
    }

    protected void assertPersistedFarmerToMatchUpdatableProperties(Farmer expectedFarmer) {
        assertFarmerAllUpdatablePropertiesEquals(expectedFarmer, getPersistedFarmer(expectedFarmer));
    }
}
