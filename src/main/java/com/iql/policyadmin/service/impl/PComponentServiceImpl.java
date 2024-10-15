package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.PComponent;
import com.iql.policyadmin.repository.PComponentRepository;
import com.iql.policyadmin.service.PComponentService;
import com.iql.policyadmin.service.dto.PComponentDTO;
import com.iql.policyadmin.service.mapper.PComponentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.PComponent}.
 */
@Service
@Transactional
public class PComponentServiceImpl implements PComponentService {

    private static final Logger LOG = LoggerFactory.getLogger(PComponentServiceImpl.class);

    private final PComponentRepository pComponentRepository;

    private final PComponentMapper pComponentMapper;

    public PComponentServiceImpl(PComponentRepository pComponentRepository, PComponentMapper pComponentMapper) {
        this.pComponentRepository = pComponentRepository;
        this.pComponentMapper = pComponentMapper;
    }

    @Override
    public PComponentDTO save(PComponentDTO pComponentDTO) {
        LOG.debug("Request to save PComponent : {}", pComponentDTO);
        PComponent pComponent = pComponentMapper.toEntity(pComponentDTO);
        pComponent = pComponentRepository.save(pComponent);
        return pComponentMapper.toDto(pComponent);
    }

    @Override
    public PComponentDTO update(PComponentDTO pComponentDTO) {
        LOG.debug("Request to update PComponent : {}", pComponentDTO);
        PComponent pComponent = pComponentMapper.toEntity(pComponentDTO);
        pComponent = pComponentRepository.save(pComponent);
        return pComponentMapper.toDto(pComponent);
    }

    @Override
    public Optional<PComponentDTO> partialUpdate(PComponentDTO pComponentDTO) {
        LOG.debug("Request to partially update PComponent : {}", pComponentDTO);

        return pComponentRepository
            .findById(pComponentDTO.getId())
            .map(existingPComponent -> {
                pComponentMapper.partialUpdate(existingPComponent, pComponentDTO);

                return existingPComponent;
            })
            .map(pComponentRepository::save)
            .map(pComponentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PComponentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PComponents");
        return pComponentRepository.findAll(pageable).map(pComponentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PComponentDTO> findOne(Long id) {
        LOG.debug("Request to get PComponent : {}", id);
        return pComponentRepository.findById(id).map(pComponentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PComponent : {}", id);
        pComponentRepository.deleteById(id);
    }
}
