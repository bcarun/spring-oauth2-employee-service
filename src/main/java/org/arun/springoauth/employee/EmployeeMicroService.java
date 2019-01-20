package org.arun.springoauth.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
                                  UserDetailsServiceAutoConfiguration.class})
public class EmployeeMicroService {

  public static void main(String[] args) {
    SpringApplication.run(EmployeeMicroService.class, args);
  }

  @RestController
  @RequestMapping(path = "/api/employees")
  @PreAuthorize("hasAnyAuthority('ROLE_READ_EMPLOYEE')")
  public static class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getEmployeeAndDepartment() {
      return employeeService.getEmployeeAndDepartment();
    }
  }

  @Service
  public static class EmployeeService {

    @Autowired
    private DepartmentRestClient departmentRestClient;

    public String getEmployeeAndDepartment() {
      String employeeName = "Arun";
      String departmentName = departmentRestClient.getDepartmentName();

      return "Employee Service Returned: " + employeeName + ", \nDepartment Service Returned: " + departmentName;
    }
  }

  @Component
  public static class DepartmentRestClient {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public String getDepartmentName() {
      return oAuth2RestTemplate.getForObject("http://localhost:8095/api/departments/1", String.class);
    }
  }
}

