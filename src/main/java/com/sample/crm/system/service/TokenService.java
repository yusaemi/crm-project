package com.sample.crm.system.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sample.crm.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TokenService. 2020/11/20 3:54 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT_BEARER = "Bearer";
    private static final long EXPIRATION_MINUTES = 30;

    private final String jwtSignKey;
    private final StringRedisTemplate stringRedisTemplate;

    public String generate(String username, String role) {
        LocalDateTime expDateTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        String jwt = JWT.create()
                .withIssuer(username)
                .withSubject(role)
                .withExpiresAt(Timestamp.valueOf(expDateTime))
                .sign(Algorithm.HMAC512(jwtSignKey));
        saveRedis(username, jwt);
        return jwt;
    }

    public boolean validate(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSignKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(getUsername(jwt))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            return !isTokenExpired(decodedJWT);
        } catch (Exception e) {
            return false;
        }
    }

    public String getJwt(HttpServletRequest httpServletRequest) {
        String jwt = httpServletRequest.getHeader(AUTH_HEADER);
        return StringUtil.isBlank(jwt) ? jwt : jwt.replaceFirst(JWT_BEARER, "").trim();
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
        return decodedJWT(jwt).getIssuer();
    }

    private void saveRedis(String username, String jwt) {
        stringRedisTemplate.opsForValue().set(username, jwt, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiration = decodedJWT.getExpiresAt();
        return expiration.before(Timestamp.valueOf(LocalDateTime.now()));
    }

    private DecodedJWT decodedJWT(String jwt) {
        return JWT.decode(jwt);
    }

}
