package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetAdoptionRepository extends JpaRepository<PetAdoption, Long> {
    List<PetAdoption>  findByUserId(Long userId);
    List<PetAdoption> findByCategoryId(Long categoryId);
}
