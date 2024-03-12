package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.OrderAdoptionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-adoptions")
public class OrderAdoptionController {
    @GetMapping("")
    public ResponseEntity<?> getAllOrderAdoption() {
        return ResponseEntity.ok("get all order pet adoption");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderAdoptionById(@PathVariable Long id) {
        return ResponseEntity.ok("get order pet adoption with ID : " + id);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrderAdoptionByUserId(@PathVariable("user_id") Long user_id) {
        return ResponseEntity.ok("get order pet adoption with user ID : " + user_id);
    }

    @PostMapping("")
    public ResponseEntity<?> createOrderPetAdoption(@Valid @RequestBody OrderAdoptionDTO orderAdoptionDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("created order pet adoption successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderAdoption(@PathVariable Long id,
                                                 @Valid @RequestBody OrderAdoptionDTO orderAdoptionDTO,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("updated order pet adoption successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderAdoption(@PathVariable Long id){
        return ResponseEntity.ok("deleted order pet adoption with ID : " + id);
    }
}
