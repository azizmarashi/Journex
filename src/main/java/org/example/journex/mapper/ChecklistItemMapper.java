package org.example.journex.mapper;

import org.example.journex.domain.Checklist;
import org.example.journex.domain.ChecklistItem;
import org.example.journex.model.ChecklistItemDto;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChecklistItemMapper {

    @Mapping(target = "checklist", source = "checklistId", qualifiedByName = "checklistFromId")
    ChecklistItem toEntity(ChecklistItemDto dto);

    @Mapping(target = "checklistId", source = "checklist.id")
    ChecklistItemDto toDto(ChecklistItem entity);

    List<ChecklistItem> toEntities(List<ChecklistItemDto> dtos);

    List<ChecklistItemDto> toDtos(List<ChecklistItem> entities);

    @Named("checklistFromId")
    default Checklist checklistFromId(Long id) {
        if (id == null) {
            return null;
        }
        Checklist checklist = new Checklist();
        checklist.setId(id);
        return checklist;
    }
}