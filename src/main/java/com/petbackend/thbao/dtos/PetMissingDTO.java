package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetMissingDTO {
    String name;

    @NotEmpty(message = "Color cannot be empty")
    String color;

    String age;

    String sex;

    String description;

    @NotEmpty(message = "Status cannot be empty")
    String status;

    @NotNull(message = "Missing time cannot be empty")
    @JsonProperty("missing_time")
    LocalDate missingTime;

    @NotNull(message = "Category ID cannot be empty")
    @JsonProperty("category_id")
    Long categoryId;

    @NotNull(message = "User ID cannot be empty")
    @JsonProperty("user_id")
    Long userId;
}
