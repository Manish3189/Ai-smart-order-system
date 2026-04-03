package com.inventory.product.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductResponseDTO implements Serializable {

    private Long productId;
    private String name;
    private double price;
    private int quantity;

    private String categoryName;
}
