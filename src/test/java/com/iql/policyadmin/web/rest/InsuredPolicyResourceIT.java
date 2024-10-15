package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.InsuredPolicyAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static com.iql.policyadmin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.InsuredPolicy;
import com.iql.policyadmin.domain.enumeration.PolicyStatus;
import com.iql.policyadmin.repository.InsuredPolicyRepository;
import com.iql.policyadmin.service.InsuredPolicyService;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import com.iql.policyadmin.service.mapper.InsuredPolicyMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InsuredPolicyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsuredPolicyResourceIT {

    private static final String DEFAULT_POLICY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PREMIUM_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREMIUM_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COVERAGE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_COVERAGE_AMOUNT = new BigDecimal(2);

    private static final PolicyStatus DEFAULT_STATUS = PolicyStatus.PENDING;
    private static final PolicyStatus UPDATED_STATUS = PolicyStatus.ACTIVE;

    private static final String ENTITY_API_URL = "/api/insured-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuredPolicyRepository insuredPolicyRepository;

    @Mock
    private InsuredPolicyRepository insuredPolicyRepositoryMock;

    @Autowired
    private InsuredPolicyMapper insuredPolicyMapper;

    @Mock
    private InsuredPolicyService insuredPolicyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuredPolicyMockMvc;

    private InsuredPolicy insuredPolicy;

    private InsuredPolicy insertedInsuredPolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuredPolicy createEntity() {
        return new InsuredPolicy()
            .policyNumber(DEFAULT_POLICY_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .premiumAmount(DEFAULT_PREMIUM_AMOUNT)
            .coverageAmount(DEFAULT_COVERAGE_AMOUNT)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuredPolicy createUpdatedEntity() {
        return new InsuredPolicy()
            .policyNumber(UPDATED_POLICY_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premiumAmount(UPDATED_PREMIUM_AMOUNT)
            .coverageAmount(UPDATED_COVERAGE_AMOUNT)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        insuredPolicy = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsuredPolicy != null) {
            insuredPolicyRepository.delete(insertedInsuredPolicy);
            insertedInsuredPolicy = null;
        }
    }

    @Test
    @Transactional
    void createInsuredPolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);
        var returnedInsuredPolicyDTO = om.readValue(
            restInsuredPolicyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuredPolicyDTO.class
        );

        // Validate the InsuredPolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInsuredPolicy = insuredPolicyMapper.toEntity(returnedInsuredPolicyDTO);
        assertInsuredPolicyUpdatableFieldsEquals(returnedInsuredPolicy, getPersistedInsuredPolicy(returnedInsuredPolicy));

        insertedInsuredPolicy = returnedInsuredPolicy;
    }

    @Test
    @Transactional
    void createInsuredPolicyWithExistingId() throws Exception {
        // Create the InsuredPolicy with an existing ID
        insuredPolicy.setId(1L);
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPolicyNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setPolicyNumber(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setStartDate(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setEndDate(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPremiumAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setPremiumAmount(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoverageAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setCoverageAmount(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuredPolicy.setStatus(null);

        // Create the InsuredPolicy, which fails.
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        restInsuredPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsuredPolicies() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        // Get all the insuredPolicyList
        restInsuredPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuredPolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyNumber").value(hasItem(DEFAULT_POLICY_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].premiumAmount").value(hasItem(sameNumber(DEFAULT_PREMIUM_AMOUNT))))
            .andExpect(jsonPath("$.[*].coverageAmount").value(hasItem(sameNumber(DEFAULT_COVERAGE_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuredPoliciesWithEagerRelationshipsIsEnabled() throws Exception {
        when(insuredPolicyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuredPolicyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insuredPolicyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuredPoliciesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insuredPolicyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuredPolicyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insuredPolicyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsuredPolicy() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        // Get the insuredPolicy
        restInsuredPolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, insuredPolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuredPolicy.getId().intValue()))
            .andExpect(jsonPath("$.policyNumber").value(DEFAULT_POLICY_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.premiumAmount").value(sameNumber(DEFAULT_PREMIUM_AMOUNT)))
            .andExpect(jsonPath("$.coverageAmount").value(sameNumber(DEFAULT_COVERAGE_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInsuredPolicy() throws Exception {
        // Get the insuredPolicy
        restInsuredPolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuredPolicy() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuredPolicy
        InsuredPolicy updatedInsuredPolicy = insuredPolicyRepository.findById(insuredPolicy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsuredPolicy are not directly saved in db
        em.detach(updatedInsuredPolicy);
        updatedInsuredPolicy
            .policyNumber(UPDATED_POLICY_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premiumAmount(UPDATED_PREMIUM_AMOUNT)
            .coverageAmount(UPDATED_COVERAGE_AMOUNT)
            .status(UPDATED_STATUS);
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(updatedInsuredPolicy);

        restInsuredPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuredPolicyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuredPolicyDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuredPolicyToMatchAllProperties(updatedInsuredPolicy);
    }

    @Test
    @Transactional
    void putNonExistingInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuredPolicyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuredPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuredPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuredPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuredPolicy using partial update
        InsuredPolicy partialUpdatedInsuredPolicy = new InsuredPolicy();
        partialUpdatedInsuredPolicy.setId(insuredPolicy.getId());

        partialUpdatedInsuredPolicy
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premiumAmount(UPDATED_PREMIUM_AMOUNT)
            .coverageAmount(UPDATED_COVERAGE_AMOUNT)
            .status(UPDATED_STATUS);

        restInsuredPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuredPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuredPolicy))
            )
            .andExpect(status().isOk());

        // Validate the InsuredPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuredPolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuredPolicy, insuredPolicy),
            getPersistedInsuredPolicy(insuredPolicy)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuredPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuredPolicy using partial update
        InsuredPolicy partialUpdatedInsuredPolicy = new InsuredPolicy();
        partialUpdatedInsuredPolicy.setId(insuredPolicy.getId());

        partialUpdatedInsuredPolicy
            .policyNumber(UPDATED_POLICY_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .premiumAmount(UPDATED_PREMIUM_AMOUNT)
            .coverageAmount(UPDATED_COVERAGE_AMOUNT)
            .status(UPDATED_STATUS);

        restInsuredPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuredPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuredPolicy))
            )
            .andExpect(status().isOk());

        // Validate the InsuredPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuredPolicyUpdatableFieldsEquals(partialUpdatedInsuredPolicy, getPersistedInsuredPolicy(partialUpdatedInsuredPolicy));
    }

    @Test
    @Transactional
    void patchNonExistingInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuredPolicyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuredPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuredPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuredPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuredPolicy.setId(longCount.incrementAndGet());

        // Create the InsuredPolicy
        InsuredPolicyDTO insuredPolicyDTO = insuredPolicyMapper.toDto(insuredPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuredPolicyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insuredPolicyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuredPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuredPolicy() throws Exception {
        // Initialize the database
        insertedInsuredPolicy = insuredPolicyRepository.saveAndFlush(insuredPolicy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuredPolicy
        restInsuredPolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuredPolicy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuredPolicyRepository.count();
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

    protected InsuredPolicy getPersistedInsuredPolicy(InsuredPolicy insuredPolicy) {
        return insuredPolicyRepository.findById(insuredPolicy.getId()).orElseThrow();
    }

    protected void assertPersistedInsuredPolicyToMatchAllProperties(InsuredPolicy expectedInsuredPolicy) {
        assertInsuredPolicyAllPropertiesEquals(expectedInsuredPolicy, getPersistedInsuredPolicy(expectedInsuredPolicy));
    }

    protected void assertPersistedInsuredPolicyToMatchUpdatableProperties(InsuredPolicy expectedInsuredPolicy) {
        assertInsuredPolicyAllUpdatablePropertiesEquals(expectedInsuredPolicy, getPersistedInsuredPolicy(expectedInsuredPolicy));
    }
}
