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
public class OrderAdoptionListResponse {
    private List<OrderAdoptionResponse> orderAdoptionResponses;

    private int totalPages;
}
