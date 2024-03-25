package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetReportResponse {
    private Long id;
    private String phoneNumber;

    private String addressFound;

    private LocalDate timeFound;

    private String health;

    private String description;

    private Long userId;

    private Long petMissingId;

    public static final PetReportResponse fromPetReportResponse(PetReport petReport){
        return PetReportResponse.builder()
                .id(petReport.getId())
                .phoneNumber(petReport.getPhoneNumber())
                .addressFound(petReport.getAddressFound())
                .timeFound(petReport.getTimeFound())
                .health(petReport.getHealth())
                .description(petReport.getDescription())
                .userId(petReport.getUser().getId())
                .petMissingId(petReport.getPetMissing().getId()).build();
    }
}
