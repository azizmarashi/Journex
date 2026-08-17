package org.example.journex.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.journex.model.Pagination;
import org.example.journex.model.StrategyDto;
import org.example.journex.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @PostMapping("/save")
    public Long save(@Valid @RequestBody StrategyDto dto) {
        return strategyService.save(dto);
    }

    @PutMapping("/update/{id}")
    public Long update(@PathVariable Long id, @Valid @RequestBody StrategyDto dto) {
        return strategyService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        strategyService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public void restoreDeleted(@PathVariable Long id){
        strategyService.restoreDeleted(id);
    }

    @GetMapping("/{id}")
    public StrategyDto findById(@PathVariable Long id) {
        return strategyService.findById(id);
    }

    @GetMapping("/find-all")
    public Page<StrategyDto> findAll(@ModelAttribute Pagination pagination) {
        return strategyService.findAll(pagination);
    }

    @GetMapping("/find-all-deleted")
    public Page<StrategyDto> findAllDeleted(@ModelAttribute Pagination pagination) {
        return strategyService.findAllDeleted(pagination);
    }

    @GetMapping("/public/{address}")
    public StrategyDto findPublicByAddress(@PathVariable String address) {
        return strategyService.findPublicByAddress(address);
    }

}
