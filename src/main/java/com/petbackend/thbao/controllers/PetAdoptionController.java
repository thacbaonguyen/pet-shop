package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetAdoptionDTO;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.responses.PetAdoptionListResponse;
import com.petbackend.thbao.responses.PetAdoptionResponse;
import com.petbackend.thbao.services.IPetAdoptionService;
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
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/pet-adoptions")
public class PetAdoptionController {
    private final IPetAdoptionService petAdoptionService;
    @PostMapping("")
    public ResponseEntity<?> createPetAdoption(@Valid @RequestBody PetAdoptionDTO petAdoptionDTO, BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            PetAdoption petAdoption = petAdoptionService.createPetAdoption(petAdoptionDTO);
            return ResponseEntity.ok(PetAdoptionResponse.fromPetAdoptionResponse(petAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("")
    public ResponseEntity<?> getAllPetAdoptions(@RequestParam("page") int page,
                                                @RequestParam("limit") int limit){
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
            Page<PetAdoptionResponse> petAdoptionResponse = petAdoptionService.getAllPetAdoption(pageRequest);
            int total = petAdoptionResponse.getTotalPages();
            List<PetAdoptionResponse> list = petAdoptionResponse.getContent();
            return ResponseEntity.ok(PetAdoptionListResponse.builder().petAdoptionResponses(list).totalPages(total).build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetAdoptionById(@PathVariable Long id){
        try{
            PetAdoption petAdoption = petAdoptionService.getPetAdoptionById(id);
            return ResponseEntity.ok(PetAdoptionResponse.fromPetAdoptionResponse(petAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getPetAdoptionByCategoryId(@PathVariable("id") Long categoryId){
        try {
            List<PetAdoption> petAdoption = petAdoptionService.getPetAdoptionByCategoryId(categoryId);
            List<PetAdoptionResponse> petAdoptionResponseList = new ArrayList<>();
            for(PetAdoption result : petAdoption){
                petAdoptionResponseList.add(PetAdoptionResponse.fromPetAdoptionResponse(result));
            }
            return ResponseEntity.ok(petAdoptionResponseList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getPetAdoptionByUserId(@PathVariable("id") Long userId){
        try {
            List<PetAdoption> petAdoption = petAdoptionService.getPetAdoptionByUserId(userId);
            List<PetAdoptionResponse> petAdoptionResponseList = new ArrayList<>();
            for(PetAdoption result : petAdoption){
                petAdoptionResponseList.add(PetAdoptionResponse.fromPetAdoptionResponse(result));
            }
            return ResponseEntity.ok(petAdoptionResponseList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetAdoption(@PathVariable Long id, @Valid @RequestBody PetAdoptionDTO petAdoptionDTO,
                                               BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            PetAdoption petAdoption = petAdoptionService.updatePetAdoption(id, petAdoptionDTO);
            return ResponseEntity.ok(PetAdoptionResponse.fromPetAdoptionResponse(petAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetAdoption(@PathVariable Long id){
        try {
            petAdoptionService.delete(id);
            return ResponseEntity.ok("Delete Pet adoption successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
