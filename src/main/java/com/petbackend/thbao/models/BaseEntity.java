package com.petbackend.thbao.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass // Các trường trong BaseEntity sẽ được ánh xạ vào các cột trong bảng của các Entity kế thừa
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist // Là các annotation của JPA
    protected void onCreate(){
        createdAt = LocalDateTime.now(); // Gán thời gian tạo là thời điểm hiện tại
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate // Là các annotation của JPA
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
