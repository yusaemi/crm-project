package com.sample.crm.system.domain;

import com.sample.crm.util.ListUtil;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.List;

/**
 * SecurityConstant
 *
 * @author sero
 * @since 2024/5/13
 **/
public final class SecurityConstant {

    private SecurityConstant() {}

    public static final String URL_LOGIN = "/auth/login";
    public static final String URL_ACTUATOR_HEALTH = "/actuator/health";
    public static final String URL_ACTUATOR_INFO = "/actuator/info";
    public static final String URL_ACTUATOR_METRICS = "/actuator/metrics";

    public static final List<AntPathRequestMatcher> URL_IGNORE = Collections.unmodifiableList(ListUtil.as(
            AntPathRequestMatcher.antMatcher(URL_LOGIN),
            AntPathRequestMatcher.antMatcher(URL_ACTUATOR_HEALTH),
            AntPathRequestMatcher.antMatcher(URL_ACTUATOR_INFO),
            AntPathRequestMatcher.antMatcher(URL_ACTUATOR_METRICS)
    ));

}
