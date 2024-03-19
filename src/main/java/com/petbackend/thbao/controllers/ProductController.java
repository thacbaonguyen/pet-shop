package com.petbackend.thbao.controllers;

import com.petbackend.thbao.dtos.ProductDTO;
import com.petbackend.thbao.dtos.ProductImageDTO;
import com.petbackend.thbao.models.Product;
import com.petbackend.thbao.models.ProductImage;
import com.petbackend.thbao.services.IProductService;
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
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    @PostMapping("")
    public ResponseEntity<?> createProducts(@Valid @RequestBody ProductDTO productDTO, BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.ok(product);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files){
        try {
            Product existProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > 5) {
                return ResponseEntity.badRequest().body("You can only upload a maximum of 5 files");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files){
                if (file.getSize() == 0) {
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
                ProductImage productImage = productService.createProductImage(existProduct.getId(),
                        ProductImageDTO.builder()
                        .imageUrl(fileName).build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam("page") int page,
                                            @RequestParam("limit") int limit){
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
            Page<Product> products = productService.getAllProduct(pageRequest);
            int total = products.getTotalPages();
            List<Product> result = products.getContent();
            return ResponseEntity.ok(result);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        try{
            productService.delete(id);
            return ResponseEntity.ok("Delete product successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
