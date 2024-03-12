package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.PetMissing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetMissingListResponse {
    private List<PetMissingResponse> petMissingResponses;
    private int totalPages;
}
