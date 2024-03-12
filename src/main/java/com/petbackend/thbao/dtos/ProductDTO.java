package com.petbackend.thbao.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3, max = 200, message = "Product name must be greater than 3 and less than 200 characters")
    private String name;

    @NotNull(message = "Product price cannot be null")
    @Min(value = 0, message = "Product price must be greater than 0")
    @Max(value = 1000000000, message = "Product price must be less than 1000000000")
    private Float price;

    private String description;

    @NotEmpty(message = "Product image cannot be empty")
    private String image;
}
