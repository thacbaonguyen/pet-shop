package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionImageDTO {
    private String url;

    @JsonProperty("adoption_id")
    private Long petAdoptionId;
}
