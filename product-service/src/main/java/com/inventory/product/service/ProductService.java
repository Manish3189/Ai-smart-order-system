package com.inventory.product.service;

import com.inventory.product.config.ProductMapper;
import com.inventory.product.dto.ProductRequestDTo;
import com.inventory.product.dto.ProductResponseDTO;
import com.inventory.product.exception.ProductNotFoundException;
import com.inventory.product.model.Product;
import com.inventory.product.repository.ProductRepository;
import com.inventory.product.response.PaginatedResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger logger= LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    //save the product
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDTO saveProduct(@Valid ProductRequestDTo dto){
//        Product product = modelMapper.map(dto,Product.class);
////        product.setName(dto.getName());;
////        product.setPrice(dto.getPrice());
////        product.setQuantity(dto.getQuantity());
//
//        return productRepository.save(product);

        Product product = productMapper.toEntity(dto);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDTO(savedProduct);
    }

    ////list of product
    @Cacheable(value = "product")
    public PaginatedResponse<ProductResponseDTO> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction,
            String name,
            Double minPrice,
            Double maxprice
    ){
        //adding the logger
        logger.info("Fetching product: page={},size={},sortBy={},direction={}",page,size,sortBy,direction);
        if(name!=null){
            logger.info("filtering by name:{}",name);
        }
        if (minPrice!=null && maxprice!=null){
            logger.info("Filtering by price between {} and {}",minPrice,maxprice);
        }
//        List<Product> products=productRepository.findAll();
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
        Sort sort=direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Product> productPage;
////        Filtering logic

        if(name!=null && minPrice!=null && maxprice!=null){
            productPage= productRepository.findByNameContainingIgnoreCase(name,pageable);
        } else if (name != null) {
            productPage=productRepository.findByNameContainingIgnoreCase(name,pageable);
        } else if (minPrice != null && maxprice != null) {
            productPage=productRepository.findByPriceBetween(minPrice,maxprice,pageable);
        }
        else {
            productPage = productRepository.findAllWithCategory(pageable);
        }

        List<ProductResponseDTO > dtoList=productPage.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        return new PaginatedResponse<>(
                dtoList,
                productPage.getNumber(),
                productPage.getTotalPages(),
                productPage.getTotalElements()
        );
    }

    //get the product by id
    public Product getProductByID(Long id){
        return productRepository.findById(id)
                .orElseThrow( ()-> new ProductNotFoundException("Product Not Found with id:"+ id));
    }

    //update the product
    @CacheEvict(value = "products", allEntries = true)
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

    /// delete the product
    @CacheEvict(value = "products", allEntries = true)
    public void  deleteProduct(Long id ){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id :"+ id));
        productRepository.delete(product);
    }

}
