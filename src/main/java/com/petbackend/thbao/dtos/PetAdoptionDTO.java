package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetAdoptionDTO {
    @NotEmpty(message = "Pet name cannot be empty")
    String name;

    String color;

    @NotEmpty(message = "Pet age cannot be empty")
    String age;

    String sex;

    @NotEmpty(message = "Health cannot be empty")
    String health;

    String description;

    @JsonProperty("is_active")
    boolean isActive;

    @NotEmpty(message = "Phone number cannot be empty")
    @JsonProperty("phone_number")
    String phoneNumber;

    @NotEmpty(message = "Address cannot be empty")
    String address;

    @NotNull(message = "Category ID cannot be null")
    @JsonProperty("category_id")
    Long categoryId;

    @NotNull(message = "User Id cannot be null")
    @JsonProperty("user_id")
    Long userId;
}
