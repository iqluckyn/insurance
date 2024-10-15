package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.Business;
import com.iql.policyadmin.repository.BusinessRepository;
import com.iql.policyadmin.service.BusinessService;
import com.iql.policyadmin.service.dto.BusinessDTO;
import com.iql.policyadmin.service.mapper.BusinessMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.Business}.
 */
@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessServiceImpl.class);

    private final BusinessRepository businessRepository;

    private final BusinessMapper businessMapper;

    public BusinessServiceImpl(BusinessRepository businessRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }

    @Override
    public BusinessDTO save(BusinessDTO businessDTO) {
        LOG.debug("Request to save Business : {}", businessDTO);
        Business business = businessMapper.toEntity(businessDTO);
        business = businessRepository.save(business);
        return businessMapper.toDto(business);
    }

    @Override
    public BusinessDTO update(BusinessDTO businessDTO) {
        LOG.debug("Request to update Business : {}", businessDTO);
        Business business = businessMapper.toEntity(businessDTO);
        business = businessRepository.save(business);
        return businessMapper.toDto(business);
    }

    @Override
    public Optional<BusinessDTO> partialUpdate(BusinessDTO businessDTO) {
        LOG.debug("Request to partially update Business : {}", businessDTO);

        return businessRepository
            .findById(businessDTO.getId())
            .map(existingBusiness -> {
                businessMapper.partialUpdate(existingBusiness, businessDTO);

                return existingBusiness;
            })
            .map(businessRepository::save)
            .map(businessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BusinessDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Businesses");
        return businessRepository.findAll(pageable).map(businessMapper::toDto);
    }

    public Page<BusinessDTO> findAllWithEagerRelationships(Pageable pageable) {
        return businessRepository.findAllWithEagerRelationships(pageable).map(businessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessDTO> findOne(Long id) {
        LOG.debug("Request to get Business : {}", id);
        return businessRepository.findOneWithEagerRelationships(id).map(businessMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Business : {}", id);
        businessRepository.deleteById(id);
    }
}
