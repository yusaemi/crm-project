package com.sample.crm.api.service;

import com.sample.crm.api.domain.EmployeeRequest;
import com.sample.crm.system.service.TokenService;
import com.sample.crm.system.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService. 2020/11/20 1:29 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String login(EmployeeRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserProfile userProfile = (UserProfile) authenticate.getPrincipal();
        return tokenService.generate(username, userProfile.getRole());
    }

}
