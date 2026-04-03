package com.inventory.product.controller;

import com.inventory.product.config.ProductMapper;
import com.inventory.product.dto.ProductRequestDTo;
import com.inventory.product.dto.ProductResponseDTO;
import com.inventory.product.model.Product;
import com.inventory.product.response.ApiResponse;
import com.inventory.product.response.PaginatedResponse;
import com.inventory.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "Fetch all products with filters")
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponseDTO>>> getAllProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction,
            @RequestParam(required = false)String name,
            @RequestParam(required = false)Double minPrice ,
            @RequestParam(required = false) Double maxPrice)

    {
        logger.info("Get/products Apt called");

        PaginatedResponse<ProductResponseDTO> data = productService.getAllProducts(page, size,sortBy,direction,name,minPrice,maxPrice);

        ApiResponse<PaginatedResponse<ProductResponseDTO>> response=
                new ApiResponse<>(true,"Product fetch succesfully",data);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductByid(@PathVariable Long id){
        Product product = productService.getProductByID(id);
        ProductResponseDTO dto  = productMapper.toDTO(product);

        ApiResponse<ProductResponseDTO> response =
                new ApiResponse<>(true,"Product fetch succesfullly ", dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTo productdto){

        ProductResponseDTO savedProduct=productService.saveProduct(productdto);


        ApiResponse <ProductResponseDTO> response =
                new ApiResponse<>(true,"Product created succesfully", savedProduct);
        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id ,@RequestBody Product product){
        return productService.updateProduct(id,product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id ){

        productService.deleteProduct(id);
        ApiResponse<?> response=
                new ApiResponse<>(true,"Product deleted successfully",null);
        return ResponseEntity.ok(response);
    }

}
