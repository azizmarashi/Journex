package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.dao.StrategyRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.mapper.ChecklistMapper;
import org.example.journex.mapper.StrategyMapper;
import org.example.journex.model.ChecklistDto;
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
    private AuthService authService;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private ChecklistMapper checklistMapper;

    public Long save(StrategyDto dto) {

        User user = authService.getCurrentUser();
        Strategy strategy = strategyMapper.toEntity(dto);

        strategy.setAddress(UUID.randomUUID().toString());
        strategy.setCreatedAt(LocalDateTime.now());
        strategy.setUser(user);
        strategy.setPublicStrategy(false);
        strategy.setChecklists(checklistRepository.findAllById(dto.getChecklistIds()));

        List<Checklist> checklists = checklistRepository.findAllById(dto.getChecklistIds());

        if (dto.getChecklistIds() != null && checklists.size() != dto.getChecklistIds().size()) {
            throw new JournexException("error.checklist.not_found");
        }

        for (Checklist checklist : checklists) {
            if (!checklist.getUser().getId().equals(user.getId())) {
                throw new JournexException("error.access.denied");
            }
        }

        Strategy saved = strategyRepository.save(strategy);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public void delete(Long strategyId){
        User user = authService.getCurrentUser();
        Strategy strategy =strategyRepository.findByIdAndUserId(strategyId, user.getId())
                        .orElseThrow(() -> new JournexException("error.strategy.notFound"));

        strategyRepository.delete(strategy);
    }

    @Transactional(readOnly = true)
    public Long update(Long strategyId, StrategyDto dto) {

        User user = authService.getCurrentUser();

        Strategy strategy = strategyRepository
                .findByIdAndUserId(strategyId, user.getId())
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));

//        strategy.setName(dto.getName());
//        strategy.setDescription(dto.getDescription());
//        strategy.setTradeType(dto.getTradeType());
//        strategy.setTradeMarketType(dto.getTradeMarketType());
//        strategy.setTradeTimeframe(dto.getTradeTimeframe());
//        strategy.setRisk(dto.getRisk());
//        strategy.setReward(dto.getReward());
//        strategy.setRiskPercent(dto.getRiskPercent());
//        strategy.setActive(dto.getActive());
//        strategy.setPublicStrategy(dto.getPublicStrategy());
//        strategy.setUpdatedAt(LocalDateTime.now());

//        if(dto.getChecklists() != null){
//
//            strategy.getChecklists().clear();
//            dto.getChecklists().forEach(checklistDto -> {
//
//                        Checklist checklist = new Checklist();
//                        checklist.setName(checklistDto.getName());
//                        checklist.setDescription(checklistDto.getDescription());
//                        checklist.setScope(checklistDto.getScope());
//                        checklist.setActive(checklistDto.getActive());
//                        checklist.setStrategy(strategy);
//                        checklist.setCreatedAt(LocalDateTime.now());
//                        strategy.getChecklists().add(checklist);
//                    });
//        }

        Strategy updatedStrategy = strategyRepository.save(strategy);
        return updatedStrategy.getId();
    }

    public Page<StrategyDto> findAll(Pagination pagination){
        Page<Strategy> strategies =strategyRepository.findAllByUserId(
                authService.getCurrentUser().getId()
                , pagination.toPageable());
        return strategies.map(this::convertToDto);
    }

    public StrategyDto findById(Long strategyId) {
        User user = authService.getCurrentUser();
        Strategy strategy = strategyRepository .findByIdAndUserId( strategyId, user.getId() )
                .orElseThrow(() -> new JournexException("error.strategy.notFound") );
        return convertToDto(strategy);
    }

    public StrategyDto findByAddress(String address){
        User user = authService.getCurrentUser();
        Strategy strategy =strategyRepository.findByUserIdAndAddress(user.getId(),address)
                .orElseThrow(() -> new JournexException("error.strategy.notFound"));
        return convertToDto(strategy);
    }

    public StrategyDto findPublicByAddress(String address) {
        Strategy strategy = strategyRepository.findByPublicStrategyAndAddress(address)
                .orElseThrow(() ->new JournexException("error.strategy.notFound"));
        return convertToDto(strategy);
    }

    private StrategyDto convertToDto(Strategy strategy){

        StrategyDto dto = new StrategyDto();

        dto.setId(strategy.getId());
        dto.setAddress(strategy.getAddress());
        dto.setName(strategy.getName());
        dto.setDescription(strategy.getDescription());
        dto.setTradeType(strategy.getTradeType());
        dto.setTradeMarketType(strategy.getTradeMarketType());
        dto.setTradeTimeframe(strategy.getTradeTimeframe());
        dto.setRisk(strategy.getRisk());
        dto.setReward(strategy.getReward());
        dto.setRiskPercent(strategy.getRiskPercent());
//        dto.setActive(strategy.getActive());
//        dto.setPublicStrategy(strategy.getPublicStrategy());
        dto.setCreatedAt(strategy.getCreatedAt());
        dto.setUpdatedAt(strategy.getUpdatedAt());

        if(strategy.getChecklists() != null){
            List<ChecklistDto> checklists =
                    strategy.getChecklists()
                            .stream()
                            .map(checklist -> {
                                ChecklistDto checklistDto =new ChecklistDto();
                                checklistDto.setId(checklist.getId());
                                checklistDto.setName(checklist.getName());
                                checklistDto.setDescription(checklist.getDescription());
                                checklistDto.setScope(checklist.getScope());
                                checklistDto.setActive(checklist.getActive());
                                checklistDto.setCreatedAt(checklist.getCreatedAt());
                                checklistDto.setUpdatedAt(checklist.getUpdatedAt());
                                checklistDto.setStrategyId(checklist.getStrategy().getId());
                                return checklistDto;
                            }).toList();
        }
        return dto;
    }

}