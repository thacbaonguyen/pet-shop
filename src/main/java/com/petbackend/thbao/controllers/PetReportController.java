package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetReportDTO;
import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.responses.PetReportListResponse;
import com.petbackend.thbao.responses.PetReportResponse;
import com.petbackend.thbao.services.IPetReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/pet-reports")
@RequiredArgsConstructor
public class PetReportController {
    private final IPetReportService petReportService;
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
            return ResponseEntity.ok("deleted pet report successfully with id : " + id);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
