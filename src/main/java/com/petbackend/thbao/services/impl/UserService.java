package com.petbackend.thbao.services.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.petbackend.thbao.dtos.TokenDTO;
import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.dtos.UserLoginDTO;
import com.petbackend.thbao.exceptions.InvalidAccountException;
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
import com.petbackend.thbao.utils.EmailUtil;
import com.petbackend.thbao.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final OtpUtil otpUtil;
    final EmailUtil emailUtil;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException, InvalidAccountException {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(userDTO.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exist");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())){
            throw new InvalidAccountException("Email already exist");
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
                .otp(otp)
                .otpGeneratedTime(LocalDateTime.now())
                .dateOfBirth(userDTO.getDateOfBirth())
                .build();
        user.setRole(role);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }
    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }
    @Override
    public UserLoginResponse login(UserLoginDTO userLoginDTO) throws DataNotFoundException,
            InvalidPasswordException, InvalidAccountException {
        User user = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber()).orElseThrow(()->
                new DataNotFoundException("Cannot found user with phone number"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
        if(!authenticated){
            throw new InvalidPasswordException("The password is incorrect");
        }
        if (!user.isActive()){
            throw new InvalidAccountException("The account has not been activated");
        }
        Role role = user.getRole();
        var token = generateToken(userLoginDTO, role);
        return UserLoginResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }
    @Override
    public String forgotPassword(String email) throws DataNotFoundException {
        String otp = otpUtil.generateOtp();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        try {
            emailUtil.sentSetPasswordEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send set password email please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Please check your email to set password";
    }
    @Override
    public User setPassword(String email, String otp, String newPassword) throws DataNotFoundException, InvalidAccountException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new DataNotFoundException("User not exist"));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return user;
        }
        throw new InvalidAccountException("Please check again your otp or regenerate otp");
    }
    public String generateToken(UserLoginDTO userLoginDTO, Role role){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Date expirationTime = Date.from(ZonedDateTime.now().plusDays(20).toInstant());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userLoginDTO.getPhoneNumber())
                .issuer("pet-app.com")
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .claim("scope", role.getName())
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
    @Override
    public User getMyInfo() throws DataNotFoundException {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByPhoneNumber(name).orElseThrow(() ->
                new DataNotFoundException("Cannot not existing"));
        return user;
    }
    @Override
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
