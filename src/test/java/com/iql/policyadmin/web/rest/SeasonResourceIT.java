package com.iql.policyadmin.web.rest;

import static com.iql.policyadmin.domain.SeasonAsserts.*;
import static com.iql.policyadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iql.policyadmin.IntegrationTest;
import com.iql.policyadmin.domain.Season;
import com.iql.policyadmin.repository.SeasonRepository;
import com.iql.policyadmin.service.dto.SeasonDTO;
import com.iql.policyadmin.service.mapper.SeasonMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeasonResourceIT {

    private static final String DEFAULT_SEASON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SEASON_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeasonMapper seasonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeasonMockMvc;

    private Season season;

    private Season insertedSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createEntity() {
        return new Season()
            .seasonName(DEFAULT_SEASON_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createUpdatedEntity() {
        return new Season()
            .seasonName(UPDATED_SEASON_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        season = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSeason != null) {
            seasonRepository.delete(insertedSeason);
            insertedSeason = null;
        }
    }

    @Test
    @Transactional
    void createSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);
        var returnedSeasonDTO = om.readValue(
            restSeasonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SeasonDTO.class
        );

        // Validate the Season in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSeason = seasonMapper.toEntity(returnedSeasonDTO);
        assertSeasonUpdatableFieldsEquals(returnedSeason, getPersistedSeason(returnedSeason));

        insertedSeason = returnedSeason;
    }

    @Test
    @Transactional
    void createSeasonWithExistingId() throws Exception {
        // Create the Season with an existing ID
        season.setId(1L);
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSeasonNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        season.setSeasonName(null);

        // Create the Season, which fails.
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        season.setStartDate(null);

        // Create the Season, which fails.
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        season.setEndDate(null);

        // Create the Season, which fails.
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeasons() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
            .andExpect(jsonPath("$.[*].seasonName").value(hasItem(DEFAULT_SEASON_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.seasonName").value(DEFAULT_SEASON_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season
        Season updatedSeason = seasonRepository.findById(season.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeason are not directly saved in db
        em.detach(updatedSeason);
        updatedSeason.seasonName(UPDATED_SEASON_NAME).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).isActive(UPDATED_IS_ACTIVE);
        SeasonDTO seasonDTO = seasonMapper.toDto(updatedSeason);

        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seasonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSeasonToMatchAllProperties(updatedSeason);
    }

    @Test
    @Transactional
    void putNonExistingSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seasonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason.isActive(UPDATED_IS_ACTIVE);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeasonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSeason, season), getPersistedSeason(season));
    }

    @Test
    @Transactional
    void fullUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason
            .seasonName(UPDATED_SEASON_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeasonUpdatableFieldsEquals(partialUpdatedSeason, getPersistedSeason(partialUpdatedSeason));
    }

    @Test
    @Transactional
    void patchNonExistingSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seasonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seasonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // Create the Season
        SeasonDTO seasonDTO = seasonMapper.toDto(season);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(seasonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the season
        restSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, season.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return seasonRepository.count();
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

    protected Season getPersistedSeason(Season season) {
        return seasonRepository.findById(season.getId()).orElseThrow();
    }

    protected void assertPersistedSeasonToMatchAllProperties(Season expectedSeason) {
        assertSeasonAllPropertiesEquals(expectedSeason, getPersistedSeason(expectedSeason));
    }

    protected void assertPersistedSeasonToMatchUpdatableProperties(Season expectedSeason) {
        assertSeasonAllUpdatablePropertiesEquals(expectedSeason, getPersistedSeason(expectedSeason));
    }
}
