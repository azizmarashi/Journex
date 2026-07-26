package org.example.journex.service;

import org.example.journex.config.exception.JournexException;
import org.example.journex.dao.ChecklistItemRepository;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistItem;
import org.example.journex.domain.User;
import org.example.journex.enums.ChecklistItemType;
import org.example.journex.mapper.ChecklistItemMapper;
import org.example.journex.model.ChecklistItemAnswerDto;
import org.example.journex.model.ChecklistItemDto;
import org.example.journex.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ChecklistItemOrderingService orderingService;

    @Autowired
    private ChecklistItemMapper checklistItemMapper;

    @Autowired
    private AuthService authService;

    @Transactional
    public Long addQuestion(ChecklistItemDto dto) {
        User user = authService.getCurrentUser();
        validateIsQuestionType(dto.getType());
        Checklist checklist = checklistRepository.findById(dto.getChecklistId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checkOwnership(checklist, user);
        orderingService.validateOrderIndex(dto.getChecklistId(), dto.getOrderIndex(), false);
        orderingService.reserveSlotForInsert(dto.getChecklistId(), dto.getOrderIndex());
        ChecklistItem entity = checklistItemMapper.toEntity(dto);
        entity.setChecklist(checklist);
        entity.setDeleted(false);
        ChecklistItem saved = checklistItemRepository.save(entity);
        return saved.getId();
    }

    @Transactional
    public Long update(Long itemId, ChecklistItemDto dto) {
        User user = authService.getCurrentUser();
        validateIsQuestionType(dto.getType());
        ChecklistItem entity = checklistItemRepository.findQuestionById(itemId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        checkOwnership(entity.getChecklist(), user);
        Checklist newChecklist = checklistRepository.findById(dto.getChecklistId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checkOwnership(newChecklist, user);
        Long oldChecklistId = entity.getChecklist().getId();
        Long oldOrder = entity.getOrderIndex();
        Long newOrder = dto.getOrderIndex();
        boolean checklistChanged = !oldChecklistId.equals(dto.getChecklistId());
        if (checklistChanged) {
            orderingService.validateOrderIndex(dto.getChecklistId(), newOrder, false);
            orderingService.closeSlotAfterDelete(oldChecklistId, oldOrder);
            orderingService.reserveSlotForInsert(dto.getChecklistId(), newOrder);
        } else if (!oldOrder.equals(newOrder)) {
            orderingService.validateOrderIndex(dto.getChecklistId(), newOrder, true);
            orderingService.reorderForMove(dto.getChecklistId(), oldOrder, newOrder);
        }
        entity.setValue(dto.getValue());
        entity.setType(dto.getType());
        entity.setChecklist(newChecklist);
        entity.setRequired(dto.getRequired());
        entity.setOrderIndex(newOrder);
        ChecklistItem saved = checklistItemRepository.save(entity);
        return saved.getId();
    }

    @Transactional
    public void move(Long itemId, Long newOrder) {
        User user = authService.getCurrentUser();
        ChecklistItem entity = checklistItemRepository.findQuestionById(itemId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        checkOwnership(entity.getChecklist(), user);
        Long checklistId = entity.getChecklist().getId();
        Long oldOrder = entity.getOrderIndex();
        if (oldOrder.equals(newOrder)) return;
        orderingService.validateOrderIndex(checklistId, newOrder, true);
        orderingService.reorderForMove(checklistId, oldOrder, newOrder);
        entity.setOrderIndex(newOrder);
        checklistItemRepository.save(entity);
    }

    @Transactional
    public void softDelete(Long itemId) {
        User user = authService.getCurrentUser();
        ChecklistItem entity = checklistItemRepository.findQuestionById(itemId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        checkOwnership(entity.getChecklist(), user);
        Long checklistId = entity.getChecklist().getId();
        Long deletedOrder = entity.getOrderIndex();
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        checklistItemRepository.save(entity);
        orderingService.closeSlotAfterDelete(checklistId, deletedOrder);
    }

    @Transactional
    public void restoreDeleted(Long itemId, Long checklistId) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checkOwnership(checklist, user);
        ChecklistItem entity = checklistItemRepository.findByIdAndChecklistIdAndDeletedTrue(itemId, checklistId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        long count = orderingService.countByChecklistId(checklistId);
        entity.setDeleted(false);
        entity.setDeletedAt(null);
        entity.setOrderIndex(count + 1);
        checklistItemRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public ChecklistItemDto findItemById(Long itemId) {
        User user = authService.getCurrentUser();
        ChecklistItem entity = checklistItemRepository.findQuestionById(itemId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        checkOwnership(entity.getChecklist(), user);
        return checklistItemMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistItemDto> findAllByChecklistId(Long checklistId, Pagination pagination) {
        User user = authService.getCurrentUser();
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));
        checkOwnership(checklist, user);
        Page<ChecklistItem> page =
                checklistItemRepository.findAllByChecklistId(checklistId, pagination.toPageable());
        return page.map(checklistItemMapper::toDto);
    }

    @Transactional
    public Long answer(ChecklistItemAnswerDto dto) {
        User user = authService.getCurrentUser();
        ChecklistItem question = checklistItemRepository.findQuestionById(dto.getItemId())
                .orElseThrow(() -> new JournexException("error.question.notFound"));
        checkOwnership(question.getChecklist(), user);
        ChecklistItemType answerType = resolveAnswerType(question.getType());
        validateAnswerValue(question.getType(), dto.getAnswerValue());
        ChecklistItem answer = new ChecklistItem();
        answer.setValue(dto.getAnswerValue());
        answer.setType(answerType);
        answer.setChecklist(question.getChecklist());
        answer.setRequired(question.getRequired());
        answer.setOrderIndex(question.getOrderIndex());
        answer.setSourceQuestion(question);
        answer.setAnsweredAt(LocalDateTime.now());
        answer.setDeleted(false);
        ChecklistItem saved = checklistItemRepository.save(answer);
        return saved.getId();
    }

    private void checkOwnership(Checklist checklist, User user) {
        if (!checklist.getUser().getId().equals(user.getId()))
            throw new JournexException("error.access.denied");
    }

    private ChecklistItemType resolveAnswerType(ChecklistItemType questionType) {
        return switch (questionType) {
            case QUESTION_BOOLEAN -> ChecklistItemType.ANSWER_BOOLEAN;
            case QUESTION_TEXT -> ChecklistItemType.ANSWER_TEXT;
            case ANSWER_BOOLEAN, ANSWER_TEXT ->
                    throw new JournexException("error.question.notAQuestion");
        };
    }

    private void validateIsQuestionType(ChecklistItemType type) {
        if (type != ChecklistItemType.QUESTION_BOOLEAN && type != ChecklistItemType.QUESTION_TEXT) {
            throw new JournexException("error.question.invalidType");
        }
    }

    private void validateAnswerValue(ChecklistItemType questionType, String answerValue) {
        if (questionType == ChecklistItemType.QUESTION_BOOLEAN) {
            if (!"true".equalsIgnoreCase(answerValue) && !"false".equalsIgnoreCase(answerValue))
                throw new JournexException("error.answer.invalidBooleanValue");
        }
    }

}