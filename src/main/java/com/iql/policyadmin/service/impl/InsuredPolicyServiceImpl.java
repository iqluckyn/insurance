package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.InsuredPolicy;
import com.iql.policyadmin.repository.InsuredPolicyRepository;
import com.iql.policyadmin.service.InsuredPolicyService;
import com.iql.policyadmin.service.dto.InsuredPolicyDTO;
import com.iql.policyadmin.service.mapper.InsuredPolicyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.InsuredPolicy}.
 */
@Service
@Transactional
public class InsuredPolicyServiceImpl implements InsuredPolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuredPolicyServiceImpl.class);

    private final InsuredPolicyRepository insuredPolicyRepository;

    private final InsuredPolicyMapper insuredPolicyMapper;

    public InsuredPolicyServiceImpl(InsuredPolicyRepository insuredPolicyRepository, InsuredPolicyMapper insuredPolicyMapper) {
        this.insuredPolicyRepository = insuredPolicyRepository;
        this.insuredPolicyMapper = insuredPolicyMapper;
    }

    @Override
    public InsuredPolicyDTO save(InsuredPolicyDTO insuredPolicyDTO) {
        LOG.debug("Request to save InsuredPolicy : {}", insuredPolicyDTO);
        InsuredPolicy insuredPolicy = insuredPolicyMapper.toEntity(insuredPolicyDTO);
        insuredPolicy = insuredPolicyRepository.save(insuredPolicy);
        return insuredPolicyMapper.toDto(insuredPolicy);
    }

    @Override
    public InsuredPolicyDTO update(InsuredPolicyDTO insuredPolicyDTO) {
        LOG.debug("Request to update InsuredPolicy : {}", insuredPolicyDTO);
        InsuredPolicy insuredPolicy = insuredPolicyMapper.toEntity(insuredPolicyDTO);
        insuredPolicy = insuredPolicyRepository.save(insuredPolicy);
        return insuredPolicyMapper.toDto(insuredPolicy);
    }

    @Override
    public Optional<InsuredPolicyDTO> partialUpdate(InsuredPolicyDTO insuredPolicyDTO) {
        LOG.debug("Request to partially update InsuredPolicy : {}", insuredPolicyDTO);

        return insuredPolicyRepository
            .findById(insuredPolicyDTO.getId())
            .map(existingInsuredPolicy -> {
                insuredPolicyMapper.partialUpdate(existingInsuredPolicy, insuredPolicyDTO);

                return existingInsuredPolicy;
            })
            .map(insuredPolicyRepository::save)
            .map(insuredPolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsuredPolicyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all InsuredPolicies");
        return insuredPolicyRepository.findAll(pageable).map(insuredPolicyMapper::toDto);
    }

    public Page<InsuredPolicyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return insuredPolicyRepository.findAllWithEagerRelationships(pageable).map(insuredPolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuredPolicyDTO> findOne(Long id) {
        LOG.debug("Request to get InsuredPolicy : {}", id);
        return insuredPolicyRepository.findOneWithEagerRelationships(id).map(insuredPolicyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InsuredPolicy : {}", id);
        insuredPolicyRepository.deleteById(id);
    }
}
