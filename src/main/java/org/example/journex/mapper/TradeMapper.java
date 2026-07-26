package org.example.journex.mapper;

import org.example.journex.domain.Strategy;
import org.example.journex.domain.Trade;
import org.example.journex.model.TradeDto;
import org.example.journex.model.TradeOpenRequestDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeMapper {

    @Mapping(target = "strategy", source = "strategyId", qualifiedByName = "strategyFromId")
    @Mapping(target = "user", ignore = true)
    Trade toEntity(TradeOpenRequestDto dto);

    @Mapping(target = "strategyId", source = "strategy.id")
    @Mapping(target = "userId", source = "user.id")
    TradeDto toDto(Trade entity);

    @Named("strategyFromId")
    default Strategy strategyFromId(Long id) {
        if (id == null) return null;
        Strategy strategy = new Strategy();
        strategy.setId(id);
        return strategy;
    }

}