package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.AdoptionImageDTO;
import com.petbackend.thbao.dtos.PetAdoptionDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.responses.PetAdoptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPetAdoptionService {
    PetAdoption createPetAdoption(PetAdoptionDTO petAdoptionDTO) throws DataNotFoundException;
    Page<PetAdoptionResponse> getAllPetAdoption(PageRequest pageRequest);

    PetAdoption getPetAdoptionById(Long id) throws DataNotFoundException;
    List<PetAdoption> getPetAdoptionByUserId(Long userId) throws DataNotFoundException;
    List<PetAdoption> getPetAdoptionByCategoryId(Long categoryId) throws DataNotFoundException;

    PetAdoption updatePetAdoption(Long id, PetAdoptionDTO petAdoptionDTO) throws DataNotFoundException;

    void delete(Long id) throws DataNotFoundException;

    AdoptionImage createAdoptionImage(Long petAdoptionId, AdoptionImageDTO adoptionImageDTO) throws DataNotFoundException;

}
