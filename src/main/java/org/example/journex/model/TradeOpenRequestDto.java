package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.TradeMarketType;
import org.example.journex.enums.TradePositionSide;
import org.example.journex.enums.TradeTimeframe;
import org.example.journex.enums.TradeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeOpenRequestDto {

    private String description;

    private Long strategyId;

    @NotNull
    private TradeMarketType tradeMarketType;

    @NotNull
    private TradeTimeframe tradeTimeframe;

    @NotNull
    private TradeType tradeType;

    private TradePositionSide tradePositionSide;

    @NotNull
    private String symbol;

    @NotNull
    private Double lotSize;

    private Integer leverage;

    @NotNull
    private BigDecimal entryPrice;

    @NotNull
    private LocalDateTime entryTime;

    private BigDecimal stopLoss;

    private BigDecimal takeProfit;

    private Double riskPercent;

    private BigDecimal balanceBeforeTrade;

    private String emotionBefore;

    private List<String> tags;

}