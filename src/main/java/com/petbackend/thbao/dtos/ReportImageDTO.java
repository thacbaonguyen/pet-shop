package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportImageDTO {
    private String url;

    @JsonProperty("report_id")
    private Long petReportId;
}
