package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeCloseRequestDto {

    @NotNull
    private BigDecimal exitPrice;

    @NotNull
    private LocalDateTime exitTime;

    private BigDecimal commission;

    private BigDecimal swap;

    private BigDecimal balanceAfterTrade;

    private String emotionAfter;
}