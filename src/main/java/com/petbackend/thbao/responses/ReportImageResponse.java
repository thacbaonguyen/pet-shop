package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.PetReport;
import com.petbackend.thbao.models.ReportImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportImageResponse {
    private Long id;

    private String url;

    private PetReportResponse petReportResponse;

    public static final ReportImageResponse fromReportImageResponse(ReportImage reportImage){
        return ReportImageResponse.builder()
                .id(reportImage.getId())
                .url(reportImage.getUrl())
                .petReportResponse(PetReportResponse.fromPetReportResponse(reportImage.getPetReport())).build();
    }
}
