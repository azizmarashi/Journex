package org.example.journex.mapper;

import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "strategies", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "strategyIds", source = "strategies", qualifiedByName = "activeStrategiesToIds")
    UserDto toDto(User entity);

    List<User> toEntities(List<UserDto> dtos);

    List<UserDto> toDtos(List<User> entities);

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
}