package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.MissingImageDTO;
import com.petbackend.thbao.dtos.PetMissingDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.MissingImage;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.responses.MissingImageResponse;
import com.petbackend.thbao.responses.PetMissingListResponse;
import com.petbackend.thbao.responses.PetMissingResponse;
import com.petbackend.thbao.services.IPetMissingService;
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
@RequestMapping("${api.prefix}/pets")
public class PetMissingController {
    private final IPetMissingService petMissingService;
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
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long petMissingId,
                                          @ModelAttribute("files")List<MultipartFile> files){
        try {
            PetMissing petMissing = petMissingService.getPetMissingById(petMissingId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > 5){
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("You can only upload a maximum of 5 files");
            }
            List<MissingImageResponse> missingImageResponses = new ArrayList<>();
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
                MissingImage missingImage = petMissingService.createMissingImage(petMissing.getId(), MissingImageDTO.builder()
                        .url(fileName).build());
                missingImageResponses.add(MissingImageResponse.fromMissingImageResponse(missingImage));
            }
            return ResponseEntity.ok(missingImageResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("upload-pet-missing");
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
    }
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
    public ResponseEntity<?> getPetMissingById(@PathVariable Long id) throws DataNotFoundException {
        PetMissing petMissing = petMissingService.getPetMissingById(id);
        return ResponseEntity.ok(PetMissingResponse.fromPetMissingResponse(petMissing));
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getPetMissingByUserId(@PathVariable("id") Long userId){
        try {
            List<PetMissing> petMissing = petMissingService.getPetMissingByUserId(userId);
            List<PetMissingResponse> petMissingResponseList = new ArrayList<>();
            for(PetMissing result : petMissing){
                petMissingResponseList.add(PetMissingResponse.fromPetMissingResponse(result));
            }
            return ResponseEntity.ok(petMissingResponseList);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getPetMissingByCategoryId(@PathVariable("id") Long categoryId){
        try {
            List<PetMissing> petMissing = petMissingService.getPetMissingByCategoryId(categoryId);
            List<PetMissingResponse> petMissingResponseList = new ArrayList<>();
            for(PetMissing result : petMissing){
                petMissingResponseList.add(PetMissingResponse.fromPetMissingResponse(result));
            }
            return ResponseEntity.ok(petMissingResponseList);
        }
        catch (Exception e){
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
