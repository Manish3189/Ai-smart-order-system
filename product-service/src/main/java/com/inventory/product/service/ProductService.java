package com.inventory.product.service;

import com.inventory.product.dto.ProductDTO;
import com.inventory.product.exception.ProductNotFoundException;
import com.inventory.product.model.Product;
import com.inventory.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    //save the product
    public Product saveProduct(ProductDTO dto){
        Product product = modelMapper.map(dto,Product.class);

//        product.setName(dto.getName());;
//        product.setPrice(dto.getPrice());
//        product.setQuantity(dto.getQuantity());

        return productRepository.save(product);
    }

    //list of product
    public List<ProductDTO> getAllProducts(){
        List<Product> products=productRepository.findAll();
//        List<ProductDTO> dtoList=new ArrayList<>();
//
//        for(Product product:products){
//            ProductDTO dto=new ProductDTO();
//
//            dto.setName(product.getName());
//            dto.setPrice(product.getPrice());
//            dto.setQuantity(product.getQuantity());
//
//            dtoList.add(dto);
//        }
        return products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
    }

    //get the product by id
    public Product getProductByID(Long id){
        return productRepository.findById(id)
                .orElseThrow( ()-> new ProductNotFoundException("Product Not Found"));
    }

    //update the product
    public Product updateProduct(Long id ,Product product){

        Product existingProduct = productRepository.findById(id).orElse(null);

        if(existingProduct!=null){
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());

            return productRepository.save(existingProduct);
        }
        return null;
    }

    public void  deleteProduct(Long id ){
        productRepository.deleteById(id);
    }

}
