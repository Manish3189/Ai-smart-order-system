package com.inventory.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.ReactiveAdapterRegistry;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponse<T> implements Serializable {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private Long totalElements;
}
