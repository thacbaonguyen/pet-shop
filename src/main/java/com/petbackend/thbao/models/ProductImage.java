package com.petbackend.thbao.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
