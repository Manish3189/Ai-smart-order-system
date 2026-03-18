package com.inventory.product.repository;

import com.inventory.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByPriceBetween(Double minPrice,Double maxPrice,Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );


}

