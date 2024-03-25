package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.MissingImageDTO;
import com.petbackend.thbao.dtos.PetMissingDTO;
import com.petbackend.thbao.exceptions.AccessDeniedException;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.models.MissingImage;
import com.petbackend.thbao.models.PetMissing;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.CategoryRepository;
import com.petbackend.thbao.repositories.MissingImageRepository;
import com.petbackend.thbao.repositories.PetMissingRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.PetMissingResponse;
import com.petbackend.thbao.services.IPetMissingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PetMissingService implements IPetMissingService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PetMissingRepository petMissingRepository;
    private final MissingImageRepository missingImageRepository;
    @Override
    public PetMissing createPetMissing(PetMissingDTO petMissingDTO) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(petMissingDTO.getUserId());
        Optional<Category> category = categoryRepository.findById(petMissingDTO.getCategoryId());
        if(!user.isPresent()){
            throw new DataNotFoundException("Cannot found user");
        }
        if(!category.isPresent()){
            throw new DataNotFoundException("Cannot found category");
        }
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // Chỉ tạo được đơn trình báo cho bản thân
        if(!user.get().getPhoneNumber().equals(name)){
            throw new AccessDeniedException("You cannot create an missing for someone else");
        }

        LocalDate missingTimeString = petMissingDTO.getMissingTime();
        PetMissing petMissing = PetMissing.builder()
                .name(petMissingDTO.getName())
                .color(petMissingDTO.getColor())
                .age(petMissingDTO.getAge())
                .sex(petMissingDTO.getSex())
                .description(petMissingDTO.getDescription())
                .status(petMissingDTO.getStatus())
                .missingTime(missingTimeString)
                .category(category.get())
                .user(user.get()).build();
        petMissingRepository.save(petMissing);
        return petMissing;
    }

    @Override
    public PetMissing getPetMissingById(Long id) throws DataNotFoundException {
        return petMissingRepository.findById(id).orElseThrow(()->new DateTimeException("Cannot found Pet missing"));
    }

    @Override
    public List<PetMissing> getPetMissingByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->
                new DataNotFoundException("Cannot found user"));
        return petMissingRepository.findByUserId(userId);
    }

    @Override
    public List<PetMissing> getPetMissingByCategoryId(Long categoryId) throws DataNotFoundException {
        return petMissingRepository.findByCategoryId(categoryId);
    }

    @Override
    public Page<PetMissingResponse> getAllPetMissing(PageRequest pageRequest) {
        return petMissingRepository.findAll(pageRequest).map(PetMissingResponse::fromPetMissingResponse);
    }
    @Override
    public PetMissing updatePetMissing(Long id, PetMissingDTO petMissingDTO) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        petMissing.setName(petMissingDTO.getName());
        petMissing.setColor(petMissingDTO.getColor());
        petMissing.setAge(petMissingDTO.getAge());
        petMissing.setSex(petMissingDTO.getSex());
        petMissing.setDescription(petMissingDTO.getDescription());
        petMissing.setStatus(petMissingDTO.getStatus());
        petMissing.setMissingTime(petMissingDTO.getMissingTime());
        Category category = categoryRepository.findById(petMissingDTO.getCategoryId()).orElseThrow(()->
                new DataNotFoundException("Cannot found category"));
        User user = userRepository.findById(petMissingDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot found user"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!petMissing.getUser().getPhoneNumber().equals(phoneNumber)){
            // chỉ update được bao cao của bản thân
            throw new AccessDeniedException("You cannot update an missing for someone else");
        }
        petMissing.setCategory(category);
        petMissing.setUser(user);
        petMissingRepository.save(petMissing);
        return petMissing;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petMissing.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot delete for someone else missing");
        }
        petMissingRepository.delete(petMissing);
    }

    @Override
    public MissingImage createMissingImage(Long id, MissingImageDTO missingImageDTO) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != petMissing.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot import image for someone else missing");
        }
        MissingImage missingImage = MissingImage.builder()
                .petMissing(petMissing)
                .url(missingImageDTO.getUrl()).build();
        missingImageRepository.save(missingImage);
        return missingImage;
    }
}
