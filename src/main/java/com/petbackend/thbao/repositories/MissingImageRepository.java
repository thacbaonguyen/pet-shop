package com.petbackend.thbao.repositories;

import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.MissingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingImageRepository extends JpaRepository<MissingImage, Long> {
}
