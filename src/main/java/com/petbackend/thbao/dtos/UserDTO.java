package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @NotBlank(message = "Full name is required")
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, max = 13, message = "The phone number must have at least 5 digits and not exceed 13 digits")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "Password cannot be empty")
    private String retypePassword;

    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Role ID cannot be empty")
    @JsonProperty("role_id")
    private Long roleId;
}
