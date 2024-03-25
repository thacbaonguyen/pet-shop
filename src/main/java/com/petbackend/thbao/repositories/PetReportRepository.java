package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetReportRepository extends JpaRepository<PetReport, Long> {
    List<PetReport> findByUserId(Long userId);
    List<PetReport>  findByPetMissingId(Long petMissingId);
    List<PetReport>  findByPetMissingIdAndUserId(Long petMissingId, Long userId);
    Optional<PetReport> findByIdAndUserId(Long id, Long userId);
}
