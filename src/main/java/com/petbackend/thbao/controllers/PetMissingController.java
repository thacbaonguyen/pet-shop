package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetMissingDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.responses.PetMissingListResponse;
import com.petbackend.thbao.responses.PetMissingResponse;
import com.petbackend.thbao.services.IPetMissingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/pets")
public class PetMissingController {
    private final IPetMissingService petMissingService;
    @GetMapping("")
    public ResponseEntity<?> getAllPetMissing(@RequestParam("page") int page,
                                       @RequestParam("limit") int limit){

        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
            Page<PetMissingResponse> petMissingResponses = petMissingService.getAllPetMissing(pageRequest);
            int totalPages = petMissingResponses.getTotalPages();
            List<PetMissingResponse> list = petMissingResponses.getContent();
            return ResponseEntity.ok(PetMissingListResponse.builder().petMissingResponses(list).totalPages(totalPages).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetMissingById(@PathVariable Long id){
        PetMissing petMissing = petMissingService.getPetMissingById(id);
        return ResponseEntity.ok(PetMissingResponse.fromPetMissingResponse(petMissing));
    }
    @PostMapping("")
    public ResponseEntity<?> createPetMissing(@Valid @RequestBody PetMissingDTO petMissingDTO, BindingResult result) {
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            PetMissing petMissing = petMissingService.createPetMissing(petMissingDTO);
            return ResponseEntity.ok(PetMissingResponse.fromPetMissingResponse(petMissing));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePet(@PathVariable Long id, @Valid @RequestBody PetMissingDTO petMissingDTO){
        try {
            PetMissing petMissing = petMissingService.updatePetMissing(id, petMissingDTO);
            return ResponseEntity.ok(PetMissingResponse.fromPetMissingResponse(petMissing));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id){
        try {
            petMissingService.delete(id);
            return ResponseEntity.ok("delete Pet missing successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
