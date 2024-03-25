package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.ProductDTO;
import com.petbackend.thbao.dtos.ProductImageDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.Product;
import com.petbackend.thbao.models.ProductImage;
import com.petbackend.thbao.repositories.ProductImageRepository;
import com.petbackend.thbao.repositories.ProductRepository;
import com.petbackend.thbao.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription()).build();
        productRepository.save(product);
        return product;
    }

    @Override
    public Page<Product> getAllProduct(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }
    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Cannot found this product"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existProduct = productRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this product"));
        existProduct.setName(productDTO.getName());
        existProduct.setPrice(productDTO.getPrice());
        existProduct.setDescription(productDTO.getDescription());
        productRepository.save(existProduct);
        return existProduct;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) throws DataNotFoundException {
        Product existProduct = productRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this product"));
        productRepository.delete(existProduct);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException {
        Product existProduct = productRepository.findById(productId).orElseThrow(()->
                new DataNotFoundException("Cannot found this product"));
        ProductImage productImage = ProductImage.builder()
                .product(existProduct)
                .imageUrl(productImageDTO.getImageUrl()).build();
        productImageRepository.save(productImage);
        return productImage;
    }

}
