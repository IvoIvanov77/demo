package com.example.demo.service;

import com.example.demo.domain.Employee;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.List;

public interface EmployeeService {


    Resource<Employee> get(Long id);


    Resources<Resource<Employee>> getAll();

    Employee createEmployee(Employee employee);

    Employee edit(Employee employee);

    Employee delete(Long id);
}
