package org.example.journex.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.journex.model.ChecklistItemAnswerDto;
import org.example.journex.model.ChecklistItemDto;
import org.example.journex.model.Pagination;
import org.example.journex.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/checklist-items")
public class ChecklistItemController {

    @Autowired
    private ChecklistItemService checklistItemService;

    @PostMapping("/add-question")
    public Long addQuestion(@Valid @RequestBody ChecklistItemDto dto) {
        return checklistItemService.addQuestion(dto);
    }

    @PostMapping("/answer")
    public Long answer(@Valid @RequestBody ChecklistItemAnswerDto dto) {
        return checklistItemService.answer(dto);
    }

    @PutMapping("/{itemId}")
    public Long update(@PathVariable Long itemId, @Valid @RequestBody ChecklistItemDto dto) {
        return checklistItemService.update(itemId, dto);
    }

    @PutMapping("/{itemId}/move")
    public void move(@PathVariable Long itemId, @RequestParam Long newOrder) {
        checklistItemService.move(itemId, newOrder);
    }

    @DeleteMapping("/{itemId}")
    public void softDelete(@PathVariable Long itemId) {
        checklistItemService.softDelete(itemId);
    }

    @PostMapping("/{itemId}/restore")
    public void restoreDeleted(@PathVariable Long itemId, @RequestParam Long checklistId) {
        checklistItemService.restoreDeleted(itemId, checklistId);
    }

    @GetMapping("/{itemId}")
    public ChecklistItemDto findItemById(@PathVariable Long itemId) {
        return checklistItemService.findItemById(itemId);
    }

    @GetMapping("/checklist/{checklistId}")
    public Page<ChecklistItemDto> findAllByChecklistId(
            @PathVariable Long checklistId,
            @ModelAttribute Pagination pagination) {
        return checklistItemService.findAllByChecklistId(checklistId, pagination);
    }

}
