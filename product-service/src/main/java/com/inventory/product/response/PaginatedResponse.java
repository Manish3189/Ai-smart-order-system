package com.inventory.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.ReactiveAdapterRegistry;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private Long totalElements;
}
