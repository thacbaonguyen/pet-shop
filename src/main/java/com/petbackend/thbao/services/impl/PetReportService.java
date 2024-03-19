package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.PetReportDTO;
import com.petbackend.thbao.dtos.ReportImageDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.models.ReportImage;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.PetMissingRepository;
import com.petbackend.thbao.repositories.PetReportRepository;
import com.petbackend.thbao.repositories.ReportImageRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.PetReportResponse;
import com.petbackend.thbao.services.IPetReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PetReportService implements IPetReportService {
    private final UserRepository userRepository;
    private final PetMissingRepository petMissingRepository;
    private final PetReportRepository petReportRepository;
    private final ReportImageRepository reportImageRepository;
    @Override
    public PetReport createPetReport(PetReportDTO petReportDTO) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(petReportDTO.getUserId());
        Optional<PetMissing> petMissing = petMissingRepository.findById(petReportDTO.getPetMissingId());
        if(!user.isPresent()){
            throw new DataNotFoundException("Cannot found user");
        }
        if(!petMissing.isPresent()){
            throw new DataNotFoundException("Cannot found Pet missing");
        }
        PetReport petReport = PetReport.builder()
                .phoneNumber(petReportDTO.getPhoneNumber())
                .addressFound(petReportDTO.getAddressFound())
                .timeFound(petReportDTO.getTimeFound())
                .health(petReportDTO.getHealth())
                .description(petReportDTO.getDescription())
                .user(user.get())
                .petMissing(petMissing.get()).build();
        petReportRepository.save(petReport);
        if (petMissing.get().getUser().getId() == petReportDTO.getUserId()){
            throw new DataNotFoundException("You cannot report your pet");
        }
        return petReport;
    }

    @Override
    public Page<PetReportResponse> getAllPetReport(PageRequest pageRequest) {
        return petReportRepository.findAll(pageRequest).map(PetReportResponse::fromPetReportResponse);
    }

    @Override
    public PetReport getPetReportById(Long id) throws DataNotFoundException {
        return petReportRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet report"));
    }

    @Override
    public List<PetReport> getPetReportByUserId(Long userId) throws DataNotFoundException {
        User existsUser = userRepository.findById(userId).orElseThrow(()->
                new DataNotFoundException("Cannot found user"));
        List<PetReport> list = petReportRepository.findByUserId(userId);
        return list;
    }

    @Override
    public List<PetReport> getPetReportByPetMissingId(Long missingId) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(missingId).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        List<PetReport> list = petReportRepository.findByPetMissingId(missingId);
        return list;
    }

    @Override
    public PetReport updatePetReport(Long id, PetReportDTO petReportDTO) throws DataNotFoundException {
        PetReport petReport = petReportRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet report"));
        petReport.setPhoneNumber(petReportDTO.getPhoneNumber());
        petReport.setAddressFound(petReportDTO.getAddressFound());
        petReport.setTimeFound(petReportDTO.getTimeFound());
        petReport.setHealth(petReportDTO.getHealth());
        petReport.setDescription(petReportDTO.getDescription());
        User user = userRepository.findById(petReportDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this user"));
        PetMissing petMissing = petMissingRepository.findById(petReportDTO.getPetMissingId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet missing"));
        petReport.setUser(user);
        petReport.setPetMissing(petMissing);
        petReportRepository.save(petReport);
        return petReport;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetReport petReport = petReportRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet report"));
        petReportRepository.delete(petReport);
    }

    @Override
    public ReportImage createReportImage(Long petReportId, ReportImageDTO reportImageDTO) throws DataNotFoundException {
        PetReport petReport = petReportRepository.findById(petReportId).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet report"));
        ReportImage reportImage = ReportImage.builder()
                .petReport(petReport)
                .url(reportImageDTO.getUrl()).build();
        reportImageRepository.save(reportImage);
        return reportImage;
    }
}
