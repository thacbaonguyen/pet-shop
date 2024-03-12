package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetMissing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetMissingRepository extends JpaRepository<PetMissing, Long> {
    PetMissing findByUserId(Long userId);
}
