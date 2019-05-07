package com.example.demo.repository;

import com.example.demo.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long>
{

    Optional<UserRole> findByAuthority(String authority);
}
