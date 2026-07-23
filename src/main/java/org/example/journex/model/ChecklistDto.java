package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistScope;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDto {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private ChecklistScope scope;

    @NotNull
    private Long strategyId;

    @NotNull
    private Boolean active;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}