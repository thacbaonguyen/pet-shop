package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.OrderAdoptionDTO;
import com.petbackend.thbao.models.OrderAdoption;
import com.petbackend.thbao.responses.OrderAdoptionListResponse;
import com.petbackend.thbao.responses.OrderAdoptionResponse;
import com.petbackend.thbao.services.IOrderAdoptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.EdECKey;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order-adoptions")
public class OrderAdoptionController {
    private final IOrderAdoptionService orderAdoptionService;
    @PostMapping("")
    public ResponseEntity<?> createOrderPetAdoption(@Valid @RequestBody OrderAdoptionDTO orderAdoptionDTO,
                                                    BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderAdoption orderAdoption = orderAdoptionService.createOrderAdoption(orderAdoptionDTO);
            return ResponseEntity.ok(OrderAdoptionResponse.fromOrderAdoptionResponse(orderAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("")
    public ResponseEntity<?> getAllOrderAdoption(@RequestParam("page") int page,
                                                 @RequestParam("limit") int limit) {
        try{
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
            Page<OrderAdoptionResponse> orderAdoptionResponses = orderAdoptionService.getAllOrderAdoption(pageRequest);
            int total = orderAdoptionResponses.getTotalPages();
            List<OrderAdoptionResponse> list = orderAdoptionResponses.getContent();
            return ResponseEntity.ok(OrderAdoptionListResponse.builder().orderAdoptionResponses(list).totalPages(total).build());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderAdoptionById(@PathVariable Long id) {
        try {
            OrderAdoption orderAdoption = orderAdoptionService.getOrderAdoptionById(id);
            return ResponseEntity.ok(OrderAdoptionResponse.fromOrderAdoptionResponse(orderAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOrderAdoptionByUserId(@PathVariable("id") Long userId) {
        try{
            List<OrderAdoption> orderAdoptionList = orderAdoptionService.getOrderAdoptionByUserId(userId);
            List<OrderAdoptionResponse> list = new ArrayList<>();
            for (OrderAdoption result : orderAdoptionList){
                list.add(OrderAdoptionResponse.fromOrderAdoptionResponse(result));
            }
            return ResponseEntity.ok(list);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/pet-adoption/{id}")
    public ResponseEntity<?> getOrderAdoptionByPetAdoptionId(@PathVariable("id") Long petAdoptionId) {
        try {
            List<OrderAdoption> orderAdoptionList = orderAdoptionService.getOrderAdoptionByPetAdoptionId(petAdoptionId);
            List<OrderAdoptionResponse> list = new ArrayList<>();
            for (OrderAdoption result : orderAdoptionList){
                list.add(OrderAdoptionResponse.fromOrderAdoptionResponse(result));
            }
            return ResponseEntity.ok(list);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderAdoption(@PathVariable Long id,
                                                 @Valid @RequestBody OrderAdoptionDTO orderAdoptionDTO,
                                                 BindingResult result) {
        try{
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderAdoption orderAdoption = orderAdoptionService.updateOrderAdoption(id, orderAdoptionDTO);
            return ResponseEntity.ok(OrderAdoptionResponse.fromOrderAdoptionResponse(orderAdoption));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderAdoption(@PathVariable Long id){
        try{
            orderAdoptionService.delete(id);
            return ResponseEntity.ok("Delete order adoption successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
