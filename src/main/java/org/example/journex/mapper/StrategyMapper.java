package org.example.journex.mapper;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.StrategyDto;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StrategyMapper {

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "checklists", ignore = true)
    Strategy toEntity(StrategyDto dto);

    @Named("toDto")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "checklistIds", source = "checklists", qualifiedByName = "activeChecklistsToIds")
    StrategyDto toDto(Strategy entity);

    @Named("toPublicDto")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "checklistIds", source = "checklists", qualifiedByName = "publicActiveChecklistsToIds")
    StrategyDto toPublicDto(Strategy entity);

    @IterableMapping(qualifiedByName = "toDto")
    List<StrategyDto> toDtos(List<Strategy> entities);

    List<Strategy> toEntities(List<StrategyDto> dtos);

    default List<StrategyDto> toPublicDtos(List<Strategy> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toPublicDto)
                .collect(Collectors.toList());
    }

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("activeChecklistsToIds")
    default List<Long> activeChecklistsToIds(List<Checklist> checklists) {
        if (checklists == null) return null;
        return checklists.stream()
                .filter(checklist -> !Boolean.TRUE.equals(checklist.getDeleted()))
                .map(Checklist::getId)
                .collect(Collectors.toList());
    }

    @Named("publicActiveChecklistsToIds")
    default List<Long> publicActiveChecklistsToIds(List<Checklist> checklists) {
        if (checklists == null) return null;
        return checklists.stream()
                .filter(checklist -> !Boolean.TRUE.equals(checklist.getDeleted()))
                .filter(checklist -> Boolean.TRUE.equals(checklist.getPublicChecklist()))
                .map(Checklist::getId)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromDto(StrategyDto dto, @MappingTarget Strategy entity);
}