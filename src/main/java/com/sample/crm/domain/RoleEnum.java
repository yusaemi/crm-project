package com.sample.crm.domain;

import lombok.Getter;

/**
 * RoleEnum
 *
 * @author Sero
 * @version 2023/8/19
 **/
@Getter
public enum RoleEnum {

    SUPERUSER("ROLE_superuser"),
    OPERATOR("ROLE_operator"),
    MANAGER("ROLE_manager");

    private final String code;

    RoleEnum(String code) {
        this.code = code;
    }

}
