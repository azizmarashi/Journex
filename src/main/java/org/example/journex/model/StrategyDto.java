package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.TradeMarketType;
import org.example.journex.enums.TradeTimeframe;
import org.example.journex.enums.TradeType;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StrategyDto {

    @NotNull
    private String address;

    @NotNull
    private String name;

    private String description;

    private List<ChecklistDto> checklists;

    @NotNull
    private UserDto user;

    private TradeType tradeType;

    private TradeMarketType tradeMarketType;

    private TradeTimeframe tradeTimeframe;

    private Long risk;

    private Long reward;

    private Long riskPercent;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean publicStrategy;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
