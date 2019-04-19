package com.example.demo.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "employees")
public class Employee extends BaseEntity
{

    private String name;

    private String role;

    private BigDecimal salary;

    public Employee()
    {
    }

    public Employee(String name, String role)
    {
        this.name = name;
        this.role = role;
    }

    public Employee(String name, String role, BigDecimal salary)
    {
        this(name, role);
        this.salary = salary;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public BigDecimal getSalary()
    {
        return salary;
    }

    public void setSalary(BigDecimal salary)
    {
        this.salary = salary;
    }
}
