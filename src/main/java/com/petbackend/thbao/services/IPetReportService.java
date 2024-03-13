package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.PetReportDTO;
import com.petbackend.thbao.dtos.ReportImageDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.models.ReportImage;
import com.petbackend.thbao.responses.PetReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPetReportService {
    PetReport createPetReport(PetReportDTO petReportDTO) throws DataNotFoundException;

    Page<PetReportResponse> getAllPetReport(PageRequest pageRequest);

    PetReport getPetReportById(Long id) throws DataNotFoundException;

    List<PetReport> getPetReportByUserId(Long userId) throws DataNotFoundException;

    List<PetReport> getPetReportByPetMissingId(Long missingId) throws DataNotFoundException;

    PetReport updatePetReport(Long id, PetReportDTO petReportDTO) throws DataNotFoundException;

    void delete(Long id) throws DataNotFoundException;

    ReportImage createReportImage(Long petReportId, ReportImageDTO reportImageDTO) throws DataNotFoundException;
}
