package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetReportDTO {

    @JsonProperty("phone_number")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotEmpty(message = "Address found cannot be empty")
    @JsonProperty("address_found")
    private String addressFound;

    @NotNull(message = "Time found cannot be null")
    @JsonProperty("time_found")
    private LocalDate timeFound;

    private String health;

    private String description;

    @JsonProperty("petmissing_id")
    @NotNull(message = "Pet ID cannot be empty")
    private Long petMissingId;

    @NotNull(message = "User ID cannot be empty")
    @JsonProperty("user_id")
    private Long userId;
}
