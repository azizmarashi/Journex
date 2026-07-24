package org.example.journex.mapper;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistItem;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.ChecklistDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChecklistMapper {

    @Mapping(target = "strategy", source = "strategyId", qualifiedByName = "strategyFromId")
    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "items", ignore = true)
    Checklist toEntity(ChecklistDto dto);

    @Mapping(target = "strategyId", source = "strategy.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "itemIds", source = "items", qualifiedByName = "itemsToIds")
    ChecklistDto toDto(Checklist entity);

    List<Checklist> toEntities(List<ChecklistDto> dtos);

    List<ChecklistDto> toDtos(List<Checklist> entities);

    @Named("strategyFromId")
    default Strategy strategyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Strategy strategy = new Strategy();
        strategy.setId(id);
        return strategy;
    }

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("itemsToIds")
    default List<Long> itemsToIds(List<ChecklistItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(ChecklistItem::getId)
                .collect(Collectors.toList());
    }
}
