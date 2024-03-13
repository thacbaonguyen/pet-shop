package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingImageDTO {
    private String url;

    @JsonProperty("missing_id")
    private Long missingId;
}
