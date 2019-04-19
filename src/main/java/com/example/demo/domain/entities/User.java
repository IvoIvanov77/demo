package com.example.demo.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails
{

    private String username;

    private String email;

    private String password;

    private Set<UserRole> authorities;


    @Override
    @Column(nullable = false, unique = true)
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    @Column(nullable = false)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Column(nullable = false, unique = true)
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<UserRole> getAuthorities()
    {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired()
    {
        return true;
    }


    @Override
    @Transient
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled()
    {
        return true;
    }

}
