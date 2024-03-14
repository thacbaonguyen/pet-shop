package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.OrderAdoption;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderAdoptionRepository extends JpaRepository<OrderAdoption, Long> {
    List<OrderAdoption> findByUserId(Long userId);
    List<OrderAdoption> findByPetAdoptionId(Long petAdoptionId);
}
