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
public class AdoptionImageDTO {
    String url;

    @JsonProperty("adoption_id")
    Long petAdoptionId;
}
