package org.example.journex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.TradeMarketType;
import org.example.journex.enums.TradeType;
import org.example.journex.enums.TradeTimeframe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "strategy_tb", schema = "journex_db")
@Data
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "name")
    private String name;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @OneToMany(
            mappedBy = "strategy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Checklist> checklists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "trade_type")
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Column(name = "trade_market_type")
    @Enumerated(EnumType.STRING)
    private TradeMarketType tradeMarketType;

    @Column(name = "trade_timeframe")
    @Enumerated(EnumType.STRING)
    private TradeTimeframe tradeTimeframe;

    @Column(name = "risk")
    private Long risk;

    @Column(name = "reward")
    private Long reward;

    @Column(name = "risk_percent")
    private Long riskPercent;

    @NotNull
    @Column(name = "public_strategy")
    private Boolean publicStrategy;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}