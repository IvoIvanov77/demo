package com.example.demo.service;

import com.example.demo.constants.ErrorMessages;
import com.example.demo.domain.model.EmployeesSearchRequestModel;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.wew.controller.EmployeeController;
import com.example.demo.domain.entities.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@Service
public class EmployeeServiceImpl implements EmployeeService
{


    private final EmployeeRepository employeeRepository;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository)
    {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Resource<Employee> get(Long id)
    {
        return new Resource<>(this.getById(id), linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }

    @Override
    public Resources<Resource<Employee>> getAll()
    {

        List<Resource<Employee>> employees = this.employeeRepository.findAll().stream()
                .map(employee -> new Resource<>(employee,
                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return new Resources<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @Override
    public Employee createEmployee(Employee employee)
    {
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee edit(Employee employee)
    {
        Employee employeeToEdit = this.getById(employee.getId());

        employeeToEdit.setName(employee.getName());
        employeeToEdit.setRole(employee.getRole());
        employeeToEdit.setSalary(employee.getSalary());
        return this.employeeRepository.save(employeeToEdit);
    }

    @Override
    public Employee delete(Long id)
    {
        Employee employeeToDelete = this.getById(id);
        this.employeeRepository.delete(this.getById(id));
        return employeeToDelete;
    }


    @Override
    public List<Employee> searchEmployee(EmployeesSearchRequestModel requestModel)
    {
        String name = requestModel.getName() == null ? "" : requestModel.getName();
        String role = requestModel.getRole() == null ? "" : requestModel.getRole();
        BigDecimal minSalary = requestModel.getMinSalary() == null ? BigDecimal.ZERO : requestModel.getMinSalary();
        BigDecimal maxSalary = requestModel.getMaxSalary() == null ? BigDecimal.valueOf(Integer.MAX_VALUE)
                : requestModel.getMaxSalary();

        return this.employeeRepository.search(name, role, minSalary, maxSalary);
    }

    private Employee getById(Long id)
    {
        return this.employeeRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND));
    }


}
