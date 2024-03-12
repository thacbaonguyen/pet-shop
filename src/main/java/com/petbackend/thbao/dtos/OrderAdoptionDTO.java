package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAdoptionDTO {
    @JsonProperty("fullname")
    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;

    @JsonProperty("phone_number")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 18, message = "Age must be greater than 18")
    @Max(value = 50, message = "Age must be less than 50")
    private Long age;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    private String email;

    @JsonProperty("pet_adoption_id")
    @NotNull(message = "Pet adoption ID cannot be null")
    private Long petAdoptionId;

    @JsonProperty("user_id")
    @NotNull(message = "User ID cannot be empty")
    private Long userId;

}
