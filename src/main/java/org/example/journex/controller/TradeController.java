package org.example.journex.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.journex.enums.TradeStatus;
import org.example.journex.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.example.journex.service.TradeService;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping
    public Long openTrade(@Valid @RequestBody TradeOpenRequestDto dto) {
        return tradeService.openTrade(dto);
    }

    @PutMapping("/{tradeId}/journal")
    public void updateJournal(@PathVariable Long tradeId, @RequestBody TradeJournalUpdateDto dto) {
        tradeService.updateJournal(tradeId, dto);
    }

    @PatchMapping("/{tradeId}/risk")
    public void updateRiskLevels(@PathVariable Long tradeId, @RequestBody TradeRiskUpdateDto dto) {
        tradeService.updateRiskLevels(tradeId, dto);
    }

    @PostMapping("/{tradeId}/close")
    public void closeTrade(@PathVariable Long tradeId, @Valid @RequestBody TradeCloseRequestDto dto) {
        tradeService.closeTrade(tradeId, dto);
    }

    @DeleteMapping("/{tradeId}")
    public void softDelete(@PathVariable Long tradeId) {
        tradeService.softDelete(tradeId);
    }

    @PostMapping("/{tradeId}/restore")
    public void restoreDeleted(@PathVariable Long tradeId) {
        tradeService.restoreDeleted(tradeId);
    }

    @GetMapping("/{tradeId}")
    public TradeDto findById(@PathVariable Long tradeId) {
        return tradeService.findById(tradeId);
    }

    @GetMapping
    public Page<TradeDto> findAllByUserId(@ModelAttribute Pagination pagination) {
        return tradeService.findAllByUserId(pagination);
    }

    @GetMapping("/status")
    public Page<TradeDto> findAllByStatus(@RequestParam TradeStatus status, @ModelAttribute Pagination pagination) {
        return tradeService.findAllByStatus(status, pagination);
    }

    @GetMapping("/strategy/{strategyId}")
    public Page<TradeDto> findAllByStrategyId(@PathVariable Long strategyId, @ModelAttribute Pagination pagination) {
        return tradeService.findAllByStrategyId(strategyId, pagination);
    }

    @GetMapping("/trash")
    public Page<TradeDto> findAllDeletedByUserId(@ModelAttribute Pagination pagination) {
        return tradeService.findAllDeletedByUserId(pagination);
    }

}