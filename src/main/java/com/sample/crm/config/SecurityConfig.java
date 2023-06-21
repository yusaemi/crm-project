package com.sample.crm.config;

import com.sample.crm.domain.RoleEnum;
import com.sample.crm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

/**
 * SecurityConfig. 2020/11/20 11:26 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final TokenFilterConfig tokenFilterConfig;
    private final LogoutConfig logoutConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/health"), AntPathRequestMatcher.antMatcher("/auth/login")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).access((authentication, context) -> {
                            String httpMethod = context.getRequest().getMethod();
                            Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                            return new AuthorizationDecision(
                                switch (httpMethod) {
                                    case "GET", "OPTIONS" -> true;
                                    case "POST" -> authorities.stream().anyMatch(authority -> StringUtil.equalsAny(authority.toString(), RoleEnum.SUPERUSER.getCode(), RoleEnum.OPERATOR.getCode()));
                                    case "PUT", "DELETE" -> authorities.stream().anyMatch(authority -> StringUtil.equalsAny(authority.toString(), RoleEnum.SUPERUSER.getCode(), RoleEnum.MANAGER.getCode()));
                                    default -> false;
                                });
                        })
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/*.html"),
                                AntPathRequestMatcher.antMatcher("/*/*.html"),
                                AntPathRequestMatcher.antMatcher("/*/*.css"),
                                AntPathRequestMatcher.antMatcher("/*/*.js")
                        ).access((authentication, context) -> new AuthorizationDecision(StringUtil.equals("GET", context.getRequest().getMethod())))
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/v1/auth/**"),
                                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                                AntPathRequestMatcher.antMatcher("/v3/api-docs.yaml"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui.html")).anonymous()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenFilterConfig, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessHandler(logoutConfig))
                .headers(headers -> headers.cacheControl(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
