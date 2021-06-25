package com.sample.crm.service;

import com.sample.crm.entity.User;
import com.sample.crm.repository.UserDao;
import com.sample.crm.util.JwtUtil;
import com.sample.crm.vo.UserProfile;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean valid = true;
        UserProfile userProfile = null;

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = httpServletRequest.getRequestURI();
        if (StringUtils.isNotBlank(requestURI) && !requestURI.endsWith("/auth/login")) {
            String redisJwt = jwtUtil.getRedisJwt(username);
            valid = StringUtils.isNotBlank(redisJwt);
        }

        if (valid) {
            User user = userDao.findById(username).orElse(null);
            if (nonNull(user)) {
                userProfile = UserProfile.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .role(user.getRole())
                        .build();
            }
        }

        return userProfile;
    }

}
