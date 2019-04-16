package com.example.demo.repository;

import com.example.demo.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Long> {


}
