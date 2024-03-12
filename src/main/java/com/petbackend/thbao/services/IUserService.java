package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.exceptions.PermissionDenyException;
import com.petbackend.thbao.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException;

    String login(String phoneNumber, String password);
}
