package com.sample.crm.system.domain;

import com.sample.crm.util.ListUtil;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

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

    public static final List<PathPatternRequestMatcher> URL_IGNORE = Collections.unmodifiableList(ListUtil.as(
            PathPatternRequestMatcher.withDefaults().matcher(URL_LOGIN),
            PathPatternRequestMatcher.withDefaults().matcher(URL_ACTUATOR_HEALTH),
            PathPatternRequestMatcher.withDefaults().matcher(URL_ACTUATOR_INFO),
            PathPatternRequestMatcher.withDefaults().matcher(URL_ACTUATOR_METRICS)
    ));

}
