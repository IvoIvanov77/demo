package com.example.demo.service;

import com.example.demo.domain.Employee;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.List;

public interface EmployeeService {


    Resource<Employee> getById(Long id);


    Resources<Resource<Employee>> getAll();
}
