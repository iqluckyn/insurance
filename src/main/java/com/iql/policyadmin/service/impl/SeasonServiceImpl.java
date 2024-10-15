package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.Season;
import com.iql.policyadmin.repository.SeasonRepository;
import com.iql.policyadmin.service.SeasonService;
import com.iql.policyadmin.service.dto.SeasonDTO;
import com.iql.policyadmin.service.mapper.SeasonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.Season}.
 */
@Service
@Transactional
public class SeasonServiceImpl implements SeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(SeasonServiceImpl.class);

    private final SeasonRepository seasonRepository;

    private final SeasonMapper seasonMapper;

    public SeasonServiceImpl(SeasonRepository seasonRepository, SeasonMapper seasonMapper) {
        this.seasonRepository = seasonRepository;
        this.seasonMapper = seasonMapper;
    }

    @Override
    public SeasonDTO save(SeasonDTO seasonDTO) {
        LOG.debug("Request to save Season : {}", seasonDTO);
        Season season = seasonMapper.toEntity(seasonDTO);
        season = seasonRepository.save(season);
        return seasonMapper.toDto(season);
    }

    @Override
    public SeasonDTO update(SeasonDTO seasonDTO) {
        LOG.debug("Request to update Season : {}", seasonDTO);
        Season season = seasonMapper.toEntity(seasonDTO);
        season = seasonRepository.save(season);
        return seasonMapper.toDto(season);
    }

    @Override
    public Optional<SeasonDTO> partialUpdate(SeasonDTO seasonDTO) {
        LOG.debug("Request to partially update Season : {}", seasonDTO);

        return seasonRepository
            .findById(seasonDTO.getId())
            .map(existingSeason -> {
                seasonMapper.partialUpdate(existingSeason, seasonDTO);

                return existingSeason;
            })
            .map(seasonRepository::save)
            .map(seasonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeasonDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Seasons");
        return seasonRepository.findAll(pageable).map(seasonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeasonDTO> findOne(Long id) {
        LOG.debug("Request to get Season : {}", id);
        return seasonRepository.findById(id).map(seasonMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Season : {}", id);
        seasonRepository.deleteById(id);
    }
}
