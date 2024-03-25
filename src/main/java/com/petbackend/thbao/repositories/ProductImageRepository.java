package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);

}
