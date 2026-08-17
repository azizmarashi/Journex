package org.example.journex.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    private String sortBy = "id";

    private Sort.Direction direction = Sort.Direction.DESC;

    public Pageable toPageable() {
        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
    }
}