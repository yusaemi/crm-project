package com.sample.crm.dao.repository;

import com.sample.crm.dao.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * EmployeeDao. 2020/11/21 1:07 上午
 *
 * @author sero
 * @version 1.0.0
 **/
public interface EmployeeDao extends JpaRepository<Employee, String> {
}
