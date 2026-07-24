package org.example.journex.mapper;

import org.example.journex.domain.Strategy;
import org.example.journex.domain.User;
import org.example.journex.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "strategies", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "strategyIds", source = "strategies", qualifiedByName = "strategiesToIds")
    UserDto toDto(User entity);

    List<User> toEntities(List<UserDto> dtos);

    List<UserDto> toDtos(List<User> entities);

    @Named("strategiesToIds")
    default List<Long> strategiesToIds(List<Strategy> strategies) {
        if (strategies == null) {
            return null;
        }
        return strategies.stream()
                .map(Strategy::getId)
                .collect(Collectors.toList());
    }
}