package com.sample.crm.repository;

import com.sample.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserDao. 2020/11/21 1:07 上午
 *
 * @author sero
 * @version 1.0.0
 **/
public interface UserDao extends JpaRepository<User, String> {
}
