package com.sample.crm.system.config;

import com.sample.crm.dao.entity.Employee;
import com.sample.crm.dao.repository.EmployeeDao;
import com.sample.crm.system.domain.UserProfile;
import com.sample.crm.system.service.TokenService;
import com.sample.crm.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * TokenFilterConfig. 2020/11/20 3:49 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Configuration
@RequiredArgsConstructor
public class TokenFilterConfig extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final EmployeeDao employeeDao;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = tokenService.getJwt(httpServletRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtil.isNotBlank(jwt) && isNull(authentication) && tokenService.validate(jwt)) {
            String username = tokenService.getUsername(jwt);
            Employee employee = employeeDao.findById(username).orElseThrow(() -> new AccessDeniedException("User not found"));
            UserDetails userDetails = UserProfile.builder()
                    .username(employee.getUsername())
                    .password(employee.getPassword())
                    .role(employee.getRole())
                    .build();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
