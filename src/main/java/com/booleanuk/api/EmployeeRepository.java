package com.booleanuk.api;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.postgresql.ds.PGSimpleDataSource;

public class EmployeeRepository {

    DataSource datasource;
    String dbUser;
    String dbURL;
    String dbPassword;
    String dbDatabase;
    Connection connection;

    public EmployeeRepository() throws SQLException {
        this.getDatabaseCredentials();
        this.datasource = this.createDataSource();
        this.connection = this.datasource.getConnection();
    }

    private void getDatabaseCredentials() {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            this.dbUser = prop.getProperty("db.user");
            this.dbURL = prop.getProperty("db.url");
            this.dbPassword = prop.getProperty("db.password");
            this.dbDatabase = prop.getProperty("db.database");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private DataSource createDataSource() {
        final String url = "jdbc:postgresql://" + this.dbURL + ":5432/" + this.dbDatabase + "?user=" + this.dbUser +"&password=" + this.dbPassword;
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public Employee create(Employee newEmployee) throws SQLException {
        String sql = "INSERT INTO Employee(name, jobName, salaryGrade, department) VALUES(?, ?, ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, newEmployee.getName());
        statement.setString(2, newEmployee.getJobName());
        statement.setString(3, newEmployee.getSalaryGrade());
        statement.setString(4, newEmployee.getDepartment());

        if (statement.executeUpdate() == 0) {
            return null;
        } else {
            long id = 0;
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            newEmployee.setId(id);
        }
        return newEmployee;
    }

    public List<Employee> getAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM employee");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("jobName"),
                    resultSet.getString("salaryGrade"),
                    resultSet.getString("department")
            );
            employees.add(employee);
        }
        return employees;
    }

}
