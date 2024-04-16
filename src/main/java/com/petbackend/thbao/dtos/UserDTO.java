package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petbackend.thbao.annotation.BirthDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotBlank(message = "Full name is required")
    @JsonProperty("fullname")
    String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, max = 13, message = "The phone number must have at least 5 digits and not exceed 13 digits")
    String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    String address;

    @NotBlank(message = "Password cannot be empty")
    String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "Password cannot be empty")
    String retypePassword;

    @NotBlank(message = "Email cannot be empty")
    String email;

    @JsonProperty("date_of_birth")
    @BirthDate
    @NotNull(message = "Date of birth cannot be empty")
    LocalDate dateOfBirth;

    @NotNull(message = "Role ID cannot be empty")
    @JsonProperty("role_id")
    Long roleId;
}
