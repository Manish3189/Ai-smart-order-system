package com.inventory.product.controller;

import com.inventory.product.dto.ProductDTO;
import com.inventory.product.model.Product;
import com.inventory.product.response.ApiResponse;
import com.inventory.product.service.ProductService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(){
        List<ProductDTO> products=productService.getAllProducts();

        ApiResponse<List<ProductDTO>> response=
                new ApiResponse<>(true,"Product fetch succesfully",products);
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
