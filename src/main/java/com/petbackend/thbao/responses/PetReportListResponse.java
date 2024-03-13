package com.petbackend.thbao.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetReportListResponse {
    private List<PetReportResponse> petReportResponses;

    private int totalPages;
}
