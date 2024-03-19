package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotEmpty(message = "Phone number cannot be empty")
    String phoneNumber;

    @NotEmpty(message = "Password cannot be empty")
    String password;
}
