package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetMissingDTO {
    private String name;

    @NotEmpty(message = "Color cannot be empty")
    private String color;

    private String age;

    private String sex;

    private String description;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Missing time cannot be empty")
    @JsonProperty("missing_time")
    private LocalDate missingTime;

    @NotNull(message = "Category ID cannot be empty")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull(message = "User ID cannot be empty")
    @JsonProperty("user_id")
    private Long userId;
}
