package com.example.demo.service;

import com.example.demo.domain.model.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean register(UserRegisterBindingModel bindingModel);
}
