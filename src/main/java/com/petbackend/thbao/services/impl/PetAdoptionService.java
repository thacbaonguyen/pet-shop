package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.AdoptionImageDTO;
import com.petbackend.thbao.dtos.PetAdoptionDTO;
import com.petbackend.thbao.exceptions.AccessDeniedException;
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
import com.petbackend.thbao.services.IPetAdoptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!user.get().getPhoneNumber().equals(name)){
            throw new AccessDeniedException("You cannot create an adoption for someone else");
        }
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
        return petAdoptionRepository.findByUserId(userId);
    }

    @Override
    public List<PetAdoption> getPetAdoptionByCategoryId(Long categoryId) throws DataNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new DataNotFoundException("Cannot found this category"));
        return petAdoptionRepository.findByCategoryId(categoryId);
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
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!petAdoption.getUser().getPhoneNumber().equals(phoneNumber)){
            // chỉ update được adoption của bản thân
            throw new AccessDeniedException("You cannot update an adoption for someone else");
        }
        petAdoption.setUser(user);
        petAdoption.setCategory(category);
        petAdoptionRepository.save(petAdoption);
        return petAdoption;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petAdoption.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot delete this order");
        }
        petAdoptionRepository.delete(petAdoption);
    }

    @Override
    public AdoptionImage createAdoptionImage(Long petAdoptionId, AdoptionImageDTO adoptionImageDTO)
            throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(petAdoptionId).orElseThrow(()->
                new DataNotFoundException("Cannot found this Pet adoption"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petAdoption.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot delete this order");
        }
        AdoptionImage adoptionImage = AdoptionImage.builder()
                .url(adoptionImageDTO.getUrl())
                .petAdoption(petAdoption).build();
        adoptionImageRepository.save(adoptionImage);
        return adoptionImage;
    }
}
