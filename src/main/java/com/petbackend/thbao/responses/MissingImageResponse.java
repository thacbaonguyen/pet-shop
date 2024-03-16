package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.MissingImage;
import com.petbackend.thbao.models.PetMissing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingImageResponse {
    private Long id;

    private String url;

    private PetMissingResponse petMissingResponse;

    public static final MissingImageResponse fromMissingImageResponse(MissingImage missingImage){
        return MissingImageResponse.builder()
                .id(missingImage.getId())
                .url(missingImage.getUrl())
                .petMissingResponse(PetMissingResponse.fromPetMissingResponse(missingImage.getPetMissing())).build();
    }
}
