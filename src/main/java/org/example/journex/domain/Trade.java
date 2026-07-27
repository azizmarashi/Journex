package org.example.journex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "trade_tb" , schema = "journex_db")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    @NotNull
    @Column(name = "trade_market_type")
    @Enumerated(EnumType.STRING)
    private TradeMarketType tradeMarketType;

    @NotNull
    @Column(name = "trade_timeframe")
    @Enumerated(EnumType.STRING)
    private TradeTimeframe tradeTimeframe;

    @NotNull
    @Column(name = "trade_type")
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Column(name = "position_side")
    @Enumerated(EnumType.STRING)
    private TradePositionSide tradePositionSide;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "symbol")
    private String symbol;

    @NotNull
    @Column(name = "lot_size")
    private Double lotSize;

    @Column(name = "leverage")
    private Integer leverage;

    @NotNull
    @Column(name = "entry_price")
    private BigDecimal entryPrice;

    @Column(name = "exit_price")
    private BigDecimal exitPrice;

    @NotNull
    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "stop_loss")
    private BigDecimal stopLoss;

    @Column(name = "take_profit")
    private BigDecimal takeProfit;

    @Column(name = "profit_loss")
    private BigDecimal profitLoss;

    @Column(name = "profit_loss_percent")
    private Double profitLossPercent;

    @Column(name = "risk_reward_ratio")
    private Double riskRewardRatio;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "swap")
    private BigDecimal swap;

    @Column(name = "risk_percent")
    private Double riskPercent;

    @Column(name = "account_balance_before")
    private BigDecimal balanceBeforeTrade;

    @Column(name = "account_balance_after")
    private BigDecimal balanceAfterTrade;

    @Column(name = "emotion_before")
    private String emotionBefore;

    @Column(name = "emotion_after")
    private String emotionAfter;

    @ElementCollection
    private List<String> tags;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}