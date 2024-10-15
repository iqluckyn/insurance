package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.PolicyComponentAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static com.iql.policyadmin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.PolicyComponent;
import com.iql.policyadmin.repository.PolicyComponentRepository;
import com.iql.policyadmin.service.PolicyComponentService;
import com.iql.policyadmin.service.dto.PolicyComponentDTO;
import com.iql.policyadmin.service.mapper.PolicyComponentMapper;
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
 * Integration tests for the {@link PolicyComponentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PolicyComponentResourceIT {

    private static final BigDecimal DEFAULT_COMPONENT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMPONENT_VALUE = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/policy-components";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PolicyComponentRepository policyComponentRepository;

    @Mock
    private PolicyComponentRepository policyComponentRepositoryMock;

    @Autowired
    private PolicyComponentMapper policyComponentMapper;

    @Mock
    private PolicyComponentService policyComponentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyComponentMockMvc;

    private PolicyComponent policyComponent;

    private PolicyComponent insertedPolicyComponent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyComponent createEntity() {
        return new PolicyComponent().componentValue(DEFAULT_COMPONENT_VALUE).createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyComponent createUpdatedEntity() {
        return new PolicyComponent().componentValue(UPDATED_COMPONENT_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        policyComponent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPolicyComponent != null) {
            policyComponentRepository.delete(insertedPolicyComponent);
            insertedPolicyComponent = null;
        }
    }

    @Test
    @Transactional
    void createPolicyComponent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);
        var returnedPolicyComponentDTO = om.readValue(
            restPolicyComponentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyComponentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PolicyComponentDTO.class
        );

        // Validate the PolicyComponent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPolicyComponent = policyComponentMapper.toEntity(returnedPolicyComponentDTO);
        assertPolicyComponentUpdatableFieldsEquals(returnedPolicyComponent, getPersistedPolicyComponent(returnedPolicyComponent));

        insertedPolicyComponent = returnedPolicyComponent;
    }

    @Test
    @Transactional
    void createPolicyComponentWithExistingId() throws Exception {
        // Create the PolicyComponent with an existing ID
        policyComponent.setId(1L);
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyComponentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyComponentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkComponentValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        policyComponent.setComponentValue(null);

        // Create the PolicyComponent, which fails.
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        restPolicyComponentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyComponentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPolicyComponents() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        // Get all the policyComponentList
        restPolicyComponentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentValue").value(hasItem(sameNumber(DEFAULT_COMPONENT_VALUE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPolicyComponentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(policyComponentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPolicyComponentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(policyComponentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPolicyComponentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(policyComponentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPolicyComponentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(policyComponentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPolicyComponent() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        // Get the policyComponent
        restPolicyComponentMockMvc
            .perform(get(ENTITY_API_URL_ID, policyComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policyComponent.getId().intValue()))
            .andExpect(jsonPath("$.componentValue").value(sameNumber(DEFAULT_COMPONENT_VALUE)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPolicyComponent() throws Exception {
        // Get the policyComponent
        restPolicyComponentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPolicyComponent() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyComponent
        PolicyComponent updatedPolicyComponent = policyComponentRepository.findById(policyComponent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPolicyComponent are not directly saved in db
        em.detach(updatedPolicyComponent);
        updatedPolicyComponent.componentValue(UPDATED_COMPONENT_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(updatedPolicyComponent);

        restPolicyComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyComponentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyComponentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPolicyComponentToMatchAllProperties(updatedPolicyComponent);
    }

    @Test
    @Transactional
    void putNonExistingPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyComponentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(policyComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(policyComponentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePolicyComponentWithPatch() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyComponent using partial update
        PolicyComponent partialUpdatedPolicyComponent = new PolicyComponent();
        partialUpdatedPolicyComponent.setId(policyComponent.getId());

        partialUpdatedPolicyComponent.createdAt(UPDATED_CREATED_AT);

        restPolicyComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyComponent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicyComponent))
            )
            .andExpect(status().isOk());

        // Validate the PolicyComponent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicyComponentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPolicyComponent, policyComponent),
            getPersistedPolicyComponent(policyComponent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePolicyComponentWithPatch() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the policyComponent using partial update
        PolicyComponent partialUpdatedPolicyComponent = new PolicyComponent();
        partialUpdatedPolicyComponent.setId(policyComponent.getId());

        partialUpdatedPolicyComponent.componentValue(UPDATED_COMPONENT_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restPolicyComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyComponent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPolicyComponent))
            )
            .andExpect(status().isOk());

        // Validate the PolicyComponent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPolicyComponentUpdatableFieldsEquals(
            partialUpdatedPolicyComponent,
            getPersistedPolicyComponent(partialUpdatedPolicyComponent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, policyComponentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policyComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(policyComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolicyComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        policyComponent.setId(longCount.incrementAndGet());

        // Create the PolicyComponent
        PolicyComponentDTO policyComponentDTO = policyComponentMapper.toDto(policyComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyComponentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(policyComponentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePolicyComponent() throws Exception {
        // Initialize the database
        insertedPolicyComponent = policyComponentRepository.saveAndFlush(policyComponent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the policyComponent
        restPolicyComponentMockMvc
            .perform(delete(ENTITY_API_URL_ID, policyComponent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return policyComponentRepository.count();
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

    protected PolicyComponent getPersistedPolicyComponent(PolicyComponent policyComponent) {
        return policyComponentRepository.findById(policyComponent.getId()).orElseThrow();
    }

    protected void assertPersistedPolicyComponentToMatchAllProperties(PolicyComponent expectedPolicyComponent) {
        assertPolicyComponentAllPropertiesEquals(expectedPolicyComponent, getPersistedPolicyComponent(expectedPolicyComponent));
    }

    protected void assertPersistedPolicyComponentToMatchUpdatableProperties(PolicyComponent expectedPolicyComponent) {
        assertPolicyComponentAllUpdatablePropertiesEquals(expectedPolicyComponent, getPersistedPolicyComponent(expectedPolicyComponent));
    }
}
