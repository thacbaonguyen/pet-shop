package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAdoptionDTO {
    @JsonProperty("fullname")
    @NotEmpty(message = "Full name cannot be empty")
    String fullName;

    @JsonProperty("phone_number")
    @NotEmpty(message = "Phone number cannot be empty")
    String phoneNumber;

    @NotNull(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    LocalDate dateOfBirth;

    @NotEmpty(message = "Address cannot be empty")
    String address;

    String email;

    @JsonProperty("pet_adoption_id")
    @NotNull(message = "Pet adoption ID cannot be null")
    Long petAdoptionId;

    @JsonProperty("user_id")
    @NotNull(message = "User ID cannot be empty")
    Long userId;

}
