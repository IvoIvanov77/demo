package com.example.demo.domain.model;

import java.math.BigDecimal;

public class EmployeesSearchRequestModel
{

    private String name;

    private String role;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

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

    public BigDecimal getMinSalary()
    {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary)
    {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary()
    {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary)
    {
        this.maxSalary = maxSalary;
    }
}
