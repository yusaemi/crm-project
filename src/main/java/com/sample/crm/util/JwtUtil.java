package com.sample.crm.util;

import com.sample.crm.vo.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * JwtUtil. 2020/11/20 3:54 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT_BEARER = "Bearer";
    private static final long EXPIRATION_MINUTES = 30;

    private final String jwtSignKey;
    private final StringRedisTemplate stringRedisTemplate;

    public String generateJwt(UserProfile userProfile) {
        String username = userProfile.getUsername();
        String role = userProfile.getRole();
        LocalDateTime expDateTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        String jwt = Jwts.builder()
                .setAudience(username)
                .setSubject(role)
                .setExpiration(Timestamp.valueOf(expDateTime))
                .signWith(SignatureAlgorithm.HS512, jwtSignKey)
                .compact();
        saveToRedis(username, jwt);
        return jwt;
    }

    public String getJwt(HttpServletRequest httpServletRequest) {
        String jwt = httpServletRequest.getHeader(AUTH_HEADER);
        return StringUtils.isBlank(jwt) ? jwt : jwt.replaceFirst(JWT_BEARER, "").trim();
    }

    public boolean checkRedisJwt(String jwt) {
        String redisJwt = getRedisJwt(getUsername(jwt));
        return jwt.equals(redisJwt);
    }

    public String getRedisJwt(String username) {
        return stringRedisTemplate.opsForValue().get(username);
    }

    public void deleteRedisJwt(String jwt) {
        stringRedisTemplate.delete(getUsername(jwt));
    }

    public String getUsername(String jwt) {
        return getClaims(jwt).getAudience();
    }

    private void saveToRedis(String username, String jwt) {
        stringRedisTemplate.opsForValue().set(username, jwt, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser().setSigningKey(jwtSignKey).parseClaimsJws(jwt).getBody();
    }

}
