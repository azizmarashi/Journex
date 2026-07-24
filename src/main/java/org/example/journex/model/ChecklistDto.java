package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistCategory;
import org.example.journex.enums.ChecklistScope;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDto {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private ChecklistScope scope;

    @NotNull
    private ChecklistCategory checklistCategory;

    private Long strategyId;

    @NotNull
    private Long userId;

    private List<Long> itemIds;

    @NotNull
    private Boolean publicChecklist;

    @NotNull
    private Boolean active;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}