package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.dao.StrategyRepository;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.mapper.StrategyMapper;
import org.example.journex.model.Pagination;
import org.example.journex.model.StrategyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StrategyService {

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private AuthService authService;

    @Autowired
    private StrategyMapper strategyMapper;

    @Transactional
    public Long save(StrategyDto dto) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyMapper.toEntity(dto);
        strategy.setAddress(UUID.randomUUID().toString());
        strategy.setUser(user);
        strategy.setCreatedAt(LocalDateTime.now());
        strategy.setDeleted(false);
        if (dto.getChecklistIds() != null && !dto.getChecklistIds().isEmpty())
            strategy.setChecklists(checklistService.resolveChecklists(dto.getChecklistIds(), user));
        Strategy saved = strategyRepository.save(strategy);
        return saved.getId();
    }

    @Transactional
    public Long update(Long strategyId, StrategyDto dto) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyRepository.findById(strategyId, user.getId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        strategyMapper.updateEntityFromDto(dto, strategy);
        strategy.setUpdatedAt(LocalDateTime.now());
        if (dto.getChecklistIds() != null)
            strategy.setChecklists(checklistService.resolveChecklists(dto.getChecklistIds(), user));
        Strategy saved = strategyRepository.save(strategy);
        return saved.getId();
    }

    @Transactional
    public void softDelete(Long strategyId) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyRepository.findById(strategyId, user.getId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        strategy.setDeleted(true);
        strategy.setDeletedAt(LocalDateTime.now());
        strategyRepository.save(strategy);
    }

    @Transactional
    public void restoreDeleted(Long strategyId) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyRepository.findByIdAndUserIdAndDeletedTrue(strategyId, user.getId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        strategy.setDeleted(false);
        strategy.setDeletedAt(null);
        strategyRepository.save(strategy);
    }

    @Transactional(readOnly = true)
    public StrategyDto findById(Long strategyId) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyRepository.findById(strategyId, user.getId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        return strategyMapper.toDto(strategy);
    }

    @Transactional(readOnly = true)
    public Page<StrategyDto> findAll(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Strategy> page = strategyRepository.findAll(user.getId(), pagination.toPageable());
        return page.map(strategyMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<StrategyDto> findAllDeleted(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Strategy> page = strategyRepository.findAllDeleted(user.getId(), pagination.toPageable());
        return page.map(strategyMapper::toDto);
    }

    @Transactional(readOnly = true)
    public StrategyDto findPublicByAddress(String address) {
        Strategy strategy = strategyRepository
                .findPublicByAddress(address)
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        return strategyMapper.toPublicDto(strategy);
    }

}