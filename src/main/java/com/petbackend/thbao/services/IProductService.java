package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.ProductDTO;
import com.petbackend.thbao.dtos.ProductImageDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.Product;
import com.petbackend.thbao.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    Page<Product> getAllProduct(PageRequest pageRequest);
    Product getProductById(Long id) throws DataNotFoundException;
    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;
    void delete(Long id) throws DataNotFoundException;
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException;
}
