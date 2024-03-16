package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionImageResponse {
    private Long id;

    private String url;

    private PetAdoptionResponse petAdoptionResponse;

    public static final AdoptionImageResponse fromAdoptionImageResponse(AdoptionImage adoptionImage){
        return AdoptionImageResponse.builder()
                .id(adoptionImage.getId())
                .url(adoptionImage.getUrl())
                .petAdoptionResponse(PetAdoptionResponse.fromPetAdoptionResponse(adoptionImage.getPetAdoption())).build();
    }
}
