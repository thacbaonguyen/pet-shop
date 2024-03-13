package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetAdoptionResponse {
    private String name;

    private String color;

    private String age;

    private String sex;

    private String health;

    private String description;

    private String phoneNumber;

    private String address;

    private Long categoryId;

    private Long userId;
    public static final PetAdoptionResponse fromPetAdoptionResponse(PetAdoption petAdoption){
        return PetAdoptionResponse.builder()
                .name(petAdoption.getName())
                .color(petAdoption.getColor())
                .age(petAdoption.getAge())
                .sex(petAdoption.getSex())
                .health(petAdoption.getHealth())
                .description(petAdoption.getDescription())
                .phoneNumber(petAdoption.getPhoneNumber())
                .address(petAdoption.getAddress())
                .userId(petAdoption.getUser().getId())
                .categoryId(petAdoption.getCategory().getId()).build();
    }
}
