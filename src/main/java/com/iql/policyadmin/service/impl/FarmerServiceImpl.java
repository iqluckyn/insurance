package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.Farmer;
import com.iql.policyadmin.repository.FarmerRepository;
import com.iql.policyadmin.service.FarmerService;
import com.iql.policyadmin.service.dto.FarmerDTO;
import com.iql.policyadmin.service.mapper.FarmerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.Farmer}.
 */
@Service
@Transactional
public class FarmerServiceImpl implements FarmerService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerServiceImpl.class);

    private final FarmerRepository farmerRepository;

    private final FarmerMapper farmerMapper;

    public FarmerServiceImpl(FarmerRepository farmerRepository, FarmerMapper farmerMapper) {
        this.farmerRepository = farmerRepository;
        this.farmerMapper = farmerMapper;
    }

    @Override
    public FarmerDTO save(FarmerDTO farmerDTO) {
        LOG.debug("Request to save Farmer : {}", farmerDTO);
        Farmer farmer = farmerMapper.toEntity(farmerDTO);
        farmer = farmerRepository.save(farmer);
        return farmerMapper.toDto(farmer);
    }

    @Override
    public FarmerDTO update(FarmerDTO farmerDTO) {
        LOG.debug("Request to update Farmer : {}", farmerDTO);
        Farmer farmer = farmerMapper.toEntity(farmerDTO);
        farmer = farmerRepository.save(farmer);
        return farmerMapper.toDto(farmer);
    }

    @Override
    public Optional<FarmerDTO> partialUpdate(FarmerDTO farmerDTO) {
        LOG.debug("Request to partially update Farmer : {}", farmerDTO);

        return farmerRepository
            .findById(farmerDTO.getId())
            .map(existingFarmer -> {
                farmerMapper.partialUpdate(existingFarmer, farmerDTO);

                return existingFarmer;
            })
            .map(farmerRepository::save)
            .map(farmerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Farmers");
        return farmerRepository.findAll(pageable).map(farmerMapper::toDto);
    }

    public Page<FarmerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return farmerRepository.findAllWithEagerRelationships(pageable).map(farmerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FarmerDTO> findOne(Long id) {
        LOG.debug("Request to get Farmer : {}", id);
        return farmerRepository.findOneWithEagerRelationships(id).map(farmerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Farmer : {}", id);
        farmerRepository.deleteById(id);
    }
}
