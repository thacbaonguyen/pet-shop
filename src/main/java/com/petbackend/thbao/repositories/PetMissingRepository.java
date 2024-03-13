package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetMissing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetMissingRepository extends JpaRepository<PetMissing, Long> {
    List<PetMissing> findByUserId(Long userId);
    List<PetMissing> findByCategoryId(Long categoryId);
}
