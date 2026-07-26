package org.example.journex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String description;

    private Long strategyId;

    private TradeMarketType tradeMarketType;

    private TradeTimeframe tradeTimeframe;

    private TradeType tradeType;

    private TradePositionSide tradePositionSide;

    private Long userId;

    private String symbol;

    private Double lotSize;

    private Integer leverage;

    private BigDecimal entryPrice;

    private BigDecimal exitPrice;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private BigDecimal stopLoss;

    private BigDecimal takeProfit;

    private BigDecimal profitLoss;

    private Double profitLossPercent;

    private Double riskRewardRatio;

    private BigDecimal commission;

    private BigDecimal swap;

    private TradeStatus status;

    private Double riskPercent;

    private BigDecimal balanceBeforeTrade;

    private BigDecimal balanceAfterTrade;

    private String emotionBefore;

    private String emotionAfter;

    private List<String> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}