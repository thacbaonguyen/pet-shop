package com.petbackend.thbao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MissingImageDTO {
    String url;

    @JsonProperty("missing_id")
    Long missingId;
}
