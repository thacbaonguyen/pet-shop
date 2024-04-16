package com.petbackend.thbao.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false, length = 255)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "address", nullable = true, length = 255)
    private String address;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = true, length = 255)
    private String email;

    @Column(name = "is_active")
    private boolean active;

    private String otp;

    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
