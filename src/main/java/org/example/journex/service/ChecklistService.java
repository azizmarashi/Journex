package org.example.journex.service;

import org.example.journex.config.exception.JournexException;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.User;
import org.example.journex.mapper.ChecklistMapper;
import org.example.journex.model.ChecklistDto;
import org.example.journex.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ChecklistMapper checklistMapper;

    @Autowired
    private AuthService authService;

    @Transactional
    public Long save(ChecklistDto dto) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistMapper.toEntity(dto);
        checklist.setUser(user);
        checklist.setCreatedAt(LocalDateTime.now());
        checklist.setDeleted(false);
        Checklist saved = checklistRepository.save(checklist);
        return saved.getId();
    }

    @Transactional
    public Long update(Long checklistId, ChecklistDto dto) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistRepository.findByIdAndUserId(checklistId, user.getId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checklistMapper.updateEntityFromDto(dto, checklist);
        checklist.setUpdatedAt(LocalDateTime.now());
        Checklist saved = checklistRepository.save(checklist);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDto> findAllByUserId(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Checklist> page = checklistRepository.findAllByUserId(user.getId(), pagination.toPageable());
        return page.map(checklistMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDto> findAllActives(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Checklist> page = checklistRepository.findAllActives(user.getId(), pagination.toPageable());
        return page.map(checklistMapper::toDto);
    }

    public Page<ChecklistDto> findActivesByStrategyId(Long strategyId, Pagination pagination){
        User user = authService.getCurrentUser();
        Page<Checklist> page = checklistRepository.findActivesByStrategyId(user.getId(), strategyId, pagination.toPageable());
        return page.map(checklistMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDto> findAllDeleted(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Checklist> page = checklistRepository.findAllDeleted(user.getId(), pagination.toPageable());
        return page.map(checklistMapper::toDto);
    }

    @Transactional
    public void softDelete(Long checklistId) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistRepository.findByIdAndUserId(checklistId, user.getId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checklist.setDeleted(true);
        checklist.setActive(false);
        checklist.setDeletedAt(LocalDateTime.now());
        checklistRepository.save(checklist);
    }

    @Transactional
    public void restoreDeleted(Long checklistId) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistRepository.findDeleted(checklistId, user.getId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checklist.setDeleted(false);
        checklist.setDeletedAt(null);
        checklistRepository.save(checklist);
    }


    @Transactional
    public List<Checklist> resolveChecklists(List<Long> checklistIds, User currentUser) {
        List<Checklist> checklists = checklistRepository.findAllByIds(checklistIds);
        if (checklists.size() != checklistIds.size())
            throw new JournexException("error.checklist.notFound");
        for (Checklist checklist : checklists) {
            boolean isOwner = checklist.getUser().getId().equals(currentUser.getId());
            boolean isPublic = Boolean.TRUE.equals(checklist.getPublicChecklist());
            if (!isOwner && !isPublic)
                throw new JournexException("error.checklist.accessDenied");
        }
        return new ArrayList<>(checklists);
    }

}