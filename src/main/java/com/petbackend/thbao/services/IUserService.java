package com.petbackend.thbao.services;

import com.nimbusds.jose.JOSEException;
import com.petbackend.thbao.dtos.TokenDTO;
import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.dtos.UserLoginDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.exceptions.InvalidPasswordException;
import com.petbackend.thbao.exceptions.PermissionDenyException;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.responses.IntrospectResponse;
import com.petbackend.thbao.responses.UserLoginResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.text.ParseException;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException;

    UserLoginResponse login(UserLoginDTO userLoginDTO) throws DataNotFoundException, InvalidPasswordException;

    IntrospectResponse introspect(TokenDTO tokenDTO) throws JOSEException, ParseException;
    User getMyInfo() throws DataNotFoundException;
}
