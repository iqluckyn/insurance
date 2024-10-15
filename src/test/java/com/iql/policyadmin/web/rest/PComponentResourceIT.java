package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.PComponentAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.PComponent;
import com.iql.policyadmin.repository.PComponentRepository;
import com.iql.policyadmin.service.dto.PComponentDTO;
import com.iql.policyadmin.service.mapper.PComponentMapper;
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
 * Integration tests for the {@link PComponentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PComponentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/p-components";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PComponentRepository pComponentRepository;

    @Autowired
    private PComponentMapper pComponentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPComponentMockMvc;

    private PComponent pComponent;

    private PComponent insertedPComponent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PComponent createEntity() {
        return new PComponent().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PComponent createUpdatedEntity() {
        return new PComponent().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        pComponent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPComponent != null) {
            pComponentRepository.delete(insertedPComponent);
            insertedPComponent = null;
        }
    }

    @Test
    @Transactional
    void createPComponent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);
        var returnedPComponentDTO = om.readValue(
            restPComponentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pComponentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PComponentDTO.class
        );

        // Validate the PComponent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPComponent = pComponentMapper.toEntity(returnedPComponentDTO);
        assertPComponentUpdatableFieldsEquals(returnedPComponent, getPersistedPComponent(returnedPComponent));

        insertedPComponent = returnedPComponent;
    }

    @Test
    @Transactional
    void createPComponentWithExistingId() throws Exception {
        // Create the PComponent with an existing ID
        pComponent.setId(1L);
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPComponentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pComponentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pComponent.setName(null);

        // Create the PComponent, which fails.
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        restPComponentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pComponentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPComponents() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        // Get all the pComponentList
        restPComponentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPComponent() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        // Get the pComponent
        restPComponentMockMvc
            .perform(get(ENTITY_API_URL_ID, pComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pComponent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPComponent() throws Exception {
        // Get the pComponent
        restPComponentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPComponent() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pComponent
        PComponent updatedPComponent = pComponentRepository.findById(pComponent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPComponent are not directly saved in db
        em.detach(updatedPComponent);
        updatedPComponent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        PComponentDTO pComponentDTO = pComponentMapper.toDto(updatedPComponent);

        restPComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pComponentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pComponentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPComponentToMatchAllProperties(updatedPComponent);
    }

    @Test
    @Transactional
    void putNonExistingPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pComponentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pComponentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePComponentWithPatch() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pComponent using partial update
        PComponent partialUpdatedPComponent = new PComponent();
        partialUpdatedPComponent.setId(pComponent.getId());

        partialUpdatedPComponent.description(UPDATED_DESCRIPTION);

        restPComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPComponent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPComponent))
            )
            .andExpect(status().isOk());

        // Validate the PComponent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPComponentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPComponent, pComponent),
            getPersistedPComponent(pComponent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePComponentWithPatch() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pComponent using partial update
        PComponent partialUpdatedPComponent = new PComponent();
        partialUpdatedPComponent.setId(pComponent.getId());

        partialUpdatedPComponent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPComponent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPComponent))
            )
            .andExpect(status().isOk());

        // Validate the PComponent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPComponentUpdatableFieldsEquals(partialUpdatedPComponent, getPersistedPComponent(partialUpdatedPComponent));
    }

    @Test
    @Transactional
    void patchNonExistingPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pComponentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pComponentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPComponent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pComponent.setId(longCount.incrementAndGet());

        // Create the PComponent
        PComponentDTO pComponentDTO = pComponentMapper.toDto(pComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPComponentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pComponentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PComponent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePComponent() throws Exception {
        // Initialize the database
        insertedPComponent = pComponentRepository.saveAndFlush(pComponent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pComponent
        restPComponentMockMvc
            .perform(delete(ENTITY_API_URL_ID, pComponent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pComponentRepository.count();
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

    protected PComponent getPersistedPComponent(PComponent pComponent) {
        return pComponentRepository.findById(pComponent.getId()).orElseThrow();
    }

    protected void assertPersistedPComponentToMatchAllProperties(PComponent expectedPComponent) {
        assertPComponentAllPropertiesEquals(expectedPComponent, getPersistedPComponent(expectedPComponent));
    }

    protected void assertPersistedPComponentToMatchUpdatableProperties(PComponent expectedPComponent) {
        assertPComponentAllUpdatablePropertiesEquals(expectedPComponent, getPersistedPComponent(expectedPComponent));
    }
}
