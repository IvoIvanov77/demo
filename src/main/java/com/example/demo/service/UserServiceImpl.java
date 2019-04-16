package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.model.UserRegisterBindingModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found.";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE)) ;
    }

    @Override
    public boolean register(UserRegisterBindingModel bindingModel){
        if(!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())){
            throw new IllegalArgumentException("Password and confirm password does not match");
        }
        /// TODO: 4/16/19 ModelMapper
        User user = new User();
        user.setUsername(bindingModel.getUsername());
        user.setPassword(this.passwordEncoder.encode(bindingModel.getPassword()));
        user.setEmail(bindingModel.getEmail());

        try {
            this.userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}