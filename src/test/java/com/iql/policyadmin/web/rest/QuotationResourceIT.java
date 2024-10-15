package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.QuotationAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static com.iql.policyadmin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.Quotation;
import com.iql.policyadmin.repository.QuotationRepository;
import com.iql.policyadmin.service.QuotationService;
import com.iql.policyadmin.service.dto.QuotationDTO;
import com.iql.policyadmin.service.mapper.QuotationMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link QuotationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuotationResourceIT {

    private static final LocalDate DEFAULT_START_OF_RISK_PERIOD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_OF_RISK_PERIOD = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LENGTH_OF_RISK_PERIOD = 1;
    private static final Integer UPDATED_LENGTH_OF_RISK_PERIOD = 2;

    private static final Integer DEFAULT_DEPTH = 1;
    private static final Integer UPDATED_DEPTH = 2;

    private static final Integer DEFAULT_CLAIMS_FREQUENCY = 1;
    private static final Integer UPDATED_CLAIMS_FREQUENCY = 2;

    private static final BigDecimal DEFAULT_INSURED_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSURED_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BEST_PREMIUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_BEST_PREMIUM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INSURED_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSURED_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INSURED_PREMIUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSURED_PREMIUM = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/quotations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotationRepository quotationRepository;

    @Mock
    private QuotationRepository quotationRepositoryMock;

    @Autowired
    private QuotationMapper quotationMapper;

    @Mock
    private QuotationService quotationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    private Quotation insertedQuotation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createEntity() {
        return new Quotation()
            .startOfRiskPeriod(DEFAULT_START_OF_RISK_PERIOD)
            .lengthOfRiskPeriod(DEFAULT_LENGTH_OF_RISK_PERIOD)
            .depth(DEFAULT_DEPTH)
            .claimsFrequency(DEFAULT_CLAIMS_FREQUENCY)
            .insuredValue(DEFAULT_INSURED_VALUE)
            .bestPremium(DEFAULT_BEST_PREMIUM)
            .insuredRate(DEFAULT_INSURED_RATE)
            .insuredPremium(DEFAULT_INSURED_PREMIUM)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quotation createUpdatedEntity() {
        return new Quotation()
            .startOfRiskPeriod(UPDATED_START_OF_RISK_PERIOD)
            .lengthOfRiskPeriod(UPDATED_LENGTH_OF_RISK_PERIOD)
            .depth(UPDATED_DEPTH)
            .claimsFrequency(UPDATED_CLAIMS_FREQUENCY)
            .insuredValue(UPDATED_INSURED_VALUE)
            .bestPremium(UPDATED_BEST_PREMIUM)
            .insuredRate(UPDATED_INSURED_RATE)
            .insuredPremium(UPDATED_INSURED_PREMIUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        quotation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuotation != null) {
            quotationRepository.delete(insertedQuotation);
            insertedQuotation = null;
        }
    }

    @Test
    @Transactional
    void createQuotation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);
        var returnedQuotationDTO = om.readValue(
            restQuotationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotationDTO.class
        );

        // Validate the Quotation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotation = quotationMapper.toEntity(returnedQuotationDTO);
        assertQuotationUpdatableFieldsEquals(returnedQuotation, getPersistedQuotation(returnedQuotation));

        insertedQuotation = returnedQuotation;
    }

    @Test
    @Transactional
    void createQuotationWithExistingId() throws Exception {
        // Create the Quotation with an existing ID
        quotation.setId(1L);
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartOfRiskPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setStartOfRiskPeriod(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLengthOfRiskPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setLengthOfRiskPeriod(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setDepth(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClaimsFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setClaimsFrequency(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuredValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setInsuredValue(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBestPremiumIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setBestPremium(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuredRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setInsuredRate(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuredPremiumIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotation.setInsuredPremium(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotations() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].startOfRiskPeriod").value(hasItem(DEFAULT_START_OF_RISK_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].lengthOfRiskPeriod").value(hasItem(DEFAULT_LENGTH_OF_RISK_PERIOD)))
            .andExpect(jsonPath("$.[*].depth").value(hasItem(DEFAULT_DEPTH)))
            .andExpect(jsonPath("$.[*].claimsFrequency").value(hasItem(DEFAULT_CLAIMS_FREQUENCY)))
            .andExpect(jsonPath("$.[*].insuredValue").value(hasItem(sameNumber(DEFAULT_INSURED_VALUE))))
            .andExpect(jsonPath("$.[*].bestPremium").value(hasItem(sameNumber(DEFAULT_BEST_PREMIUM))))
            .andExpect(jsonPath("$.[*].insuredRate").value(hasItem(sameNumber(DEFAULT_INSURED_RATE))))
            .andExpect(jsonPath("$.[*].insuredPremium").value(hasItem(sameNumber(DEFAULT_INSURED_PREMIUM))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quotationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuotationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quotationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quotationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuotationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quotationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc
            .perform(get(ENTITY_API_URL_ID, quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.startOfRiskPeriod").value(DEFAULT_START_OF_RISK_PERIOD.toString()))
            .andExpect(jsonPath("$.lengthOfRiskPeriod").value(DEFAULT_LENGTH_OF_RISK_PERIOD))
            .andExpect(jsonPath("$.depth").value(DEFAULT_DEPTH))
            .andExpect(jsonPath("$.claimsFrequency").value(DEFAULT_CLAIMS_FREQUENCY))
            .andExpect(jsonPath("$.insuredValue").value(sameNumber(DEFAULT_INSURED_VALUE)))
            .andExpect(jsonPath("$.bestPremium").value(sameNumber(DEFAULT_BEST_PREMIUM)))
            .andExpect(jsonPath("$.insuredRate").value(sameNumber(DEFAULT_INSURED_RATE)))
            .andExpect(jsonPath("$.insuredPremium").value(sameNumber(DEFAULT_INSURED_PREMIUM)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuotation() throws Exception {
        // Get the quotation
        restQuotationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findById(quotation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotation are not directly saved in db
        em.detach(updatedQuotation);
        updatedQuotation
            .startOfRiskPeriod(UPDATED_START_OF_RISK_PERIOD)
            .lengthOfRiskPeriod(UPDATED_LENGTH_OF_RISK_PERIOD)
            .depth(UPDATED_DEPTH)
            .claimsFrequency(UPDATED_CLAIMS_FREQUENCY)
            .insuredValue(UPDATED_INSURED_VALUE)
            .bestPremium(UPDATED_BEST_PREMIUM)
            .insuredRate(UPDATED_INSURED_RATE)
            .insuredPremium(UPDATED_INSURED_PREMIUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        QuotationDTO quotationDTO = quotationMapper.toDto(updatedQuotation);

        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotationToMatchAllProperties(updatedQuotation);
    }

    @Test
    @Transactional
    void putNonExistingQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .startOfRiskPeriod(UPDATED_START_OF_RISK_PERIOD)
            .lengthOfRiskPeriod(UPDATED_LENGTH_OF_RISK_PERIOD)
            .claimsFrequency(UPDATED_CLAIMS_FREQUENCY)
            .insuredValue(UPDATED_INSURED_VALUE)
            .insuredPremium(UPDATED_INSURED_PREMIUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotation, quotation),
            getPersistedQuotation(quotation)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotationWithPatch() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotation using partial update
        Quotation partialUpdatedQuotation = new Quotation();
        partialUpdatedQuotation.setId(quotation.getId());

        partialUpdatedQuotation
            .startOfRiskPeriod(UPDATED_START_OF_RISK_PERIOD)
            .lengthOfRiskPeriod(UPDATED_LENGTH_OF_RISK_PERIOD)
            .depth(UPDATED_DEPTH)
            .claimsFrequency(UPDATED_CLAIMS_FREQUENCY)
            .insuredValue(UPDATED_INSURED_VALUE)
            .bestPremium(UPDATED_BEST_PREMIUM)
            .insuredRate(UPDATED_INSURED_RATE)
            .insuredPremium(UPDATED_INSURED_PREMIUM)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotation))
            )
            .andExpect(status().isOk());

        // Validate the Quotation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationUpdatableFieldsEquals(partialUpdatedQuotation, getPersistedQuotation(partialUpdatedQuotation));
    }

    @Test
    @Transactional
    void patchNonExistingQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotation.setId(longCount.incrementAndGet());

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quotation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotation() throws Exception {
        // Initialize the database
        insertedQuotation = quotationRepository.saveAndFlush(quotation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotation
        restQuotationMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotationRepository.count();
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

    protected Quotation getPersistedQuotation(Quotation quotation) {
        return quotationRepository.findById(quotation.getId()).orElseThrow();
    }

    protected void assertPersistedQuotationToMatchAllProperties(Quotation expectedQuotation) {
        assertQuotationAllPropertiesEquals(expectedQuotation, getPersistedQuotation(expectedQuotation));
    }

    protected void assertPersistedQuotationToMatchUpdatableProperties(Quotation expectedQuotation) {
        assertQuotationAllUpdatablePropertiesEquals(expectedQuotation, getPersistedQuotation(expectedQuotation));
    }
}
