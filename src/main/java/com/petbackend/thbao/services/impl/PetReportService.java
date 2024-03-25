package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.PetReportDTO;
import com.petbackend.thbao.dtos.ReportImageDTO;
import com.petbackend.thbao.exceptions.AccessDeniedException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // Chỉ tạo được đơn báo cáo cho bản thân
        if(!user.get().getPhoneNumber().equals(name)){
            throw new AccessDeniedException("You cannot create an report for someone else");
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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PetReportResponse> getAllPetReport(PageRequest pageRequest) {
        return petReportRepository.findAll(pageRequest).map(PetReportResponse::fromPetReportResponse);
    }

    @Override
    public PetReport getPetReportById(Long id) throws DataNotFoundException {
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User existUser = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        PetReport petReport = petReportRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Pet report not exist"));
        PetMissing petMissing = petMissingRepository.findById(petReport.getPetMissing().getId()).orElseThrow(() ->
                new DataNotFoundException("Pet missing not exist"));
        // User bi mat thu cung (missing)/
        User userByMissing = userRepository.findById(petMissing.getUser().getId()).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        // User dang bai report
        User userByReport = userRepository.findById(petReport.getUser().getId()).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (!role.contains("ROLE_ADMIN") && !userByMissing.getPhoneNumber().equals(phoneNumber) &&
                !userByReport.getPhoneNumber().equals(phoneNumber)){
            throw new AccessDeniedException("Denied this report");
        }
        return petReportRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet report"));
    }

    @Override
    public List<PetReport> getPetReportByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->
                new DataNotFoundException("Cannot found user"));
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User existUser = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (userId != existUser.getId() && !role.contains("ROLE_ADMIN")){
            // Nếu role user lấy bao cao từ id người khác thì bị lỗi
            throw new AccessDeniedException("You cannot access orders from other people");
        }
        List<PetReport> list = petReportRepository.findByUserId(userId);
        return list;
    }

    @Override
    public List<PetReport> getPetReportByPetMissingId(Long missingId) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(missingId).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        User userByMissing = userRepository.findById(petMissing.getUser().getId()).orElseThrow(()->
                new DataNotFoundException("User not exist"));
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!role.contains("ROLE_ADMIN") && !userByMissing.getPhoneNumber().equals(phoneNumber)){
            throw new AccessDeniedException("You denied this report");
        }
        return petReportRepository.findByPetMissingId(missingId);
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
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!petReport.getUser().getPhoneNumber().equals(phoneNumber)){
            // chỉ update được bao cao của bản thân
            throw new AccessDeniedException("You cannot update an missing for someone else");
        }
        petReport.setUser(user);
        petReport.setPetMissing(petMissing);
        petReportRepository.save(petReport);
        return petReport;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetReport petReport = petReportRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet report"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petReport.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot delete for someone else missing");
        }
        petReportRepository.delete(petReport);
    }

    @Override
    public ReportImage createReportImage(Long petReportId, ReportImageDTO reportImageDTO) throws DataNotFoundException {
        PetReport petReport = petReportRepository.findById(petReportId).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet report"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petReport.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot import image for someone else missing");
        }
        ReportImage reportImage = ReportImage.builder()
                .petReport(petReport)
                .url(reportImageDTO.getUrl()).build();
        reportImageRepository.save(reportImage);
        return reportImage;
    }
}
