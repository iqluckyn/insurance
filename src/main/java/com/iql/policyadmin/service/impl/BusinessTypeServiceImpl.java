package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.BusinessType;
import com.iql.policyadmin.repository.BusinessTypeRepository;
import com.iql.policyadmin.service.BusinessTypeService;
import com.iql.policyadmin.service.dto.BusinessTypeDTO;
import com.iql.policyadmin.service.mapper.BusinessTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.BusinessType}.
 */
@Service
@Transactional
public class BusinessTypeServiceImpl implements BusinessTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessTypeServiceImpl.class);

    private final BusinessTypeRepository businessTypeRepository;

    private final BusinessTypeMapper businessTypeMapper;

    public BusinessTypeServiceImpl(BusinessTypeRepository businessTypeRepository, BusinessTypeMapper businessTypeMapper) {
        this.businessTypeRepository = businessTypeRepository;
        this.businessTypeMapper = businessTypeMapper;
    }

    @Override
    public BusinessTypeDTO save(BusinessTypeDTO businessTypeDTO) {
        LOG.debug("Request to save BusinessType : {}", businessTypeDTO);
        BusinessType businessType = businessTypeMapper.toEntity(businessTypeDTO);
        businessType = businessTypeRepository.save(businessType);
        return businessTypeMapper.toDto(businessType);
    }

    @Override
    public BusinessTypeDTO update(BusinessTypeDTO businessTypeDTO) {
        LOG.debug("Request to update BusinessType : {}", businessTypeDTO);
        BusinessType businessType = businessTypeMapper.toEntity(businessTypeDTO);
        businessType = businessTypeRepository.save(businessType);
        return businessTypeMapper.toDto(businessType);
    }

    @Override
    public Optional<BusinessTypeDTO> partialUpdate(BusinessTypeDTO businessTypeDTO) {
        LOG.debug("Request to partially update BusinessType : {}", businessTypeDTO);

        return businessTypeRepository
            .findById(businessTypeDTO.getId())
            .map(existingBusinessType -> {
                businessTypeMapper.partialUpdate(existingBusinessType, businessTypeDTO);

                return existingBusinessType;
            })
            .map(businessTypeRepository::save)
            .map(businessTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BusinessTypes");
        return businessTypeRepository.findAll(pageable).map(businessTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessTypeDTO> findOne(Long id) {
        LOG.debug("Request to get BusinessType : {}", id);
        return businessTypeRepository.findById(id).map(businessTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BusinessType : {}", id);
        businessTypeRepository.deleteById(id);
    }
}
