package com.example.demo.controller;

import com.example.demo.domain.model.UserRegisterBindingModel;
import com.example.demo.domain.model.UserViewModel;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@PreAuthorize("permitAll()")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserViewModel registerUser(@RequestBody UserRegisterBindingModel bindingModel){
        if(bindingModel == null || !this.userService.register(bindingModel)){
            throw new IllegalArgumentException("Something went wrong.....");
        }

        return new UserViewModel(bindingModel.getUsername(), bindingModel.getEmail());
    }


}
