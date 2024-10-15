package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.QuotationStatusAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.QuotationStatus;
import com.iql.policyadmin.repository.QuotationStatusRepository;
import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import com.iql.policyadmin.service.mapper.QuotationStatusMapper;
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
 * Integration tests for the {@link QuotationStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuotationStatusResourceIT {

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/quotation-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotationStatusRepository quotationStatusRepository;

    @Autowired
    private QuotationStatusMapper quotationStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationStatusMockMvc;

    private QuotationStatus quotationStatus;

    private QuotationStatus insertedQuotationStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationStatus createEntity() {
        return new QuotationStatus()
            .statusName(DEFAULT_STATUS_NAME)
            .statusCode(DEFAULT_STATUS_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationStatus createUpdatedEntity() {
        return new QuotationStatus()
            .statusName(UPDATED_STATUS_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        quotationStatus = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuotationStatus != null) {
            quotationStatusRepository.delete(insertedQuotationStatus);
            insertedQuotationStatus = null;
        }
    }

    @Test
    @Transactional
    void createQuotationStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);
        var returnedQuotationStatusDTO = om.readValue(
            restQuotationStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotationStatusDTO.class
        );

        // Validate the QuotationStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotationStatus = quotationStatusMapper.toEntity(returnedQuotationStatusDTO);
        assertQuotationStatusUpdatableFieldsEquals(returnedQuotationStatus, getPersistedQuotationStatus(returnedQuotationStatus));

        insertedQuotationStatus = returnedQuotationStatus;
    }

    @Test
    @Transactional
    void createQuotationStatusWithExistingId() throws Exception {
        // Create the QuotationStatus with an existing ID
        quotationStatus.setId(1L);
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotationStatus.setStatusName(null);

        // Create the QuotationStatus, which fails.
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        restQuotationStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotationStatus.setStatusCode(null);

        // Create the QuotationStatus, which fails.
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        restQuotationStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotationStatuses() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        // Get all the quotationStatusList
        restQuotationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getQuotationStatus() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        // Get the quotationStatus
        restQuotationStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, quotationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotationStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuotationStatus() throws Exception {
        // Get the quotationStatus
        restQuotationStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotationStatus() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationStatus
        QuotationStatus updatedQuotationStatus = quotationStatusRepository.findById(quotationStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotationStatus are not directly saved in db
        em.detach(updatedQuotationStatus);
        updatedQuotationStatus
            .statusName(UPDATED_STATUS_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(updatedQuotationStatus);

        restQuotationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotationStatusToMatchAllProperties(updatedQuotationStatus);
    }

    @Test
    @Transactional
    void putNonExistingQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotationStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotationStatusWithPatch() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationStatus using partial update
        QuotationStatus partialUpdatedQuotationStatus = new QuotationStatus();
        partialUpdatedQuotationStatus.setId(quotationStatus.getId());

        partialUpdatedQuotationStatus.statusName(UPDATED_STATUS_NAME).statusCode(UPDATED_STATUS_CODE).isActive(UPDATED_IS_ACTIVE);

        restQuotationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotationStatus))
            )
            .andExpect(status().isOk());

        // Validate the QuotationStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotationStatus, quotationStatus),
            getPersistedQuotationStatus(quotationStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotationStatusWithPatch() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotationStatus using partial update
        QuotationStatus partialUpdatedQuotationStatus = new QuotationStatus();
        partialUpdatedQuotationStatus.setId(quotationStatus.getId());

        partialUpdatedQuotationStatus
            .statusName(UPDATED_STATUS_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restQuotationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotationStatus))
            )
            .andExpect(status().isOk());

        // Validate the QuotationStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotationStatusUpdatableFieldsEquals(
            partialUpdatedQuotationStatus,
            getPersistedQuotationStatus(partialUpdatedQuotationStatus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotationStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotationStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotationStatus.setId(longCount.incrementAndGet());

        // Create the QuotationStatus
        QuotationStatusDTO quotationStatusDTO = quotationStatusMapper.toDto(quotationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotationStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotationStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotationStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotationStatus() throws Exception {
        // Initialize the database
        insertedQuotationStatus = quotationStatusRepository.saveAndFlush(quotationStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotationStatus
        restQuotationStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotationStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotationStatusRepository.count();
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

    protected QuotationStatus getPersistedQuotationStatus(QuotationStatus quotationStatus) {
        return quotationStatusRepository.findById(quotationStatus.getId()).orElseThrow();
    }

    protected void assertPersistedQuotationStatusToMatchAllProperties(QuotationStatus expectedQuotationStatus) {
        assertQuotationStatusAllPropertiesEquals(expectedQuotationStatus, getPersistedQuotationStatus(expectedQuotationStatus));
    }

    protected void assertPersistedQuotationStatusToMatchUpdatableProperties(QuotationStatus expectedQuotationStatus) {
        assertQuotationStatusAllUpdatablePropertiesEquals(expectedQuotationStatus, getPersistedQuotationStatus(expectedQuotationStatus));
    }
}
