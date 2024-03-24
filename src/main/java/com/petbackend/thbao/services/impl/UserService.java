package com.petbackend.thbao.services.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.petbackend.thbao.dtos.TokenDTO;
import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.dtos.UserLoginDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.exceptions.InvalidPasswordException;
import com.petbackend.thbao.exceptions.PermissionDenyException;
import com.petbackend.thbao.models.Role;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.repositories.RoleRepository;
import com.petbackend.thbao.repositories.UserRepository;
import com.petbackend.thbao.responses.IntrospectResponse;
import com.petbackend.thbao.responses.UserLoginResponse;
import com.petbackend.thbao.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Value("${jwt.secretKey}")
    private String secretKey;
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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public UserLoginResponse login(UserLoginDTO userLoginDTO) throws DataNotFoundException, InvalidPasswordException {
        User user = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber()).orElseThrow(()->
                new DataNotFoundException("Cannot found user with phone number"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
        if(!authenticated){
            throw new InvalidPasswordException("The password is incorrect");
        }
        var token = generateToken(userLoginDTO, user.getId());
        return UserLoginResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }
    private String generateToken(UserLoginDTO userLoginDTO, Long id){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Date expirationTime = Date.from(ZonedDateTime.now().plusDays(20).toInstant());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userLoginDTO.getPhoneNumber())
                .issuer("pet-app.com")
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .claim("userId", id)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject  jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    public IntrospectResponse introspect(TokenDTO tokenDTO) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(tokenDTO.getToken());
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean check = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(check && expiryTime.after(new Date()))
                .build();
    }
}
