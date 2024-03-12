package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.MissingImageDTO;
import com.petbackend.thbao.dtos.PetMissingDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PetMissingService implements IPetMissingService{
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
    public PetMissing getPetMissingById(Long id) {
        return petMissingRepository.findById(id).orElseThrow(()->new DateTimeException("Cannot found Pet missing"));
    }

    @Override
    public PetMissing getPetMissingByUserId(Long userId) {
        return petMissingRepository.findByUserId(userId);
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
        petMissing.setCategory(category);
        petMissing.setUser(user);
        petMissingRepository.save(petMissing);
        return petMissing;
    }

    @Override
    public void delete(Long id) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        petMissingRepository.delete(petMissing);
    }

    @Override
    public MissingImage createMissingImage(Long id, MissingImageDTO missingImageDTO) throws DataNotFoundException {
        PetMissing petMissing = petMissingRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found Pet missing"));
        MissingImage missingImage = MissingImage.builder()
                .petMissing(petMissing)
                .url(missingImageDTO.getUrl()).build();
        missingImageRepository.save(missingImage);
        return missingImage;
    }
}
