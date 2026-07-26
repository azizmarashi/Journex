package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.StrategyRepository;
import org.example.journex.dao.TradeRepository;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.Trade;
import org.example.journex.domain.User;
import org.example.journex.enums.TradeStatus;
import org.example.journex.mapper.TradeMapper;
import org.example.journex.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private AuthService authService;

    @Transactional
    public Long openTrade(TradeOpenRequestDto dto) {
        User user = authService.getCurrentUser();
        Trade trade = tradeMapper.toEntity(dto);
        trade.setUser(user);
        trade.setStatus(TradeStatus.OPEN);
        trade.setCreatedAt(LocalDateTime.now());
        trade.setDeleted(false);
        if (dto.getStrategyId() != null) {
            Strategy strategy = strategyRepository.findById(dto.getStrategyId())
                    .orElseThrow(() -> new JournexException("error.strategy.notFound"));
            if (!strategy.getUser().getId().equals(user.getId()))
                throw new JournexException("error.access.denied");
            trade.setStrategy(strategy);
        }
        Trade saved = tradeRepository.save(trade);
        return saved.getId();
    }

    @Transactional
    public void updateJournal(Long tradeId, TradeJournalUpdateDto dto) {
        Trade trade = getOwnedTrade(tradeId);
        if (dto.getDescription() != null)
            trade.setDescription(dto.getDescription());
        if (dto.getEmotionBefore() != null)
            trade.setEmotionBefore(dto.getEmotionBefore());
        if (dto.getEmotionAfter() != null)
            trade.setEmotionAfter(dto.getEmotionAfter());
        if (dto.getTags() != null)
            trade.setTags(dto.getTags());
        trade.setUpdatedAt(LocalDateTime.now());
        tradeRepository.save(trade);
    }

    @Transactional
    public void updateRiskLevels(Long tradeId, TradeRiskUpdateDto dto) {
        Trade trade = getOwnedTrade(tradeId);
        if (trade.getStatus() != TradeStatus.OPEN)
            throw new JournexException("error.trade.notOpen");
        if (dto.getStopLoss() != null)
            trade.setStopLoss(dto.getStopLoss());
        if (dto.getTakeProfit() != null)
            trade.setTakeProfit(dto.getTakeProfit());
        trade.setUpdatedAt(LocalDateTime.now());
        tradeRepository.save(trade);
    }

    @Transactional
    public void closeTrade(Long tradeId, TradeCloseRequestDto dto) {
        Trade trade = getOwnedTrade(tradeId);
        if (trade.getStatus() != TradeStatus.OPEN)
            throw new JournexException("error.trade.notOpen");
        trade.setExitPrice(dto.getExitPrice());
        trade.setExitTime(dto.getExitTime());
        trade.setCommission(dto.getCommission());
        trade.setSwap(dto.getSwap());
        trade.setBalanceAfterTrade(dto.getBalanceAfterTrade());
        trade.setEmotionAfter(dto.getEmotionAfter());
        trade.setStatus(TradeStatus.CLOSED);
        calculateResults(trade);
        trade.setUpdatedAt(LocalDateTime.now());
        tradeRepository.save(trade);
    }

    private void calculateResults(Trade trade) {
        BigDecimal entry = trade.getEntryPrice();
        BigDecimal exit = trade.getExitPrice();
        BigDecimal lotSize = BigDecimal.valueOf(trade.getLotSize());
        BigDecimal priceDiff = exit.subtract(entry);
        boolean isShort = trade.getTradePositionSide() != null
                && "SHORT".equalsIgnoreCase(trade.getTradePositionSide().name());
        if (isShort) priceDiff = priceDiff.negate();
        BigDecimal grossProfit = priceDiff.multiply(lotSize);
        BigDecimal commission = trade.getCommission() != null ? trade.getCommission() : BigDecimal.ZERO;
        BigDecimal swap = trade.getSwap() != null ? trade.getSwap() : BigDecimal.ZERO;
        BigDecimal netProfit = grossProfit.subtract(commission).subtract(swap);
        trade.setProfitLoss(netProfit);
        if (entry.compareTo(BigDecimal.ZERO) != 0) {
            double percent = priceDiff.divide(entry, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
            trade.setProfitLossPercent(percent);
        }
        if (trade.getStopLoss() != null) {
            BigDecimal riskDistance = entry.subtract(trade.getStopLoss()).abs();
            BigDecimal rewardDistance = priceDiff.abs();
            if (riskDistance.compareTo(BigDecimal.ZERO) != 0) {
                double rr = rewardDistance.divide(riskDistance, 4, RoundingMode.HALF_UP).doubleValue();
                trade.setRiskRewardRatio(rr);
            }
        }
    }

    @Transactional
    public void softDelete(Long tradeId) {
        Trade trade = getOwnedTrade(tradeId);
        trade.setDeleted(true);
        trade.setDeletedAt(LocalDateTime.now());
        tradeRepository.save(trade);
    }

    @Transactional
    public void restoreDeleted(Long tradeId) {
        User user = authService.getCurrentUser();
        Trade trade = tradeRepository.findByIdAndUserIdAndDeletedTrue(tradeId, user.getId())
                .orElseThrow(() -> new JournexException("error.trade.notFound"));
        trade.setDeleted(false);
        trade.setDeletedAt(null);
        tradeRepository.save(trade);
    }

    @Transactional(readOnly = true)
    public TradeDto findById(Long tradeId) {
        return tradeMapper.toDto(getOwnedTrade(tradeId));
    }

    @Transactional(readOnly = true)
    public Page<TradeDto> findAllByUserId(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Trade> page = tradeRepository.findAllActiveByUserId(user.getId(), pagination.toPageable());
        return page.map(tradeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TradeDto> findAllByStatus(TradeStatus status, Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Trade> page = tradeRepository.findAllByUserIdAndStatus(user.getId(), status, pagination.toPageable());
        return page.map(tradeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TradeDto> findAllByStrategyId(Long strategyId, Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Trade> page = tradeRepository.findAllByStrategyIdAndUserId(strategyId, user.getId(), pagination.toPageable());
        return page.map(tradeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TradeDto> findAllDeletedByUserId(Pagination pagination) {
        User user = authService.getCurrentUser();
        Page<Trade> page = tradeRepository.findAllDeletedByUserId(user.getId(), pagination.toPageable());
        return page.map(tradeMapper::toDto);
    }

    private Trade getOwnedTrade(Long tradeId) {
        User user = authService.getCurrentUser();
        return tradeRepository.findByIdAndUserIdAndDeletedFalseOrDeletedIsNull(tradeId, user.getId())
                .orElseThrow(() -> new JournexException("error.trade.notFound"));
    }

}