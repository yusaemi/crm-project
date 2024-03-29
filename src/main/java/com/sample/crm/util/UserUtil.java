package com.sample.crm.util;

import com.sample.crm.system.domain.UserProfile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * UserUtil. 2020/11/22 4:01 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Component
public class UserUtil {

    public UserProfile get() {
        return (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
