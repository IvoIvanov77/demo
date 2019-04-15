package com.example.demo.service;

import com.example.demo.controller.EmployeeController;
import com.example.demo.domain.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Service
public class EmployeeServiceImpl implements EmployeeService{


    public static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private final EmployeeRepository employeeRepository;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Resource<Employee> getById(Long id){
        Employee employee = this.employeeRepository
                .findById(id).orElseThrow(() -> new NoSuchElementException(EMPLOYEE_NOT_FOUND));
        return new Resource<>(employee, linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }

    @Override
    public Resources<Resource<Employee>> getAll() {

        List<Resource<Employee>> employees = this.employeeRepository.findAll().stream()
                .map(employee -> new Resource<>(employee,
                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return new Resources<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
}
