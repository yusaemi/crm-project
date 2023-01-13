package com.sample.crm.config;

import com.sample.crm.util.JwtUtil;
import com.sample.crm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtUtil.getJwt(httpServletRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtil.isNotBlank(jwt) && isNull(authentication)) {
            UserDetails userDetails = getUserDetails(jwt);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public UserDetails getUserDetails(String jwt) {
        return userDetailsService.loadUserByUsername(jwtUtil.getUsername(jwt));
    }

}
