package com.petbackend.thbao.controllers;

import com.nimbusds.jose.JOSEException;
import com.petbackend.thbao.dtos.TokenDTO;
import com.petbackend.thbao.dtos.UserDTO;
import com.petbackend.thbao.dtos.UserLoginDTO;
import com.petbackend.thbao.exceptions.DataNotFoundException;
import com.petbackend.thbao.exceptions.InvalidAccountException;
import com.petbackend.thbao.exceptions.InvalidPasswordException;
import com.petbackend.thbao.exceptions.PermissionDenyException;
import com.petbackend.thbao.models.User;
import com.petbackend.thbao.responses.IntrospectResponse;
import com.petbackend.thbao.responses.UserLoginResponse;
import com.petbackend.thbao.responses.UserResponse;
import com.petbackend.thbao.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) throws DataNotFoundException, PermissionDenyException {
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
            return ResponseEntity.badRequest().body("Password does not match");
        }
        User user = null;
        try {
            user = userService.createUser(userDTO);
        } catch (InvalidAccountException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(UserResponse.fromUserResponse(user));
    }
    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }
    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(){
        try {
            User user = userService.getMyInfo();
            return ResponseEntity.ok(UserResponse.fromUserResponse(user));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try {
            UserLoginResponse userLoginResponse = userService.login(userLoginDTO);
            return ResponseEntity.ok(userLoginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
        try {
            return ResponseEntity.ok(userService.forgotPassword(email));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/set-password")
    public ResponseEntity<?> setPasswordEmail(@RequestParam String email,
                                              @RequestParam String otp,
                                              @RequestHeader String newPassword){
        try {
            User user = userService.setPassword(email,otp, newPassword);
            return ResponseEntity.ok(UserResponse.fromUserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody TokenDTO tokenDTO) throws ParseException, JOSEException {
            IntrospectResponse introspectResponse = userService.introspect(tokenDTO);
            return ResponseEntity.ok(introspectResponse);

    }
    @GetMapping("/secured")
    public String secured(){
        return "Hello, secured";
    }
}
