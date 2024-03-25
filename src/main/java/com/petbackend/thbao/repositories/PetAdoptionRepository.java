package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetAdoptionRepository extends JpaRepository<PetAdoption, Long> {
    List<PetAdoption>  findByUserId(Long userId);
    List<PetAdoption> findByCategoryId(Long categoryId);

    List<PetAdoption> findByCategoryIdAndUserId(Long categoryId, Long userId);
    Optional<PetAdoption> findByIdAndUserId(Long id, Long userId);
}
