package com.sample.crm.service;

import com.sample.crm.entity.Employee;
import com.sample.crm.repository.EmployeeDao;
import com.sample.crm.util.JwtUtil;
import com.sample.crm.util.StringUtil;
import com.sample.crm.vo.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import static java.util.Objects.nonNull;

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
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean valid = true;
        UserProfile userProfile = null;

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = httpServletRequest.getRequestURI();
        if (StringUtil.isNotBlank(requestURI) && !requestURI.endsWith("/auth/login")) {
            String redisJwt = jwtUtil.getRedisJwt(username);
            valid = StringUtil.isNotBlank(redisJwt);
        }

        if (valid) {
            Employee employee = employeeDao.findById(username).orElse(null);
            if (nonNull(employee)) {
                userProfile = UserProfile.builder()
                        .username(employee.getUsername())
                        .password(employee.getPassword())
                        .role(employee.getRole())
                        .build();
            }
        }

        return userProfile;
    }

}
