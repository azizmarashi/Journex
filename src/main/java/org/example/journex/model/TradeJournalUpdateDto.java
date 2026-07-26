package org.example.journex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeJournalUpdateDto {

    private String description;

    private String emotionBefore;

    private String emotionAfter;

    private List<String> tags;
}