package org.example.journex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistItemType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String value;

    @NotNull
    private ChecklistItemType type;

    @NotNull
    private Boolean required;

    @NotNull
    @Positive
    private Long orderIndex;

    private Long checklistId;
}