package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.AdoptionImageDTO;
import com.petbackend.thbao.dtos.PetAdoptionDTO;
import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.responses.AdoptionImageResponse;
import com.petbackend.thbao.responses.PetAdoptionListResponse;
import com.petbackend.thbao.responses.PetAdoptionResponse;
import com.petbackend.thbao.services.IPetAdoptionService;
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
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long petAdoptionId,
                                          @ModelAttribute("files") List<MultipartFile> files){
        try{
            PetAdoption petAdoption = petAdoptionService.getPetAdoptionById(petAdoptionId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > 5){
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("You can only upload a maximum of 5 files");
            }
            List<AdoptionImageResponse> adoptionImageResponses = new ArrayList<>();
            for (MultipartFile file : files){
                if (file.getSize() == 0){
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size limit exceeded");
                }
                //dùng để lấy kiểu MIME (còn được gọi là kiểu nội dung) -> image/jpg, image/png
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported media type");
                }
                String fileName = storeFile(file);
                AdoptionImage adoptionImage = petAdoptionService.createAdoptionImage(petAdoption.getId(), AdoptionImageDTO.builder()
                        .url(fileName).build());
                adoptionImageResponses.add(AdoptionImageResponse.fromAdoptionImageResponse(adoptionImage));
            }
            return ResponseEntity.ok(adoptionImageResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("upload-pet-adoptions");
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueName);
        // sao chép toàn bộ nội dung của file được biểu thị bởi file sang vị trí được xác định bởi destination
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
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
