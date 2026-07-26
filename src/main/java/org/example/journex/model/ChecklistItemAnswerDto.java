package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemAnswerDto {

    @NotNull
    private Long itemId;

    @NotNull
    private String answerValue;
}