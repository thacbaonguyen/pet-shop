package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.OrderAdoption;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAdoptionResponse {
    private String fullName;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    private String email;

    private Long userId;

    private Long petAdoptionId;

    public static final OrderAdoptionResponse fromOrderAdoptionResponse(OrderAdoption orderAdoption){
        return OrderAdoptionResponse.builder()
                .fullName(orderAdoption.getFullName())
                .phoneNumber(orderAdoption.getPhoneNumber())
                .address(orderAdoption.getAddress())
                .dateOfBirth(orderAdoption.getDateOfBirth())
                .email(orderAdoption.getEmail())
                .userId(orderAdoption.getUser().getId())
                .petAdoptionId(orderAdoption.getPetAdoption().getId()).build();
    }
}
