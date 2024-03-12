package com.petbackend.thbao.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_adoptions")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetAdoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, length = 255)
    private String color;

    @Column(nullable = false, length = 255)
    private String age;

    @Column(nullable = false, length = 255)
    private String sex;

    @Column(nullable = false, length = 255)
    private String health;

    private String description;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    private String address;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
