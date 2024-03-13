package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.ProductDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.Product;
import com.petbackend.thbao.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    @Override
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
    public void delete(Long id) throws DataNotFoundException {
        Product existProduct = productRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this product"));
        productRepository.delete(existProduct);
    }
}