package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.Quotation;
import com.iql.policyadmin.repository.QuotationRepository;
import com.iql.policyadmin.service.QuotationService;
import com.iql.policyadmin.service.dto.QuotationDTO;
import com.iql.policyadmin.service.mapper.QuotationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.Quotation}.
 */
@Service
@Transactional
public class QuotationServiceImpl implements QuotationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationServiceImpl.class);

    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationServiceImpl(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    @Override
    public QuotationDTO save(QuotationDTO quotationDTO) {
        LOG.debug("Request to save Quotation : {}", quotationDTO);
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation = quotationRepository.save(quotation);
        return quotationMapper.toDto(quotation);
    }

    @Override
    public QuotationDTO update(QuotationDTO quotationDTO) {
        LOG.debug("Request to update Quotation : {}", quotationDTO);
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation = quotationRepository.save(quotation);
        return quotationMapper.toDto(quotation);
    }

    @Override
    public Optional<QuotationDTO> partialUpdate(QuotationDTO quotationDTO) {
        LOG.debug("Request to partially update Quotation : {}", quotationDTO);

        return quotationRepository
            .findById(quotationDTO.getId())
            .map(existingQuotation -> {
                quotationMapper.partialUpdate(existingQuotation, quotationDTO);

                return existingQuotation;
            })
            .map(quotationRepository::save)
            .map(quotationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuotationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Quotations");
        return quotationRepository.findAll(pageable).map(quotationMapper::toDto);
    }

    public Page<QuotationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return quotationRepository.findAllWithEagerRelationships(pageable).map(quotationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuotationDTO> findOne(Long id) {
        LOG.debug("Request to get Quotation : {}", id);
        return quotationRepository.findOneWithEagerRelationships(id).map(quotationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Quotation : {}", id);
        quotationRepository.deleteById(id);
    }
}
