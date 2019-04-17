package com.example.demo.web.controller;

import com.example.demo.configuration.component.jwt.JwtTokenProvider;
import com.example.demo.domain.model.AuthenticationRequestModel;
import com.example.demo.domain.model.UserRegisterBindingModel;
import com.example.demo.domain.model.UserViewModel;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/users")
@PreAuthorize("permitAll()")
public class UserController {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;


    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;

    }

    @PostMapping("/register")
    public UserViewModel registerUser(@RequestBody UserRegisterBindingModel bindingModel){
        if(bindingModel == null || !this.userService.register(bindingModel)){
            throw new IllegalArgumentException("Something went wrong.....");
        }

        return new UserViewModel(bindingModel.getUsername(), bindingModel.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestModel requestModel) {
        try {
            String username = requestModel.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestModel.getPassword()));
            String token = jwtTokenProvider.createToken(username,
                    this.userService.loadUserByUsername(username).getAuthorities());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("error");
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}



