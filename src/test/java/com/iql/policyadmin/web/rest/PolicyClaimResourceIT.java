package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.PolicyClaimAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static com.iql.policyadmin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.PolicyClaim;
import com.iql.policyadmin.domain.enumeration.ClaimStatus;
import com.iql.policyadmin.repository.PolicyClaimRepository;
import com.iql.policyadmin.service.PolicyClaimService;
import com.iql.policyadmin.service.dto.PolicyClaimDTO;
import com.iql.policyadmin.service.mapper.PolicyClaimMapper;
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
 * Integration tests for the {@link PolicyClaimResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PolicyClaimResourceIT {

    private static final String DEFAULT_CLAIM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CLAIM_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CLAIM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLAIM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_AMOUNT_CLAIMED = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_CLAIMED = new BigDecimal(2);

    private static final ClaimStatus DEFAULT_STATUS = ClaimStatus.OPEN;
    private static final ClaimStatus UPDATED_STATUS = ClaimStatus.CLOSED;

    private static final String ENTITY_API_URL = "/api/policy-claims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PolicyClaimRepository policyClaimRepository;

    @Mock
    private PolicyClaimRepository policyClaimRepositoryMock;

    @Autowired
    private PolicyClaimMapper policyClaimMapper;

    @Mock
    private PolicyClaimService policyClaimServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyClaimMockMvc;

    private PolicyClaim policyClaim;

    private PolicyClaim insertedPolicyClaim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyClaim createEntity() {
        return new PolicyClaim()
            .claimNumber(DEFAULT_CLAIM_NUMBER)
            .claimDate(DEFAULT_CLAIM_DATE)
            .amountClaimed(DEFAULT_AMOUNT_CLAIMED)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyClaim createUpdatedEntity() {
        return new PolicyClaim()
            .claimNumber(UPDATED_CLAIM_NUMBER)
            .claimDate(UPDATED_CLAIM_DATE)
            .amountClaimed(UPDATED_AMOUNT_CLAIMED)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        policyClaim = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPolicyClaim != null) {
            policyClaimRepository.delete(insertedPolicyClaim);
            insertedPolicyClaim = null;
        }
    }

    @Test
    @Transactional
    void createPolicyClaim() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);
        var returnedPolicyClaimDTO = om.readValue(
            restPolicyClaimMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PolicyClaimDTO.class
        );

        // Validate the PolicyClaim in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPolicyClaim = policyClaimMapper.toEntity(returnedPolicyClaimDTO);
        assertPolicyClaimUpdatableFieldsEquals(returnedPolicyClaim, getPersistedPolicyClaim(returnedPolicyClaim));

        insertedPolicyClaim = returnedPolicyClaim;
    }

    @Test
    @Transactional
    void createPolicyClaimWithExistingId() throws Exception {
        // Create the PolicyClaim with an existing ID
        policyClaim.setId(1L);
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClaimNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policyClaim.setClaimNumber(null);

        // Create the PolicyClaim, which fails.
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        restPolicyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClaimDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policyClaim.setClaimDate(null);

        // Create the PolicyClaim, which fails.
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        restPolicyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountClaimedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policyClaim.setAmountClaimed(null);

        // Create the PolicyClaim, which fails.
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        restPolicyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policyClaim.setStatus(null);

        // Create the PolicyClaim, which fails.
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        restPolicyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPolicyClaims() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        // Get all the policyClaimList
        restPolicyClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyClaim.getId().intValue())))
            .andExpect(jsonPath("$.[*].claimNumber").value(hasItem(DEFAULT_CLAIM_NUMBER)))
            .andExpect(jsonPath("$.[*].claimDate").value(hasItem(DEFAULT_CLAIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].amountClaimed").value(hasItem(sameNumber(DEFAULT_AMOUNT_CLAIMED))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPolicyClaimsWithEagerRelationshipsIsEnabled() throws Exception {
        when(policyClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPolicyClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(policyClaimServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPolicyClaimsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(policyClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPolicyClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(policyClaimRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPolicyClaim() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        // Get the policyClaim
        restPolicyClaimMockMvc
            .perform(get(ENTITY_API_URL_ID, policyClaim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policyClaim.getId().intValue()))
            .andExpect(jsonPath("$.claimNumber").value(DEFAULT_CLAIM_NUMBER))
            .andExpect(jsonPath("$.claimDate").value(DEFAULT_CLAIM_DATE.toString()))
            .andExpect(jsonPath("$.amountClaimed").value(sameNumber(DEFAULT_AMOUNT_CLAIMED)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPolicyClaim() throws Exception {
        // Get the policyClaim
        restPolicyClaimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPolicyClaim() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyClaim
        PolicyClaim updatedPolicyClaim = policyClaimRepository.findById(policyClaim.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPolicyClaim are not directly saved in db
        em.detach(updatedPolicyClaim);
        updatedPolicyClaim
            .claimNumber(UPDATED_CLAIM_NUMBER)
            .claimDate(UPDATED_CLAIM_DATE)
            .amountClaimed(UPDATED_AMOUNT_CLAIMED)
            .status(UPDATED_STATUS);
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(updatedPolicyClaim);

        restPolicyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyClaimDTO))
            )
            .andExpect(status().isOk());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPolicyClaimToMatchAllProperties(updatedPolicyClaim);
    }

    @Test
    @Transactional
    void putNonExistingPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePolicyClaimWithPatch() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyClaim using partial update
        PolicyClaim partialUpdatedPolicyClaim = new PolicyClaim();
        partialUpdatedPolicyClaim.setId(policyClaim.getId());

        partialUpdatedPolicyClaim.claimDate(UPDATED_CLAIM_DATE).amountClaimed(UPDATED_AMOUNT_CLAIMED).status(UPDATED_STATUS);

        restPolicyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicyClaim))
            )
            .andExpect(status().isOk());

        // Validate the PolicyClaim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicyClaimUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPolicyClaim, policyClaim),
            getPersistedPolicyClaim(policyClaim)
        );
    }

    @Test
    @Transactional
    void fullUpdatePolicyClaimWithPatch() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyClaim using partial update
        PolicyClaim partialUpdatedPolicyClaim = new PolicyClaim();
        partialUpdatedPolicyClaim.setId(policyClaim.getId());

        partialUpdatedPolicyClaim
            .claimNumber(UPDATED_CLAIM_NUMBER)
            .claimDate(UPDATED_CLAIM_DATE)
            .amountClaimed(UPDATED_AMOUNT_CLAIMED)
            .status(UPDATED_STATUS);

        restPolicyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicyClaim))
            )
            .andExpect(status().isOk());

        // Validate the PolicyClaim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicyClaimUpdatableFieldsEquals(partialUpdatedPolicyClaim, getPersistedPolicyClaim(partialUpdatedPolicyClaim));
    }

    @Test
    @Transactional
    void patchNonExistingPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, policyClaimDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolicyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyClaim.setId(longCount.incrementAndGet());

        // Create the PolicyClaim
        PolicyClaimDTO policyClaimDTO = policyClaimMapper.toDto(policyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyClaimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(policyClaimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePolicyClaim() throws Exception {
        // Initialize the database
        insertedPolicyClaim = policyClaimRepository.saveAndFlush(policyClaim);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the policyClaim
        restPolicyClaimMockMvc
            .perform(delete(ENTITY_API_URL_ID, policyClaim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return policyClaimRepository.count();
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

    protected PolicyClaim getPersistedPolicyClaim(PolicyClaim policyClaim) {
        return policyClaimRepository.findById(policyClaim.getId()).orElseThrow();
    }

    protected void assertPersistedPolicyClaimToMatchAllProperties(PolicyClaim expectedPolicyClaim) {
        assertPolicyClaimAllPropertiesEquals(expectedPolicyClaim, getPersistedPolicyClaim(expectedPolicyClaim));
    }

    protected void assertPersistedPolicyClaimToMatchUpdatableProperties(PolicyClaim expectedPolicyClaim) {
        assertPolicyClaimAllUpdatablePropertiesEquals(expectedPolicyClaim, getPersistedPolicyClaim(expectedPolicyClaim));
    }
}
