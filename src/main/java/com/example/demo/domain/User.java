package com.example.demo.domain;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.beans.Transient;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String username;

    private String email;

    private String password;

    private Set<UserRole> authorities;


    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }



    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {

    }

    public void setAccountNonLocked(boolean accountNonLocke) {

    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {

    }

    public void setEnabled(boolean enabled) {

    }



    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return false;
    }



}
