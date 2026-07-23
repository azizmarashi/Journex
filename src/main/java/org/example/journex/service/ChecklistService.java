package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.dao.StrategyRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.Strategy;
import org.example.journex.model.ChecklistDto;
import org.example.journex.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private StrategyRepository strategyRepository;

    public Long save (ChecklistDto dto){

        Strategy strategy = strategyRepository.findById(dto.getStrategyId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));

        Checklist checklist = new Checklist();
        checklist.setName(dto.getName());
        checklist.setDescription(dto.getDescription());
        checklist.setScope(dto.getScope());
        checklist.setStrategy(strategy);
        checklist.setActive(dto.getActive());
        checklist.setCreatedAt(LocalDateTime.now());
        checklist.setUpdatedAt(null);

        checklistRepository.save(checklist);

        return checklist.getId();
    }

    @Transactional
    public Long update(Long id, ChecklistDto dto){

        Checklist checklist =checklistRepository.findById(id)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        checklist.setName(dto.getName());
        checklist.setDescription(dto.getDescription());
        checklist.setScope(dto.getScope());
        checklist.setActive(dto.getActive());
        checklist.setUpdatedAt(LocalDateTime.now());

        checklistRepository.save(checklist);
        return checklist.getId();
    }

    @Transactional
    public void delete(Long ChecklistId){
        Checklist checklist =checklistRepository
                .findById(ChecklistId).orElseThrow(() -> new JournexException("error.checklist.notFound"));

        checklist.setDeleted(true);
        checklist.setDeletedAt(LocalDateTime.now());
        checklistRepository.save(checklist);
    }

    @Transactional(readOnly = true)
    public ChecklistDto findById(Long id) {

        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        ChecklistDto dto = new ChecklistDto();
        dto.setName(checklist.getName());
        dto.setDescription(checklist.getDescription());
        dto.setScope(checklist.getScope());
        dto.setStrategyId(checklist.getStrategy().getId());
        dto.setActive(checklist.getActive());
        dto.setCreatedAt(checklist.getCreatedAt());
        dto.setUpdatedAt(checklist.getUpdatedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ChecklistDto> findAllByStrategy(Long strategyId, Pagination pagination) {

        Page<Checklist> checklists =
                checklistRepository.findAllByStrategyId(strategyId, pagination.toPageable());

        return checklists.stream()
                .map(checklist -> {

                    ChecklistDto dto = new ChecklistDto();

                    dto.setName(checklist.getName());
                    dto.setDescription(checklist.getDescription());
                    dto.setScope(checklist.getScope());
                    dto.setStrategyId(checklist.getStrategy().getId());
                    dto.setActive(checklist.getActive());
                    dto.setCreatedAt(checklist.getCreatedAt());
                    dto.setUpdatedAt(checklist.getUpdatedAt());
                    return dto;

                }).toList();

    }

    //todo find all by userId

}