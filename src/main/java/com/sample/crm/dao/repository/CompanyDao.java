package com.sample.crm.dao.repository;

import com.sample.crm.dao.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CompanyDao. 2020/11/20 01:21 下午
 *
 * @author sero
 * @version 1.0.0
 **/
public interface CompanyDao extends JpaRepository<Company, Integer> {
}
