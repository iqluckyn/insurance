package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.PolicyClaim;
import com.iql.policyadmin.repository.PolicyClaimRepository;
import com.iql.policyadmin.service.PolicyClaimService;
import com.iql.policyadmin.service.dto.PolicyClaimDTO;
import com.iql.policyadmin.service.mapper.PolicyClaimMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.PolicyClaim}.
 */
@Service
@Transactional
public class PolicyClaimServiceImpl implements PolicyClaimService {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyClaimServiceImpl.class);

    private final PolicyClaimRepository policyClaimRepository;

    private final PolicyClaimMapper policyClaimMapper;

    public PolicyClaimServiceImpl(PolicyClaimRepository policyClaimRepository, PolicyClaimMapper policyClaimMapper) {
        this.policyClaimRepository = policyClaimRepository;
        this.policyClaimMapper = policyClaimMapper;
    }

    @Override
    public PolicyClaimDTO save(PolicyClaimDTO policyClaimDTO) {
        LOG.debug("Request to save PolicyClaim : {}", policyClaimDTO);
        PolicyClaim policyClaim = policyClaimMapper.toEntity(policyClaimDTO);
        policyClaim = policyClaimRepository.save(policyClaim);
        return policyClaimMapper.toDto(policyClaim);
    }

    @Override
    public PolicyClaimDTO update(PolicyClaimDTO policyClaimDTO) {
        LOG.debug("Request to update PolicyClaim : {}", policyClaimDTO);
        PolicyClaim policyClaim = policyClaimMapper.toEntity(policyClaimDTO);
        policyClaim = policyClaimRepository.save(policyClaim);
        return policyClaimMapper.toDto(policyClaim);
    }

    @Override
    public Optional<PolicyClaimDTO> partialUpdate(PolicyClaimDTO policyClaimDTO) {
        LOG.debug("Request to partially update PolicyClaim : {}", policyClaimDTO);

        return policyClaimRepository
            .findById(policyClaimDTO.getId())
            .map(existingPolicyClaim -> {
                policyClaimMapper.partialUpdate(existingPolicyClaim, policyClaimDTO);

                return existingPolicyClaim;
            })
            .map(policyClaimRepository::save)
            .map(policyClaimMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyClaimDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PolicyClaims");
        return policyClaimRepository.findAll(pageable).map(policyClaimMapper::toDto);
    }

    public Page<PolicyClaimDTO> findAllWithEagerRelationships(Pageable pageable) {
        return policyClaimRepository.findAllWithEagerRelationships(pageable).map(policyClaimMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PolicyClaimDTO> findOne(Long id) {
        LOG.debug("Request to get PolicyClaim : {}", id);
        return policyClaimRepository.findOneWithEagerRelationships(id).map(policyClaimMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PolicyClaim : {}", id);
        policyClaimRepository.deleteById(id);
    }
}
