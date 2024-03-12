package com.petbackend.thbao.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingImageDTO {
    private String url;

    private Long missingId;
}
