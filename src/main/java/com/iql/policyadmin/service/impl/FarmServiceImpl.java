package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.Farm;
import com.iql.policyadmin.repository.FarmRepository;
import com.iql.policyadmin.service.FarmService;
import com.iql.policyadmin.service.dto.FarmDTO;
import com.iql.policyadmin.service.mapper.FarmMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.Farm}.
 */
@Service
@Transactional
public class FarmServiceImpl implements FarmService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmServiceImpl.class);

    private final FarmRepository farmRepository;

    private final FarmMapper farmMapper;

    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
    }

    @Override
    public FarmDTO save(FarmDTO farmDTO) {
        LOG.debug("Request to save Farm : {}", farmDTO);
        Farm farm = farmMapper.toEntity(farmDTO);
        farm = farmRepository.save(farm);
        return farmMapper.toDto(farm);
    }

    @Override
    public FarmDTO update(FarmDTO farmDTO) {
        LOG.debug("Request to update Farm : {}", farmDTO);
        Farm farm = farmMapper.toEntity(farmDTO);
        farm = farmRepository.save(farm);
        return farmMapper.toDto(farm);
    }

    @Override
    public Optional<FarmDTO> partialUpdate(FarmDTO farmDTO) {
        LOG.debug("Request to partially update Farm : {}", farmDTO);

        return farmRepository
            .findById(farmDTO.getId())
            .map(existingFarm -> {
                farmMapper.partialUpdate(existingFarm, farmDTO);

                return existingFarm;
            })
            .map(farmRepository::save)
            .map(farmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Farms");
        return farmRepository.findAll(pageable).map(farmMapper::toDto);
    }

    public Page<FarmDTO> findAllWithEagerRelationships(Pageable pageable) {
        return farmRepository.findAllWithEagerRelationships(pageable).map(farmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FarmDTO> findOne(Long id) {
        LOG.debug("Request to get Farm : {}", id);
        return farmRepository.findOneWithEagerRelationships(id).map(farmMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Farm : {}", id);
        farmRepository.deleteById(id);
    }
}
