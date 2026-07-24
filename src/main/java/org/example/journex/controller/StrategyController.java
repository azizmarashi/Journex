package org.example.journex.controller;

import jakarta.validation.Valid;
import org.example.journex.model.Pagination;
import org.example.journex.model.StrategyDto;
import org.example.journex.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public Long update( @PathVariable Long id, @Valid @RequestBody StrategyDto dto) {
        return strategyService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        strategyService.delete(id);
    }

    @GetMapping("/find-all")
    public Page<StrategyDto> findAll(Pagination pagination) {
        return strategyService.findAll(pagination);
    }

    @GetMapping("/{id}")
    public StrategyDto findById(@PathVariable Long id) {
        return strategyService.findById(id);
    }

    @GetMapping("/{address}")
    public StrategyDto findByAddress(@PathVariable String address) {
        return strategyService.findByAddress(address);
    }

    @GetMapping("/public/{address}")
    public StrategyDto findPublicByAddress(@PathVariable String address) {
        return strategyService.findPublicByAddress(address);
    }

}
