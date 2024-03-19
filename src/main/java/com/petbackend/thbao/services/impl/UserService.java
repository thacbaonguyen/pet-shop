package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.exceptions.PermissionDenyException;
import com.petbackend.thbao.models.Role;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.RoleRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exist");
        }
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()->
                 new DataNotFoundException("Cannot found this role"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot register an admin account");
        }
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .active(true)
                .build();
        user.setRole(role);
        return userRepository.save(user);
    }
    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}
