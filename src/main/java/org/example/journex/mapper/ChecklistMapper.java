package org.example.journex.mapper;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistItem;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.ChecklistDto;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChecklistMapper {

    @Mapping(target = "strategy", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "strategies", ignore = true)
    Checklist toEntity(ChecklistDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "itemIds", source = "items", qualifiedByName = "activeItemsToIds")
    @Mapping(target = "strategyIds", source = "strategies", qualifiedByName = "activeStrategiesToIds")
    ChecklistDto toDto(Checklist entity);

    List<Checklist> toEntities(List<ChecklistDto> dtos);

    List<ChecklistDto> toDtos(List<Checklist> entities);

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("activeItemsToIds")
    default List<Long> activeItemsToIds(List<ChecklistItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .filter(item -> !Boolean.TRUE.equals(item.getDeleted()))
                .map(ChecklistItem::getId)
                .collect(Collectors.toList());
    }

    @Named("activeStrategiesToIds")
    default List<Long> activeStrategiesToIds(List<Strategy> strategies) {
        if (strategies == null) {
            return null;
        }
        return strategies.stream()
                .filter(strategy -> !Boolean.TRUE.equals(strategy.getDeleted()))
                .map(Strategy::getId)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromDto(ChecklistDto dto, @MappingTarget Checklist entity);
}