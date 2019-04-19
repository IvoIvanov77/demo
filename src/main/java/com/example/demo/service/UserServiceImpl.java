package com.example.demo.service;

import com.example.demo.constants.ErrorMessages;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.entities.UserRole;
import com.example.demo.domain.enums.Role;
import com.example.demo.domain.model.UserRegisterRequestModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService
{


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND_ERROR_MESSAGE));
    }

    @Override
    public boolean register(UserRegisterRequestModel bindingModel)
    {
        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword()))
        {
            throw new IllegalArgumentException(ErrorMessages.PASSWORDS_DOES_NOT_MATCH_ERROR_MESSAGE);
        }
        /// TODO: 4/16/19 ModelMapper
        User user = new User();
        user.setUsername(bindingModel.getUsername());
        user.setPassword(this.passwordEncoder.encode(bindingModel.getPassword()));
        user.setEmail(bindingModel.getEmail());
        user.setAuthorities(this.getRolesForRegistration());

        try
        {
            this.userRepository.save(user);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private Set<UserRole> setUserRoles(Role role)
    {
        Set<UserRole> roles = new HashSet<>();
        for (Role r : Role.values()) {
            UserRole roleToAdd = this.roleRepository.findByAuthority(r.name()).orElse(null);
            roles.add(roleToAdd);
            if(r.equals(role)){
                break;
            }
        }
        return roles;
    }

    private Set<UserRole> getRolesForRegistration()
    {
        if (this.userRepository.count() == 0)
        {
            return this.setUserRoles(Role.ROLE_ADMIN);
        }
        return this.setUserRoles(Role.ROLE_USER);
    }

}
