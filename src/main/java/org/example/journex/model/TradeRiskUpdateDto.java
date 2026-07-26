package org.example.journex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRiskUpdateDto {

    private BigDecimal stopLoss;

    private BigDecimal takeProfit;
}