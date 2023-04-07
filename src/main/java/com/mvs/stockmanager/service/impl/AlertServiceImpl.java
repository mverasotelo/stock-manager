package com.mvs.stockmanager.service.impl;

import com.mvs.stockmanager.domain.Alert;
import com.mvs.stockmanager.repository.AlertRepository;
import com.mvs.stockmanager.service.AlertService;
import com.mvs.stockmanager.service.dto.AlertDTO;
import com.mvs.stockmanager.service.mapper.AlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Alert}.
 */
@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertServiceImpl(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    @Override
    public AlertDTO save(AlertDTO alertDTO) {
        log.debug("Request to save Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    @Override
    public AlertDTO update(AlertDTO alertDTO) {
        log.debug("Request to update Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    @Override
    public Optional<AlertDTO> partialUpdate(AlertDTO alertDTO) {
        log.debug("Request to partially update Alert : {}", alertDTO);

        return alertRepository
            .findById(alertDTO.getId())
            .map(existingAlert -> {
                alertMapper.partialUpdate(existingAlert, alertDTO);

                return existingAlert;
            })
            .map(alertRepository::save)
            .map(alertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alerts");
        return alertRepository.findAll(pageable).map(alertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertDTO> findAllActive(Pageable pageable) {
        log.debug("Request to get all Alerts");
        return alertRepository.findAllByRectificationDatetimeNull(pageable).map(alertMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AlertDTO> findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        return alertRepository.findById(id).map(alertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        alertRepository.deleteById(id);
    }
}
