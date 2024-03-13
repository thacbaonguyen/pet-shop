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
public class PetAdoptionListResponse {
    private List<PetAdoptionResponse> petAdoptionResponses;

    private int totalPages;
}
