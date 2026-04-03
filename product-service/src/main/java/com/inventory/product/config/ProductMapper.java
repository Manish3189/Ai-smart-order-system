package com.inventory.product.config;

import com.inventory.product.dto.ProductRequestDTo;
import com.inventory.product.dto.ProductResponseDTO;
import com.inventory.product.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product toEntity(ProductRequestDTo dto){
        return modelMapper.map(dto,Product.class);
    }

    public ProductResponseDTO toDTO(Product product ){
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);

        dto.setCategoryName(
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );

        return dto;
    }
}