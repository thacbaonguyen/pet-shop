package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.PetAdoptionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/pet-adoptions")
public class PetAdoptionController {
    @GetMapping("")
    public ResponseEntity<?> getAllPetAdoptions(){
        return ResponseEntity.ok("get all pet adoption");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetAdoptionById(@PathVariable Long id){
        return ResponseEntity.ok("get pet adoption with id : " + id);
    }
    @GetMapping("/category/{category_id}")
    public ResponseEntity<?> getPetAdoptionByCategoryId(@PathVariable("category_id") Long category_id){
        return ResponseEntity.ok("get pet adoption by category id : " + category_id);
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getPetAdoptionByUserId(@PathVariable("user_id") Long user_id){
        return ResponseEntity.ok("get pet adoption by user id : " + user_id);
    }
    @PostMapping("")
    public ResponseEntity<?> createPetAdoption(@Valid @RequestBody PetAdoptionDTO petAdoptionDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("created pet adoption");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetAdoption(@PathVariable Long id, @Valid @RequestBody PetAdoptionDTO petAdoptionDTO,
                                               BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("updated pet adoption with id: " + id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetAdoption(@PathVariable Long id){
        return ResponseEntity.ok("deleted pet adoption with id : " +id);
    }
}
