package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistQuestionRepository;
import org.example.journex.dao.ChecklistRepository;
import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistQuestion;
import org.example.journex.model.ChecklistQuestionDto;
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

    public Long save (ChecklistQuestionDto model){

        Checklist checklist = checklistRepository.findById(model.getChecklistId())
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        orderingService.validateOrderIndex(model.getChecklistId(), model.getOrderIndex(), false);
        orderingService.reserveSlotForInsert(model.getChecklistId(), model.getOrderIndex());

        ChecklistQuestion entity = new ChecklistQuestion();
        entity.setChecklist(checklist);
        entity.setChecklistQuestion(model.getChecklistQuestion());
        entity.setQuestionType(model.getQuestionType());
        entity.setChecklistCategory(model.getChecklistCategory());
        entity.setRequired(model.getRequired());
        entity.setOrderIndex(model.getOrderIndex());

        checklistQuestionRepository.save(entity);

        return entity.getId();
    }

    public Long update(Long checklistQuestionId, ChecklistQuestionDto dto) {

        ChecklistQuestion entity = checklistQuestionRepository.findById(checklistQuestionId)
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
        entity.setQuestionType(dto.getQuestionType());
        entity.setChecklistCategory(dto.getChecklistCategory());
        entity.setChecklist(checklist);
        entity.setRequired(dto.getRequired());
        entity.setOrderIndex(dto.getOrderIndex());

        checklistQuestionRepository.save(entity);

        return entity.getId();
    }

    public void delete (Long questionId){
        ChecklistQuestion question = checklistQuestionRepository.findById(questionId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));

        Long checklistId = question.getChecklist().getId();
        Long deletedOrder = question.getOrderIndex();

        checklistQuestionRepository.delete(question);
        orderingService.closeSlotAfterDelete(checklistId, deletedOrder);
    }

    public ChecklistQuestionDto findById(Long questionId){

        ChecklistQuestion question = checklistQuestionRepository.findById(questionId)
                .orElseThrow(() -> new JournexException("error.question.notFound"));

        ChecklistQuestionDto dto = new ChecklistQuestionDto();

        dto.setChecklistId(question.getChecklist().getId());
        dto.setChecklistQuestion(question.getChecklistQuestion());
        dto.setQuestionType(question.getQuestionType());
        dto.setChecklistCategory(question.getChecklistCategory());
        dto.setRequired(question.getRequired());
        dto.setOrderIndex(question.getOrderIndex());

        return dto;
    }

    public Page<ChecklistQuestionDto> findAllByChecklistId(Long checklistId, Pagination pagination){

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new JournexException("error.checklist.notFound"));

        Page<ChecklistQuestion> entityPage =
                checklistQuestionRepository.findAllByChecklistId(checklistId, pagination.toPageable());

        return entityPage.map(entity -> {
            ChecklistQuestionDto dto = new ChecklistQuestionDto();
            dto.setChecklistQuestion(entity.getChecklistQuestion());
            dto.setQuestionType(entity.getQuestionType());
            dto.setChecklistCategory(entity.getChecklistCategory());
            dto.setChecklistId(entity.getChecklist().getId());
            dto.setRequired(entity.getRequired());
            dto.setOrderIndex(entity.getOrderIndex());
            return dto;
        });

    }

}