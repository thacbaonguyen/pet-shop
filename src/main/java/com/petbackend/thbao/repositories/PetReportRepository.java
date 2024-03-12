package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetReportRepository extends JpaRepository<PetReport, Long> {
}
