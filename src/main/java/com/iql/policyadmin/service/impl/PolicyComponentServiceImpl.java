package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.PolicyComponent;
import com.iql.policyadmin.repository.PolicyComponentRepository;
import com.iql.policyadmin.service.PolicyComponentService;
import com.iql.policyadmin.service.dto.PolicyComponentDTO;
import com.iql.policyadmin.service.mapper.PolicyComponentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.PolicyComponent}.
 */
@Service
@Transactional
public class PolicyComponentServiceImpl implements PolicyComponentService {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyComponentServiceImpl.class);

    private final PolicyComponentRepository policyComponentRepository;

    private final PolicyComponentMapper policyComponentMapper;

    public PolicyComponentServiceImpl(PolicyComponentRepository policyComponentRepository, PolicyComponentMapper policyComponentMapper) {
        this.policyComponentRepository = policyComponentRepository;
        this.policyComponentMapper = policyComponentMapper;
    }

    @Override
    public PolicyComponentDTO save(PolicyComponentDTO policyComponentDTO) {
        LOG.debug("Request to save PolicyComponent : {}", policyComponentDTO);
        PolicyComponent policyComponent = policyComponentMapper.toEntity(policyComponentDTO);
        policyComponent = policyComponentRepository.save(policyComponent);
        return policyComponentMapper.toDto(policyComponent);
    }

    @Override
    public PolicyComponentDTO update(PolicyComponentDTO policyComponentDTO) {
        LOG.debug("Request to update PolicyComponent : {}", policyComponentDTO);
        PolicyComponent policyComponent = policyComponentMapper.toEntity(policyComponentDTO);
        policyComponent = policyComponentRepository.save(policyComponent);
        return policyComponentMapper.toDto(policyComponent);
    }

    @Override
    public Optional<PolicyComponentDTO> partialUpdate(PolicyComponentDTO policyComponentDTO) {
        LOG.debug("Request to partially update PolicyComponent : {}", policyComponentDTO);

        return policyComponentRepository
            .findById(policyComponentDTO.getId())
            .map(existingPolicyComponent -> {
                policyComponentMapper.partialUpdate(existingPolicyComponent, policyComponentDTO);

                return existingPolicyComponent;
            })
            .map(policyComponentRepository::save)
            .map(policyComponentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyComponentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PolicyComponents");
        return policyComponentRepository.findAll(pageable).map(policyComponentMapper::toDto);
    }

    public Page<PolicyComponentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return policyComponentRepository.findAllWithEagerRelationships(pageable).map(policyComponentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PolicyComponentDTO> findOne(Long id) {
        LOG.debug("Request to get PolicyComponent : {}", id);
        return policyComponentRepository.findOneWithEagerRelationships(id).map(policyComponentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PolicyComponent : {}", id);
        policyComponentRepository.deleteById(id);
    }
}
