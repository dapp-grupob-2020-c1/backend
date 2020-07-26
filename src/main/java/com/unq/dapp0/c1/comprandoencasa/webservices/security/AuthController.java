package com.unq.dapp0.c1.comprandoencasa.webservices.security;

import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.ApiResponse;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.AuthResponse;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.LoginRequest;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.SignUpRequest;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try{
            User result = userService.registerUser(
                    signUpRequest.getEmail(),
                    signUpRequest.getName(),
                    signUpRequest.getPassword()
            );
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/user/me")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "User registered successfully@"));
        } catch (FieldAlreadyExistsException e){
            throw new BadRequestException(e.getMessage());
        }
    }

}
