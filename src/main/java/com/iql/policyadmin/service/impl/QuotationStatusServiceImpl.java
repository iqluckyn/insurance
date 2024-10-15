package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.QuotationStatus;
import com.iql.policyadmin.repository.QuotationStatusRepository;
import com.iql.policyadmin.service.QuotationStatusService;
import com.iql.policyadmin.service.dto.QuotationStatusDTO;
import com.iql.policyadmin.service.mapper.QuotationStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.QuotationStatus}.
 */
@Service
@Transactional
public class QuotationStatusServiceImpl implements QuotationStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationStatusServiceImpl.class);

    private final QuotationStatusRepository quotationStatusRepository;

    private final QuotationStatusMapper quotationStatusMapper;

    public QuotationStatusServiceImpl(QuotationStatusRepository quotationStatusRepository, QuotationStatusMapper quotationStatusMapper) {
        this.quotationStatusRepository = quotationStatusRepository;
        this.quotationStatusMapper = quotationStatusMapper;
    }

    @Override
    public QuotationStatusDTO save(QuotationStatusDTO quotationStatusDTO) {
        LOG.debug("Request to save QuotationStatus : {}", quotationStatusDTO);
        QuotationStatus quotationStatus = quotationStatusMapper.toEntity(quotationStatusDTO);
        quotationStatus = quotationStatusRepository.save(quotationStatus);
        return quotationStatusMapper.toDto(quotationStatus);
    }

    @Override
    public QuotationStatusDTO update(QuotationStatusDTO quotationStatusDTO) {
        LOG.debug("Request to update QuotationStatus : {}", quotationStatusDTO);
        QuotationStatus quotationStatus = quotationStatusMapper.toEntity(quotationStatusDTO);
        quotationStatus = quotationStatusRepository.save(quotationStatus);
        return quotationStatusMapper.toDto(quotationStatus);
    }

    @Override
    public Optional<QuotationStatusDTO> partialUpdate(QuotationStatusDTO quotationStatusDTO) {
        LOG.debug("Request to partially update QuotationStatus : {}", quotationStatusDTO);

        return quotationStatusRepository
            .findById(quotationStatusDTO.getId())
            .map(existingQuotationStatus -> {
                quotationStatusMapper.partialUpdate(existingQuotationStatus, quotationStatusDTO);

                return existingQuotationStatus;
            })
            .map(quotationStatusRepository::save)
            .map(quotationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuotationStatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all QuotationStatuses");
        return quotationStatusRepository.findAll(pageable).map(quotationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuotationStatusDTO> findOne(Long id) {
        LOG.debug("Request to get QuotationStatus : {}", id);
        return quotationStatusRepository.findById(id).map(quotationStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuotationStatus : {}", id);
        quotationStatusRepository.deleteById(id);
    }
}
