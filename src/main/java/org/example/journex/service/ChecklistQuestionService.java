package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistQuestionRepository;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistItem;
import org.example.journex.model.ChecklistItemDto;
import org.example.journex.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecklistQuestionService {

    @Autowired
    private ChecklistQuestionRepository checklistQuestionRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ChecklistQuestionOrderingService orderingService;

    public Long save (ChecklistItemDto model){

        Checklist checklist = checklistRepository.findById(model.getChecklistId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        orderingService.validateOrderIndex(model.getChecklistId(), model.getOrderIndex(), false);
        orderingService.reserveSlotForInsert(model.getChecklistId(), model.getOrderIndex());

        ChecklistItem entity = new ChecklistItem();
        entity.setChecklist(checklist);
        entity.setChecklistQuestion(model.getChecklistQuestion());
        entity.setItemType(model.getQuestionType());
        entity.setRequired(model.getRequired());
        entity.setOrderIndex(model.getOrderIndex());

        checklistQuestionRepository.save(entity);

        return entity.getId();
    }

    public Long update(Long checklistQuestionId, ChecklistItemDto dto) {

        ChecklistItem entity = checklistQuestionRepository.findById(checklistQuestionId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));

        Checklist checklist = checklistRepository.findById(dto.getChecklistId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

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

        entity.setChecklistQuestion(dto.getChecklistQuestion());
        entity.setItemType(dto.getQuestionType());
        entity.setChecklist(checklist);
        entity.setRequired(dto.getRequired());
        entity.setOrderIndex(dto.getOrderIndex());

        checklistQuestionRepository.save(entity);

        return entity.getId();
    }

    public void delete (Long questionId){
        ChecklistItem question = checklistQuestionRepository.findById(questionId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));

        Long checklistId = question.getChecklist().getId();
        Long deletedOrder = question.getOrderIndex();

        checklistQuestionRepository.delete(question);
        orderingService.closeSlotAfterDelete(checklistId, deletedOrder);
    }

    public ChecklistItemDto findById(Long questionId){

        ChecklistItem question = checklistQuestionRepository.findById(questionId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));

        ChecklistItemDto dto = new ChecklistItemDto();

        dto.setChecklistId(question.getChecklist().getId());
        dto.setChecklistQuestion(question.getChecklistQuestion());
        dto.setQuestionType(question.getItemType());
        dto.setRequired(question.getRequired());
        dto.setOrderIndex(question.getOrderIndex());

        return dto;
    }

    public Page<ChecklistItemDto> findAllByChecklistId(Long checklistId, Pagination pagination){

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        Page<ChecklistItem> entityPage =
                checklistQuestionRepository.findAllByChecklistId(checklistId, pagination.toPageable());

        return entityPage.map(entity -> {
            ChecklistItemDto dto = new ChecklistItemDto();
            dto.setChecklistQuestion(entity.getChecklistQuestion());
            dto.setQuestionType(entity.getItemType());
            dto.setChecklistId(entity.getChecklist().getId());
            dto.setRequired(entity.getRequired());
            dto.setOrderIndex(entity.getOrderIndex());
            return dto;
        });

    }

}