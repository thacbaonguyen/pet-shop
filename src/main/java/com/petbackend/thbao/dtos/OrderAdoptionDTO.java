package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

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

    @NotNull(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

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
