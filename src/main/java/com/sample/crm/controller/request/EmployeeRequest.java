package com.sample.crm.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.crm.entity.Employee;

/**
 * LoginInfo. 2020/11/21 1:21 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@JsonIgnoreProperties("role")
public class EmployeeRequest extends Employee {
}
