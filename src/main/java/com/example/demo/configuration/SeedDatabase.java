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

import java.math.BigDecimal;


@Configuration
@Slf4j
public class SeedDatabase
{
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, RoleRepository roleRepository)
    {
        Employee[] employees = {
                new Employee("ABC", "ZZZ", new BigDecimal("100")),
                new Employee("ABD", "ZZY", new BigDecimal("1000")),
                new Employee("CAB", "ZWW", new BigDecimal("500")),
                new Employee("ACD", "ZDD", new BigDecimal("300")),
                new Employee("AAA", "MMM", new BigDecimal("200")),
                new Employee("ACD", "HKK", new BigDecimal("50"))
        };
        return args ->
        {
            if (employeeRepository.count() == 0)
            {
                for (Employee employee : employees)
                {
//                    log.info("Preloading " + 
                employeeRepository.save(employee);
//                    );
                }
            }
            if (roleRepository.count() == 0)
            {
                for (Role role : Role.values())
                {
//                    log.info("Preloading " + 
                roleRepository.save(new UserRole(role.name()));
//                    );
                }
            }

        };
    }
}
