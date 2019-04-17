package com.example.demo.configuration;

import com.example.demo.domain.entities.Employee;
import com.example.demo.domain.entities.UserRole;
import com.example.demo.domain.enums.Role;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class SeedDatabase {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("first employee", "boss")));
            log.info("Preloading " + employeeRepository.save(new Employee("second employee", "new role")));
            if(roleRepository.count() == 0){
                for (Role role : Role.values()) {
                    log.info("Preloading " + roleRepository.save(new UserRole(role.name())));
                }
            }

        };
    }
}
