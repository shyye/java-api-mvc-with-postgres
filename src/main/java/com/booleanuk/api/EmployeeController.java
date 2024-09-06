package com.booleanuk.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private EmployeeRepository repository;

    public EmployeeController() throws SQLException {
        this.repository = new EmployeeRepository();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) throws SQLException {
        Employee employeeObject = this.repository.create(employee);
        if (employeeObject == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create employee");
        }
        return employeeObject;
    }

    @GetMapping
    public List<Employee> getAll() throws SQLException {
        return this.repository.getAll();
    }


}
