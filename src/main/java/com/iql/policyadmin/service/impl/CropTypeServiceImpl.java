package com.iql.policyadmin.service.impl;

import com.iql.policyadmin.domain.CropType;
import com.iql.policyadmin.repository.CropTypeRepository;
import com.iql.policyadmin.service.CropTypeService;
import com.iql.policyadmin.service.dto.CropTypeDTO;
import com.iql.policyadmin.service.mapper.CropTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.iql.policyadmin.domain.CropType}.
 */
@Service
@Transactional
public class CropTypeServiceImpl implements CropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CropTypeServiceImpl.class);

    private final CropTypeRepository cropTypeRepository;

    private final CropTypeMapper cropTypeMapper;

    public CropTypeServiceImpl(CropTypeRepository cropTypeRepository, CropTypeMapper cropTypeMapper) {
        this.cropTypeRepository = cropTypeRepository;
        this.cropTypeMapper = cropTypeMapper;
    }

    @Override
    public CropTypeDTO save(CropTypeDTO cropTypeDTO) {
        LOG.debug("Request to save CropType : {}", cropTypeDTO);
        CropType cropType = cropTypeMapper.toEntity(cropTypeDTO);
        cropType = cropTypeRepository.save(cropType);
        return cropTypeMapper.toDto(cropType);
    }

    @Override
    public CropTypeDTO update(CropTypeDTO cropTypeDTO) {
        LOG.debug("Request to update CropType : {}", cropTypeDTO);
        CropType cropType = cropTypeMapper.toEntity(cropTypeDTO);
        cropType = cropTypeRepository.save(cropType);
        return cropTypeMapper.toDto(cropType);
    }

    @Override
    public Optional<CropTypeDTO> partialUpdate(CropTypeDTO cropTypeDTO) {
        LOG.debug("Request to partially update CropType : {}", cropTypeDTO);

        return cropTypeRepository
            .findById(cropTypeDTO.getId())
            .map(existingCropType -> {
                cropTypeMapper.partialUpdate(existingCropType, cropTypeDTO);

                return existingCropType;
            })
            .map(cropTypeRepository::save)
            .map(cropTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CropTypes");
        return cropTypeRepository.findAll(pageable).map(cropTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CropTypeDTO> findOne(Long id) {
        LOG.debug("Request to get CropType : {}", id);
        return cropTypeRepository.findById(id).map(cropTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CropType : {}", id);
        cropTypeRepository.deleteById(id);
    }
}
