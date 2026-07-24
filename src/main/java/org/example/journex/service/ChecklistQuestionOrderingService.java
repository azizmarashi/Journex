package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.ChecklistQuestionRepository;
import org.example.journex.domain.ChecklistItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ChecklistQuestionOrderingService {

    @Autowired
    private ChecklistQuestionRepository repository;

    public void reserveSlotForInsert(Long checklistId, Long newOrder) {
        List<ChecklistItem> questions =
                repository.findByChecklistIdAndOrderIndexGreaterThanEqualOrderByOrderIndex(
                        checklistId, newOrder);

        Collections.reverse(questions);

        for (ChecklistItem q : questions) {
            q.setOrderIndex(q.getOrderIndex() + 1);
        }
        repository.saveAll(questions);
    }

    public void reorderForMove(Long checklistId, Long oldOrder, Long newOrder) {
        if (oldOrder.equals(newOrder)) {
            return;
        }

        List<ChecklistItem> questions =
                repository.findByChecklistIdOrderByOrderIndex(checklistId);

        if (oldOrder < newOrder) {
            for (ChecklistItem q : questions) {
                if (q.getOrderIndex() > oldOrder && q.getOrderIndex() <= newOrder) {
                    q.setOrderIndex(q.getOrderIndex() - 1);
                }
            }
        } else {
            Collections.reverse(questions);
            for (ChecklistItem q : questions) {
                if (q.getOrderIndex() >= newOrder && q.getOrderIndex() < oldOrder) {
                    q.setOrderIndex(q.getOrderIndex() + 1);
                }
            }
        }

        repository.saveAll(questions);
    }

    public void closeSlotAfterDelete(Long checklistId, Long deletedOrder) {
        List<ChecklistItem> questions =
                repository.findByChecklistIdAndOrderIndexGreaterThanOrderByOrderIndex(
                        checklistId, deletedOrder);

        for (ChecklistItem q : questions) {
            q.setOrderIndex(q.getOrderIndex() - 1);
        }
        repository.saveAll(questions);
    }

    public long countByChecklistId(Long checklistId) {
        return repository.countByChecklistId(checklistId);
    }

    public void validateOrderIndex(Long checklistId, Long orderIndex, boolean isMoveOfExisting) {
        long count = countByChecklistId(checklistId);
        long maxAllowed = isMoveOfExisting ? count : count + 1;

        if (orderIndex < 1) throw new JournexException("error.question.invalidOrderIndex");
        if (orderIndex > maxAllowed) throw new JournexException("error.question.invalidOrderIndex");
    }

}
