package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetAdoptionDTO {
    @NotEmpty(message = "Pet name cannot be empty")
    private String name;

    private String color;

    @NotEmpty(message = "Pet age cannot be empty")
    private String age;

    private String sex;

    @NotEmpty(message = "Health cannot be empty")
    private String health;

    private String description;

    @NotEmpty(message = "Phone number cannot be empty")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Category ID cannot be null")
    @JsonProperty("category_id")
    private Long categoryId;

    @NotNull(message = "User Id cannot be null")
    @JsonProperty("user_id")
    private Long userId;
}
