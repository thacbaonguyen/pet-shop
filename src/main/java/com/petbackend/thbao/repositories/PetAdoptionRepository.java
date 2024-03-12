package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetAdoptionRepository extends JpaRepository<PetAdoption, Long> {
}
