package org.example.journex.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.journex.model.ChecklistDto;
import org.example.journex.model.Pagination;
import org.example.journex.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @PostMapping("/save")
    public Long save(@Valid @RequestBody ChecklistDto dto) {
        return checklistService.save(dto);
    }

    @PutMapping("/update/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody ChecklistDto dto) {
        return checklistService.update(id, dto);
    }

    @GetMapping("/find-all")
    public Page<ChecklistDto> findAll(Pagination pagination) {
        return checklistService.findAllByUserId(pagination);
    }

    @GetMapping("/find-all-actives")
    public Page<ChecklistDto> findAllActives(Pagination pagination) {
        return checklistService.findAllActives(pagination);
    }

    @GetMapping("/find-by-strategy/{strategyId}")
    public Page<ChecklistDto> findActivesByStrategyId(@PathVariable Long strategyId, Pagination pagination) {
        return checklistService.findActivesByStrategyId(strategyId, pagination);
    }

    @GetMapping("/find-all-deleted")
    public Page<ChecklistDto> findAllDeleted(Pagination pagination) {
        return checklistService.findAllDeleted(pagination);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        checklistService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public void restoreDeleted(@PathVariable Long id){
        checklistService.restoreDeleted(id);
    }

}
