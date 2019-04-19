package com.example.demo.service;

import com.example.demo.domain.entities.User;
import com.example.demo.domain.model.UserRegisterRequestModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    boolean register(UserRegisterRequestModel bindingModel);
}
