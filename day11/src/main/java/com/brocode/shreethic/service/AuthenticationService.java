package com.brocode.shreethic.service;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brocode.shreethic.config.AuthenticationResponse;
import com.brocode.shreethic.controller.AuthenticationRequest;
import com.brocode.shreethic.controller.RegisterRequest;
import com.brocode.shreethic.model.User;
import com.brocode.shreethic.model.enumerated.Role;
import com.brocode.shreethic.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder; // Use final here as well.
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repo; // Use final to indicate this field should not be reassigned.
    public AuthenticationResponse register(RegisterRequest request) {
        var user =User.builder()
        		.username(request.getUsername())
        		.password(passwordEncoder.encode(request.getPassword()))// Your code to save the user or perform other registration logic goes here.
        		.role(Role.User)
        		.build();
        repo.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        		.token(jwtToken)
        		.build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
      
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
                )
            );
            System.out.println("apply");
            var user = repo.findByUsername(request.getUsername())
                .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
     
    }

}