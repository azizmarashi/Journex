package org.example.journex.model;

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

    private int page;
    private int size;
    private String sortBy;
    private Sort.Direction direction;

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

}