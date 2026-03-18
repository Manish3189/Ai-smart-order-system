package com.inventory.product.controller;

import com.inventory.product.dto.ProductDTO;
import com.inventory.product.model.Product;
import com.inventory.product.response.ApiResponse;
import com.inventory.product.response.PaginatedResponse;
import com.inventory.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Fetch all products with filters")
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductDTO>>> getAllProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction,
            @RequestParam(required = false)String name,
            @RequestParam(required = false)Double minPrice ,
            @RequestParam(required = false) Double maxPrice)

    {
        logger.info("Get/products Apt called");

        PaginatedResponse<ProductDTO> data =productService.getAllProducts(page, size,sortBy,direction,name,minPrice,maxPrice);

        ApiResponse<PaginatedResponse<ProductDTO>> response=
                new ApiResponse<>(true,"Product fetch succesfully",data);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByid(@PathVariable Long id){
        Product product = productService.getProductByID(id);
        ProductDTO dto  = modelMapper.map(product,ProductDTO.class);

        ApiResponse<ProductDTO> response =
                new ApiResponse<>(true,"Product fetch succesfullly ", dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO){
        Product savedProduct=productService.saveProduct(productDTO);

        ProductDTO dto= modelMapper.map(savedProduct,ProductDTO.class);
        ApiResponse <ProductDTO> response =
                new ApiResponse<>(true,"Product created succesfully", dto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id ,@RequestBody Product product){
        return productService.updateProduct(id,product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id ){
        productService.deleteProduct(id);
    }

}
