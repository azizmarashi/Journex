package org.example.journex.mapper;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.StrategyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StrategyMapper {

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "checklists", ignore = true)
    Strategy toEntity(StrategyDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "checklistIds", source = "checklists", qualifiedByName = "checklistsToIds")
    StrategyDto toDto(Strategy entity);

    List<Strategy> toEntities(List<StrategyDto> dtos);

    List<StrategyDto> toDtos(List<Strategy> entities);

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("checklistsToIds")
    default List<Long> checklistsToIds(List<Checklist> checklists) {
        if (checklists == null) {
            return null;
        }
        return checklists.stream()
                .map(Checklist::getId)
                .collect(Collectors.toList());
    }
}