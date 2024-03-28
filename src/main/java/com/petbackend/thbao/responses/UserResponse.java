package com.petbackend.thbao.responses;

import com.petbackend.thbao.models.Role;
import com.petbackend.thbao.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE )
public class UserResponse {
    Long id;

    String fullName;

    String phoneNumber;

    String address;

    String email;

    Role role;
    public static final UserResponse fromUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
