package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.web.controller.EmployeeController;
import com.example.demo.domain.entities.Employee;
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
    public Resource<Employee> get(Long id){
        return new Resource<>(this.getById(id), linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
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

    @Override
    public Employee createEmployee(Employee employee){
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee edit(Employee employee){
        Employee employeeToEdit = this.getById(employee.getId());

        employeeToEdit.setName(employee.getName());
        employeeToEdit.setRole(employee.getRole());
        return this.employeeRepository.save(employeeToEdit);
    }

    @Override
    public Employee delete(Long id){
        Employee employeeToDelete = this.getById(id);
        this.employeeRepository.delete(this.getById(id));
        return employeeToDelete;
    }

    private Employee getById(Long id){
        return this.employeeRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
    }


}
