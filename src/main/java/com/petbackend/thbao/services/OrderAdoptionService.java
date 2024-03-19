package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.OrderAdoptionDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.OrderAdoption;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.OrderAdoptionRepository;
import com.petbackend.thbao.repositories.PetAdoptionRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.OrderAdoptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderAdoptionService implements IOrderAdoptionService{
    private final UserRepository userRepository;
    private final PetAdoptionRepository petAdoptionRepository;
    private final OrderAdoptionRepository orderAdoptionRepository;
    @Override
    public OrderAdoption createOrderAdoption(OrderAdoptionDTO orderAdoptionDTO) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(orderAdoptionDTO.getUserId());
        Optional<PetAdoption> petAdoption = petAdoptionRepository.findById(orderAdoptionDTO.getPetAdoptionId());
        if(!user.isPresent()){
            throw new DataNotFoundException("Cannot found this user");
        }
        if(!petAdoption.isPresent()){
            throw new DataNotFoundException("Cannot found this pet adoption");
        }
        if(user.get().getId() == petAdoption.get().getUser().getId()){
            throw new DataNotFoundException("You cannot adoption your pet");
        }
        if(!petAdoption.get().isActive()){
            throw new DataNotFoundException("The pet has been claimed by someone else");
        }
        OrderAdoption orderAdoption = OrderAdoption.builder()
                .fullName(orderAdoptionDTO.getFullName())
                .phoneNumber(orderAdoptionDTO.getPhoneNumber())
                .address(orderAdoptionDTO.getAddress())
                .email(orderAdoptionDTO.getEmail())
                .dateOfBirth(orderAdoptionDTO.getDateOfBirth())
                .user(user.get())
                .petAdoption(petAdoption.get())
                .build();
        orderAdoptionRepository.save(orderAdoption);
        petAdoption.get().setActive(false);
        return orderAdoption;
    }

    @Override
    public Page<OrderAdoptionResponse> getAllOrderAdoption(PageRequest pageRequest) {
        return orderAdoptionRepository.findAll(pageRequest).map(OrderAdoptionResponse::fromOrderAdoptionResponse);
    }
    @Override
    public OrderAdoption getOrderAdoptionById(Long id) throws DataNotFoundException {
        return orderAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this order adoption"));
    }

    @Override
    public List<OrderAdoption> getOrderAdoptionByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->
                new DataNotFoundException("Cannot found this user"));
        return orderAdoptionRepository.findByUserId(userId);
    }

    @Override
    public List<OrderAdoption> getOrderAdoptionByPetAdoptionId(Long petAdoptionId) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(petAdoptionId).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet adoption"));
        return orderAdoptionRepository.findByPetAdoptionId(petAdoptionId);
    }
    @Override
    public OrderAdoption updateOrderAdoption(Long id, OrderAdoptionDTO orderAdoptionDTO) throws DataNotFoundException {
        OrderAdoption existOrderAdoption = orderAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this order adoption"));
        existOrderAdoption.setFullName(orderAdoptionDTO.getFullName());
        existOrderAdoption.setPhoneNumber(orderAdoptionDTO.getPhoneNumber());
        existOrderAdoption.setAddress(orderAdoptionDTO.getAddress());
        existOrderAdoption.setEmail(orderAdoptionDTO.getEmail());
        User user = userRepository.findById(orderAdoptionDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this user"));
        PetAdoption petAdoption = petAdoptionRepository.findById(orderAdoptionDTO.getPetAdoptionId()).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet adoption"));
        if(user.getId() == petAdoption.getUser().getId()){
            throw new DataNotFoundException("You cannot adoption your pet");
        }
        if(existOrderAdoption.getPetAdoption().getId() != orderAdoptionDTO.getPetAdoptionId()){
            if(!petAdoption.isActive()){
                throw new DataNotFoundException("The pet has been claimed by someone else");
            }
        }
        existOrderAdoption.setUser(user);
        existOrderAdoption.setPetAdoption(petAdoption);
        orderAdoptionRepository.save(existOrderAdoption);
        return existOrderAdoption;
    }

    @Override
    public void delete(long id) throws DataNotFoundException {
        orderAdoptionRepository.delete(orderAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this order adoption")));
    }
}
