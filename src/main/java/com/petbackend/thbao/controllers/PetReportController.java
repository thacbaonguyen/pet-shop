package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetReportDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/pet-reports")
public class PetReportController {
    @GetMapping("")
    public ResponseEntity<?> getAllPetReports(){
        return ResponseEntity.ok("get all pet reports");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetReportById(@PathVariable Long id){
        return ResponseEntity.ok("get pet report by id : " + id);
    }
    @GetMapping("/pets/{pet_id}")
    public ResponseEntity<?> getPetReportByPetId(@PathVariable("pet_id") Long pet_id){
        return ResponseEntity.ok("get pet report by pet id : "+ pet_id);
    }
    @PostMapping("")
    public ResponseEntity<?> createPetReport(@Valid @RequestBody PetReportDTO petReportDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("create pet report successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetReport(@PathVariable Long id, @Valid @RequestBody PetReportDTO petReportDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("update pet report with ID: "+ id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetReport(@PathVariable Long id){
        return ResponseEntity.ok("deleted pet report successfully with id : " + id);
    }

}
