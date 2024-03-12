package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.OrderAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAdoptionRepository extends JpaRepository<OrderAdoption, Long> {
}
