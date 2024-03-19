package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    @JsonProperty("url")
    @Size(min = 5, max = 200, message = "Images url must be greater than 5 characters and less than 200 characters")
    private String imageUrl;
}
