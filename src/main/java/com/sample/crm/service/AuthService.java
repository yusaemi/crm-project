package com.sample.crm.service;

import com.sample.crm.controller.request.UserRequest;
import com.sample.crm.util.JwtUtil;
import com.sample.crm.vo.UserProfile;
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
    private final JwtUtil jwtUtil;

    public String login(UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserProfile userProfile = (UserProfile) authenticate.getPrincipal();
        return jwtUtil.generateJwt(userProfile);
    }

}
