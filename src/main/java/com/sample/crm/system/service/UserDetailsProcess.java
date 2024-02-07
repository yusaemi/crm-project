package com.sample.crm.system.service;

import com.sample.crm.dao.entity.Employee;
import com.sample.crm.dao.repository.EmployeeDao;
import com.sample.crm.system.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsProcess. 2020/11/20 3:51 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserDetailsProcess implements UserDetailsService {

    private final EmployeeDao employeeDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeDao.findById(username).orElse(null);
        if (employee == null) {
            return UserProfile.builder().username(username).build();
        }

        return UserProfile.builder()
                .username(employee.getUsername())
                .password(employee.getPassword())
                .role(employee.getRole())
                .build();
    }

}
