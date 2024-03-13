package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.MissingImageDTO;
import com.petbackend.thbao.dtos.PetMissingDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.MissingImage;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.responses.PetMissingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPetMissingService {
    PetMissing createPetMissing(PetMissingDTO petMissingDTO) throws DataNotFoundException;

    PetMissing getPetMissingById(Long id);

    List<PetMissing> getPetMissingByUserId(Long userId);
    List<PetMissing> getPetMissingByCategoryId(Long categoryId);

    Page<PetMissingResponse> getAllPetMissing(PageRequest pageRequest);

    PetMissing updatePetMissing(Long id, PetMissingDTO petMissingDTO) throws DataNotFoundException;

    void delete(Long id) throws DataNotFoundException;

    MissingImage createMissingImage(Long id, MissingImageDTO missingImageDTO) throws DataNotFoundException;
}
