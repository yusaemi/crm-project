package com.sample.crm.system.config;

import com.sample.crm.system.domain.RoleEnum;
import com.sample.crm.system.domain.SecurityConstant;
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
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

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
                        .requestMatchers(SecurityConstant.URL_IGNORE.toArray(new PathPatternRequestMatcher[0])).permitAll()
                        .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/**")).access((authentication, context) -> {
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
                                PathPatternRequestMatcher.withDefaults().matcher("/*.html"),
                                PathPatternRequestMatcher.withDefaults().matcher("/*/*.html"),
                                PathPatternRequestMatcher.withDefaults().matcher("/*/*.css"),
                                PathPatternRequestMatcher.withDefaults().matcher("/*/*.js")
                        ).access((authentication, context) -> new AuthorizationDecision(StringUtil.equals("GET", context.getRequest().getMethod())))
                        .requestMatchers(
                                PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs.yaml"),
                                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**"),
                                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui.html")).anonymous()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenFilterConfig, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/auth/logout").logoutSuccessHandler(logoutConfig))
                .headers(headers -> headers.cacheControl(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/h2-console/**"));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
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
