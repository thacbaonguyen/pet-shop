package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.OrderAdoptionDTO;
import com.petbackend.thbao.exceptions.AccessDeniedException;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.models.OrderAdoption;
import com.petbackend.thbao.models.PetAdoption;
import com.petbackend.thbao.models.Role;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.OrderAdoptionRepository;
import com.petbackend.thbao.repositories.PetAdoptionRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.OrderAdoptionResponse;
import com.petbackend.thbao.services.IOrderAdoptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderAdoptionService implements IOrderAdoptionService {
    private final UserRepository userRepository;
    private final PetAdoptionRepository petAdoptionRepository;
    private final OrderAdoptionRepository orderAdoptionRepository;
    @Override
    @PreAuthorize("hasRole('USER')")
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // Chỉ tạo được đơn hàng cho bản thân
        if(!user.get().getPhoneNumber().equals(name)){
            throw new AccessDeniedException("You cannot create an order for someone else");
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
        petAdoptionRepository.save(petAdoption.get());
        return orderAdoption;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderAdoptionResponse> getAllOrderAdoption(PageRequest pageRequest) {
        return orderAdoptionRepository.findAll(pageRequest).map(OrderAdoptionResponse::fromOrderAdoptionResponse);
    }
    @Override
    public OrderAdoption getOrderAdoptionById(Long id) throws DataNotFoundException {
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User existUser = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (!role.contains("ROLE_ADMIN")){
            // Chỉ get được đơn hàng của bản thân
            return orderAdoptionRepository.findByIdAndUserId(id, existUser.getId()).orElseThrow(()->
                    new DataNotFoundException("Cannot found this order adoption"));
        }
        // nếu qua if, Admin có thế get hết
        return orderAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this order adoption"));
    }

    @Override
    public List<OrderAdoption> getOrderAdoptionByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->
                new DataNotFoundException("Cannot found this user"));
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User existUser = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (userId != existUser.getId() && !role.contains("ROLE_ADMIN")){
            // Nếu role user lấy đơn hàng từ id người khác thì bị lỗi
            throw new AccessDeniedException("You cannot access orders from other people");
        }
        return orderAdoptionRepository.findByUserId(userId);
    }

    @Override
    public List<OrderAdoption> getOrderAdoptionByPetAdoptionId(Long petAdoptionId) throws DataNotFoundException {
        PetAdoption petAdoption = petAdoptionRepository.findById(petAdoptionId).orElseThrow(()->
                new DataNotFoundException("Cannot found this pet adoption"));
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (!role.contains("ROLE_ADMIN")){
            // Chỉ lấy được đơn hàng của bản thân
            return orderAdoptionRepository.findByPetAdoptionIdAndUserId(petAdoptionId, user.getId());
        }
        // Admin get hết
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
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!existOrderAdoption.getUser().getPhoneNumber().equals(phoneNumber)){
            // chỉ update được đơn hàng của bản thân
            throw new AccessDeniedException("You cannot update an order for someone else");
        }
        existOrderAdoption.setUser(user);
        existOrderAdoption.setPetAdoption(petAdoption);
        orderAdoptionRepository.save(existOrderAdoption);
        return existOrderAdoption;
    }

    @Override
    public void delete(long id) throws DataNotFoundException {
        OrderAdoption orderAdoption = orderAdoptionRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot found this order adoption"));
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getId() != orderAdoption.getUser().getId() && !role.contains("ROLE_ADMIN")){
            throw new AccessDeniedException("You cannot delete this order");
        }
        orderAdoptionRepository.delete(orderAdoption);
    }
}
