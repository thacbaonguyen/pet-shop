package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PetReportDTO {

    @JsonProperty("phone_number")
    @NotEmpty(message = "Phone number cannot be empty")
    String phoneNumber;

    @NotEmpty(message = "Address found cannot be empty")
    @JsonProperty("address_found")
    String addressFound;

    @NotNull(message = "Time found cannot be null")
    @JsonProperty("time_found")
    LocalDate timeFound;

    String health;

    String description;

    @JsonProperty("petmissing_id")
    @NotNull(message = "Pet ID cannot be empty")
    Long petMissingId;

    @NotNull(message = "User ID cannot be empty")
    @JsonProperty("user_id")
    Long userId;
}
