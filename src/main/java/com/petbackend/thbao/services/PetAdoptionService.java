package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.AdoptionImageDTO;
import com.petbackend.thbao.dtos.PetAdoptionDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.AdoptionImage;
import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.AdoptionImageRepository;
import com.petbackend.thbao.repositories.CategoryRepository;
import com.petbackend.thbao.repositories.PetAdoptionRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.PetAdoptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PetAdoptionService implements IPetAdoptionService {
    private final PetAdoptionRepository petAdoptionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdoptionImageRepository adoptionImageRepository;
    @Override
    public PetAdoption createPetAdoption(PetAdoptionDTO petAdoptionDTO) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(petAdoptionDTO.getUserId());
        Optional<Category> category = categoryRepository.findById(petAdoptionDTO.getCategoryId());
        if(!user.isPresent()){
            throw new DataNotFoundException("Cannot found user");
        }
        if(!category.isPresent()){
            throw new DataNotFoundException("Cannot found category");
        }
        PetAdoption petAdoption = PetAdoption.builder()
                .name(petAdoptionDTO.getName())
                .color(petAdoptionDTO.getColor())
                .age(petAdoptionDTO.getAge())
                .sex(petAdoptionDTO.getSex())
                .health(petAdoptionDTO.getHealth())
                .description(petAdoptionDTO.getDescription())
                .phoneNumber(petAdoptionDTO.getPhoneNumber())
                .address(petAdoptionDTO.getAddress())
                .isActive(true)
                .category(category.get())
                .user(user.get()).build();
        petAdoptionRepository.save(petAdoption);
        return petAdoption;
    }

    @Override
    public Page<PetAdoptionResponse> getAllPetAdoption(PageRequest pageRequest) {
        return petAdoptionRepository.findAll(pageRequest).map(PetAdoptionResponse::fromPetAdoptionResponse);
    }

    @Override
    public PetAdoption getPetAdoptionById(Long id) throws DataNotFoundException {
        return petAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
    }

    @Override
    public List<PetAdoption> getPetAdoptionByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()-> new DataNotFoundException("Cannot found this user"));
        List<PetAdoption> list = petAdoptionRepository.findByUserId(userId);
        return list;
    }

    @Override
    public List<PetAdoption> getPetAdoptionByCategoryId(Long categoryId) throws DataNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new DataNotFoundException("Cannot found this category"));
        List<PetAdoption> list = petAdoptionRepository.findByCategoryId(categoryId);
        return list;
    }

    @Override
    public PetAdoption updatePetAdoption(Long id, PetAdoptionDTO petAdoptionDTO) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
        petAdoption.setName(petAdoptionDTO.getName());
        petAdoption.setColor(petAdoptionDTO.getColor());
        petAdoption.setAge(petAdoptionDTO.getAge());
        petAdoption.setSex(petAdoptionDTO.getSex());
        petAdoption.setHealth(petAdoptionDTO.getHealth());
        petAdoption.setDescription(petAdoptionDTO.getDescription());
        petAdoption.setPhoneNumber(petAdoptionDTO.getPhoneNumber());
        petAdoption.setAddress(petAdoptionDTO.getAddress());
        User user = userRepository.findById(petAdoptionDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this user"));
        Category category = categoryRepository.findById(petAdoptionDTO.getCategoryId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this category"));
        petAdoption.setUser(user);
        petAdoption.setCategory(category);
        petAdoptionRepository.save(petAdoption);
        return petAdoption;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
        petAdoptionRepository.delete(petAdoption);
    }

    @Override
    public AdoptionImage createAdoptionImage(Long petAdoptionId, AdoptionImageDTO adoptionImageDTO) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(petAdoptionId).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
        AdoptionImage adoptionImage = AdoptionImage.builder()
                .url(adoptionImageDTO.getUrl())
                .petAdoption(petAdoption).build();
        adoptionImageRepository.save(adoptionImage);
        return adoptionImage;
    }
}
