package org.example.journex.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.ChecklistCategory;
import org.example.journex.enums.ChecklistQuestionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistQuestionDto {

    @NotNull
    @Size(min = 5, max = 500)
    private String checklistQuestion;

    @NotNull
    private ChecklistQuestionType questionType;

    @NotNull
    private ChecklistCategory checklistCategory;

    @NotNull
    @Positive
    private Long checklistId;

    @NotNull
    private Boolean required;

    @NotNull
    @Positive
    private Long orderIndex;

}