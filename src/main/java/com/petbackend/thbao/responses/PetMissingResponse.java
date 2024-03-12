package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetMissingResponse {
    private String name;

    private String color;

    private String age;

    private String sex;

    private String description;

    private String status;

    private LocalDate missingTime;

    private Long categoryId;

    private Long userId;

    public static final PetMissingResponse fromPetMissingResponse(PetMissing petMissing){
        return PetMissingResponse.builder()
                .name(petMissing.getName())
                .color(petMissing.getColor())
                .age(petMissing.getAge())
                .sex(petMissing.getSex())
                .description(petMissing.getDescription())
                .status(petMissing.getStatus())
                .missingTime(petMissing.getMissingTime())
                .categoryId(petMissing.getCategory().getId())
                .userId(petMissing.getUser().getId()).build();
    }
}
