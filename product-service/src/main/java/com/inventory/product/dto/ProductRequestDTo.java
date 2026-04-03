package com.inventory.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequestDTo {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Positive(message = "price must be greater than 0")
    private double price;

    @PositiveOrZero (message = "Quantity cannot be nefative ")
    private int quantity;
}
