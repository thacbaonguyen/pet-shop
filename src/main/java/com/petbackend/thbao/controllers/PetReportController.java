package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetReportDTO;
import com.petbackend.thbao.dtos.ReportImageDTO;
import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.models.ReportImage;
import com.petbackend.thbao.responses.PetReportListResponse;
import com.petbackend.thbao.responses.PetReportResponse;
import com.petbackend.thbao.responses.ReportImageResponse;
import com.petbackend.thbao.services.IPetReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/pet-reports")
@RequiredArgsConstructor
public class PetReportController {
    private final IPetReportService petReportService;
    @PostMapping("")
    public ResponseEntity<?> createPetReport(@Valid @RequestBody PetReportDTO petReportDTO, BindingResult result){

        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            PetReport petReport = petReportService.createPetReport(petReportDTO);
            return ResponseEntity.ok(PetReportResponse.fromPetReportResponse(petReport));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long petReportId,
                                          @ModelAttribute("files") List<MultipartFile> files){
        try {
            PetReport petReport = petReportService.getPetReportById(petReportId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > 5){
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("You can only upload a maximum of 5 files");
            }
            List<ReportImageResponse> reportImageResponses = new ArrayList<>();
            for (MultipartFile file : files){
                if (file.getSize() == 0){
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size limit exceeded");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported media type");
                }
                String fileName = storeFile(file);
                ReportImage reportImage = petReportService.createReportImage(petReport.getId(), ReportImageDTO.builder()
                        .url(fileName).build());
                reportImageResponses.add(ReportImageResponse.fromReportImageResponse(reportImage));
            }
            return ResponseEntity.ok(reportImageResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("upload-pet-report");
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
    }
    @GetMapping("")
    public ResponseEntity<?> getAllPetReports(@RequestParam("page") int page,
                                              @RequestParam("limit") int limit){
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
            Page<PetReportResponse> petReportResponses = petReportService.getAllPetReport(pageRequest);
            int total = petReportResponses.getTotalPages();
            List<PetReportResponse> list = petReportResponses.getContent();
            return ResponseEntity.ok(PetReportListResponse.builder().petReportResponses(list).totalPages(total).build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetReportById(@PathVariable Long id){
        try {
            PetReport petReport = petReportService.getPetReportById(id);
            return ResponseEntity.ok(PetReportResponse.fromPetReportResponse(petReport));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/pets/{id}")
    public ResponseEntity<?> getPetReportByPetMissingId(@PathVariable("id") Long missingID){
        try{
            List<PetReport> petReport = petReportService.getPetReportByPetMissingId(missingID);
            List<PetReportResponse> petReportResponseList = new ArrayList<>();
            for(PetReport result : petReport){
                petReportResponseList.add(PetReportResponse.fromPetReportResponse(result));
            }
            return ResponseEntity.ok(petReportResponseList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getPetReportByUserId(@PathVariable("id") Long userId){
        try{
            List<PetReport> petReport = petReportService.getPetReportByUserId(userId);
            List<PetReportResponse> petReportResponseList = new ArrayList<>();
            for (PetReport result : petReport){
                petReportResponseList.add(PetReportResponse.fromPetReportResponse(result));
            }
            return ResponseEntity.ok(petReportResponseList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetReport(@PathVariable Long id, @Valid @RequestBody PetReportDTO petReportDTO,
                                             BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            PetReport petReport = petReportService.updatePetReport(id, petReportDTO);
            return ResponseEntity.ok(PetReportResponse.fromPetReportResponse(petReport));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetReport(@PathVariable Long id){
        try {
            petReportService.delete(id);
            return ResponseEntity.ok("Delete pet report successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
