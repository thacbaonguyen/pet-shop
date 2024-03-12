package com.petbackend.thbao.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missing_images")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "missing_id")
    private PetMissing petMissing;
}
